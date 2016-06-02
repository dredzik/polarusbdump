package io.typedef.polarusbdump.downloader;

import io.typedef.polar.io.PolarEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class DirectoryDownloader implements Consumer<PolarEntry> {

    private final Path backupDirectory;

    public DirectoryDownloader(Path backupDirectory) {
        this.backupDirectory = backupDirectory;
    }

    @Override
    public void accept(PolarEntry entry) {
        if (entry.isFile()) {
            return;
        }

        Path destination = Paths.get(backupDirectory.toString(), entry.getPath());

        try {
            Files.createDirectory(destination);
            System.out.println("[+] created " + destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
