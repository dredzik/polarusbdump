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

import io.typedef.polar.model.Model.ListMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class PolarFileSystem {

    private final PolarService service;

    public PolarFileSystem(final PolarService service) {
        this.service = service;
    }

    public InputStream get(final PolarEntry file) {
        String path = file.getPath();
        PolarRequest request = new PolarRequest(path);
        PolarResponse response;

        try {
            service.write(request.getData());
            response = service.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new PolarInputStream(response.getBytes());
    }

    public List<PolarEntry> list(PolarEntry directory) {
        String path = directory.getPath();
        PolarRequest request = new PolarRequest(path);
        PolarResponse response;

        try {
            service.write(request.getData());
            response = service.read();
        } catch (IOException e) {
            throw new RuntimeException("Cannot list " + directory.getPath(), e);
        }

        try {
            return ListMessage
                .parseFrom(response.getBytes())
                .getEntryList()
                .stream()
                .map(x -> new PolarEntry(path, x))
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
