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

import io.typedef.polar.model.Model.Date;
import io.typedef.polar.model.Model.ListMessage.ListEntry;
import io.typedef.polar.model.Model.Time;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class PolarEntry {

    private final Instant modified;
    private final String path;
    private final int size;

    public PolarEntry(String path) {
        this.modified = Instant.now();
        this.path = path;
        this.size = 0;
    }

    PolarEntry(String rootPath, ListEntry entry) {
        Date date = entry.getModified().getDate();
        Time time = entry.getModified().getTime();

        ZonedDateTime modified = ZonedDateTime.of(date.getYear(), date.getMonth(), date.getDay(),
            time.getHour(), time.getMinute(), time.getSecond(),
            0, ZoneId.systemDefault());

        this.modified = modified.toInstant();
        this.path = rootPath + entry.getPath();
        this.size = entry.getSize();
    }

    public boolean isDirectory() {
        return path.endsWith("/");
    }

    public boolean isFile() {
        return !isDirectory();
    }

    public Instant getModified() {
        return modified;
    }

    public String getPath() {
        return path;
    }

    public int getSize() {
        return size;
    }
}
