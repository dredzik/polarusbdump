package pl.niekoniecznie.p2e;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.polar.io.PolarEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.function.Predicate;

public class FileFilter implements Predicate<PolarEntry> {

    private final static String DOWNLOAD_DIRECTORY = "/Users/ak/Downloads/polar/";

    @Override
    public boolean test(PolarEntry entry) {
        if (entry.isDirectory()) {
            return true;
        }

        Path file = Paths.get(DOWNLOAD_DIRECTORY, entry.getPath());

        if (Files.notExists(file)) {
            return true;
        }

        Instant modified;

        try {
            modified = Files.getLastModifiedTime(file).toInstant();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!entry.getModified().equals(modified)) {
            return true;
        }

        return false;
    }
}
