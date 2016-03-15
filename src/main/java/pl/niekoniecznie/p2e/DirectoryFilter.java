package pl.niekoniecznie.p2e;

import pl.niekoniecznie.polar.io.PolarEntry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

public class DirectoryFilter implements Predicate<PolarEntry> {

    private final static String DOWNLOAD_DIRECTORY = "/Users/ak/Downloads/polar/";

    @Override
    public boolean test(PolarEntry entry) {
        if (entry.isFile()) {
            return false;
        }

        Path directory = Paths.get(DOWNLOAD_DIRECTORY, entry.getPath());

        return Files.notExists(directory);
    }
}
