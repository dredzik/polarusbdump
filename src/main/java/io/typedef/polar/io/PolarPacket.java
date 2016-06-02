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

import java.util.Arrays;

public class PolarPacket {

    public static final int BUFFER_LENGTH = 64;
    public static final int MAX_DATA_SIZE = 61;

    private byte[] buffer;

    public PolarPacket(byte[] buffer) {
        if (buffer.length != BUFFER_LENGTH) {
            throw new IllegalArgumentException("Buffer size set to " + buffer.length + ", should be " + BUFFER_LENGTH);
        }

        this.buffer = buffer;
    }

    public int getType() {
        return buffer[0];
    }

    public void setType(int type) {
        buffer[0] = (byte) type;
    }

    public boolean hasMore() {
        return (buffer[1] & 0x03) == 1;
    }

    public void setMore(boolean more) {
        buffer[1] &= 0xfc;
        buffer[1] |= more ? 1 : 0;
    }

    public int getSize() {
        return ((buffer[1] & 0xfc) >> 2) - 1;
    }

    public void setSize(int size) {
        buffer[1] &= 0x03;
        buffer[1] |= (size + 1) << 2;
    }

    public int getSequence() {
        return buffer[2];
    }

    public void setSequence(int sequence) {
        buffer[2] = (byte) sequence;
    }

    public byte[] getData() {
        return Arrays.copyOfRange(buffer, 3, getSize() + 3);
    }

    public void setData(byte[] data) {
        if (data.length > MAX_DATA_SIZE) {
            throw new IllegalArgumentException("Data size set to " + data.length + ", shouldn't be greater than " + MAX_DATA_SIZE);
        }

        System.arraycopy(data, 0, buffer, 3, data.length);
        setSize(data.length);
    }

    public byte[] getBytes() {
        return buffer;
    }
}
