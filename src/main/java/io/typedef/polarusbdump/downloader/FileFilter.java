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

package io.typedef.polarusbdump.downloader;

import io.typedef.polar.io.PolarEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.function.Predicate;

public class FileFilter implements Predicate<PolarEntry> {

    private final Path backupDirectory;

    public FileFilter(Path backupDirectory) {
        this.backupDirectory = backupDirectory;
    }

    @Override
    public boolean test(PolarEntry entry) {
        if (entry.isDirectory()) {
            return true;
        }

        Path file = Paths.get(backupDirectory.toString(), entry.getPath());

        if (Files.notExists(file)) {
            return true;
        }

        Instant modified;

        try {
            modified = Files.getLastModifiedTime(file).toInstant();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!entry.getModified().equals(modified)) {
            return true;
        }

        return false;
    }
}
