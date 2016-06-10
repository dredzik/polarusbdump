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

        Instant local;
        Instant remote = entry.getModified();

        if (remote == null) {
            return true;
        }

        try {
            local = Files.getLastModifiedTime(file).toInstant();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!remote.equals(local)) {
            return true;
        }

        return false;
    }
}
