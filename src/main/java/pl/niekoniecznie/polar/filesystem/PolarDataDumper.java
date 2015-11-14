package pl.niekoniecznie.polar.filesystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PolarDataDumper {

    private final static Logger logger = LogManager.getLogger(PolarDataDumper.class);

    public void dump(PolarFile source, Path root) {
        Path destination = Paths.get(root.toString(), source.getPath());

        logger.trace("Creating " + destination);

        if (source.isDirectory()) {
            mkdir(destination);
            source.listFiles().forEach((child) -> dump(child, root));
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

    private void copy(Path destination, PolarFile source) {
        try {
            PolarFileInputStream is = new PolarFileInputStream(source);
            Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
