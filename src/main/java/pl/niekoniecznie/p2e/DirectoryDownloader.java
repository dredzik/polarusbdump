package pl.niekoniecznie.p2e;

import pl.niekoniecznie.polar.io.PolarEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class DirectoryDownloader implements Consumer<PolarEntry> {

    private final static String DOWNLOAD_DIRECTORY = "/Users/ak/Downloads/polar/";

    @Override
    public void accept(PolarEntry polarEntry) {
        if (polarEntry.isFile()) {
            return;
        }

        Path destination = Paths.get(DOWNLOAD_DIRECTORY, polarEntry.getPath());

        try {
            Files.createDirectory(destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
