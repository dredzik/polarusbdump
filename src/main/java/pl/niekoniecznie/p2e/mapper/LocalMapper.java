package pl.niekoniecznie.p2e.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

public class LocalMapper implements Function<String, Pair<String, InputStream>> {

    @Override
    public Pair<String, InputStream> apply(String entry) {
        try {
            return _apply(entry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Pair<String, InputStream> _apply(String entry) throws IOException {
        Path path = Paths.get(entry);
        InputStream stream = Files.newInputStream(path);

        if (entry.endsWith("GZB")) {
            stream = new GZIPInputStream(stream);
        }

        return new Pair<>(entry, stream);
    }
}
