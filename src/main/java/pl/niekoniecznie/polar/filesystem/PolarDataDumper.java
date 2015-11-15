package pl.niekoniecznie.polar.filesystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PolarDataDumper {

    PolarFileSystem fs = new PolarFileSystem();

    private final static Logger logger = LogManager.getLogger(PolarDataDumper.class);

    public void dump(Path root, String source) {
        Path destination = Paths.get(root.toString(), source);

        logger.trace("Creating " + destination);

        if (fs.isDirectory(source)) {
            mkdir(destination);
            fs.list(source).forEach((child) -> dump(root, child));
        } else {
            copy(destination, source);
        }
    }

    private void mkdir(Path destination) {
        try {
            Files.createDirectories(destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void copy(Path destination, String source) {
        try {
            PolarFileInputStream is = new PolarFileInputStream(source);
            Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
