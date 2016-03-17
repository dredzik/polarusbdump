package pl.niekoniecznie.p2e.downloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.polar.io.PolarEntry;
import pl.niekoniecznie.polar.io.PolarFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.function.Consumer;

public class FileDownloader implements Consumer<PolarEntry> {

    private final static Logger logger = LogManager.getLogger(FileDownloader.class);
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

        InputStream source = filesystem.get(entry);
        Path destination = Paths.get(backupDirectory.toString(), entry.getPath());

        try {
            Files.deleteIfExists(destination);
            Files.copy(source, destination);
            Files.setLastModifiedTime(destination, FileTime.from(entry.getModified()));
            logger.trace("downloaded " + destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
