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

        System.out.println("[+] downloading " + entry.getPath());
        Path target = backupDirectory.resolve(entry.getPath().substring(1));

        try (InputStream source = filesystem.get(entry)) {
            Files.deleteIfExists(target);
            Files.copy(source, target);
            if (entry.getModified() != null) {
                Files.setLastModifiedTime(target, FileTime.from(entry.getModified()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
