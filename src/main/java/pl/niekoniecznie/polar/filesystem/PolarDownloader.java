package pl.niekoniecznie.polar.filesystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PolarDownloader {

    PolarFileSystem fs = new PolarFileSystem();

    private final static Logger logger = LogManager.getLogger(PolarDownloader.class);

    public void download(Path root, String source) {
        logger.trace("Downloading " + source + " to " + root);

        if (source.endsWith("/")) {
            downloadDirectory(root, source);
        } else {
            downloadFile(root, source);
        }
    }

    private void downloadDirectory(Path root, String source) {
        Path destination = Paths.get(root.toString(), source);

        logger.trace("Creating directory " + destination);

        try {
            Files.createDirectories(destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fs.list(source).forEach((child) -> {
            if (child.endsWith("/")) {
                downloadDirectory(root, child);
            } else {
                downloadFile(root, child);
            }
        });
    }

    private void downloadFile(Path root, String source) {
        Path destination = Paths.get(root.toString(), source);

        logger.trace("Saving file " + destination);

        try {
            PolarFileInputStream is = new PolarFileInputStream(source);
            Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
