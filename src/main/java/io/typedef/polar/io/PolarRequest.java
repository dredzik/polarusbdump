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

import io.typedef.polar.model.Model.CommandMessage;
import io.typedef.polar.model.Model.CommandType;

public class PolarRequest {

    private String url;

    public PolarRequest(String url) {
        this.url = url;
    }

    public byte[] getData() {
        byte[] message = CommandMessage.newBuilder().setType(CommandType.READ).setPath(url).build().toByteArray();
        byte[] data = new byte[message.length + 3];

        data[0] = (byte) message.length;
        data[1] = 0x00;

        data[data.length - 1] = 0x00;

        System.arraycopy(message, 0, data, 2, message.length);
        return data;
    }
}
