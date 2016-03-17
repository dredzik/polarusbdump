package pl.niekoniecznie.p2e.downloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.polar.io.PolarEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class DirectoryDownloader implements Consumer<PolarEntry> {

    private final static Logger logger = LogManager.getLogger(DirectoryDownloader.class);
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
            logger.trace("created " + destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
