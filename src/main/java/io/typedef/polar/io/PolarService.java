/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Adam Kuczynski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.typedef.polar.io;

import com.codeminders.hidapi.HIDDevice;

import java.io.IOException;

public class PolarService {

    private final HIDDevice device;

    public PolarService(final HIDDevice device) {
        this.device = device;
    }

    public void write(byte[] message) throws IOException {
        byte[] chunk = new byte[PolarPacket.MAX_DATA_SIZE];
        byte[] buffer = new byte[PolarPacket.BUFFER_LENGTH];
        PolarPacket packet = new PolarPacket(buffer);

        int count = (int) Math.ceil((double) message.length / PolarPacket.MAX_DATA_SIZE);

        for (int i = 0; i < count; i++) {
            boolean last = i == count - 1;
            int start = i * PolarPacket.MAX_DATA_SIZE;
            int size = PolarPacket.MAX_DATA_SIZE;

            if (last) {
                size = message.length % size;
            }

            System.arraycopy(message, start, chunk, 0, size);

            packet.setType(0x01);
            packet.setMore(!last);
            packet.setSequence(i);
            packet.setData(chunk);
            packet.setSize(size);

            device.write(buffer);

            if (!last) {
                device.read(buffer);
            }
        }
    }

    public PolarResponse read() throws IOException {
        PolarResponse response = new PolarResponse();

        byte[] buffer = new byte[PolarPacket.BUFFER_LENGTH];
        PolarPacket packet = new PolarPacket(buffer);

        while (true) {
            device.read(buffer);
            response.append(packet.getData());

            if (!packet.hasMore()) {
                break;
            }

            packet.setType(0x01);
            packet.setSize(0);

            device.write(buffer);
        }

        return response;
    }
}
