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
import io.typedef.polar.io.PolarFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.function.Consumer;

public class FileDownloader implements Consumer<PolarEntry> {

    private final Path backupDirectory;
    private final PolarFileSystem filesystem;

    public FileDownloader(Path backupDirectory, PolarFileSystem filesystem) {
        this.backupDirectory = backupDirectory;
        this.filesystem = filesystem;
    }

    @Override
    public void accept(PolarEntry entry) {
        if (entry.isDirectory()) {
            return;
        }

        Path target = backupDirectory.resolve(entry.getPath().substring(1));

        try (InputStream source = filesystem.get(entry)) {
            Files.deleteIfExists(target);
            Files.copy(source, target);
            Files.setLastModifiedTime(target, FileTime.from(entry.getModified()));
            System.out.println("[+] downloaded " + target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
