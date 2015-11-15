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
        if (source.endsWith("/")) {
            dumpDirectory(root, source);
        } else {
            dumpFile(root, source);
        }
    }

    private void dumpDirectory(Path root, String source) {
        Path destination = Paths.get(root.toString(), source);

        try {
            Files.createDirectories(destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fs.list(source).forEach((child) -> {
            if (child.endsWith("/")) {
                dumpDirectory(root, child);
            } else {
                dumpFile(root, child);
            }
        });
    }

    private void dumpFile(Path root, String source) {
        Path destination = Paths.get(root.toString(), source);

        try {
            PolarFileInputStream is = new PolarFileInputStream(source);
            Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
