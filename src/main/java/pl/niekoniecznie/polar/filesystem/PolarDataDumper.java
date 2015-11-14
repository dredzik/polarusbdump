package pl.niekoniecznie.polar.filesystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;

public class PolarDataDumper {

    private final static Logger logger = LogManager.getLogger(PolarDataDumper.class);

    public void dump(PolarFile source, Path root) throws IOException {
        Path destination = Paths.get(root.toString(), source.getPath());

        logger.trace("Creating " + destination);

        if (source.isDirectory()) {
            Files.createDirectories(destination);

            for (PolarFile child : source.listFiles()) {
                dump(child, root);
            }
        } else {
            PolarFileInputStream is = new PolarFileInputStream(source);
            Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
