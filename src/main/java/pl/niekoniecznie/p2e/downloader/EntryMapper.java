package pl.niekoniecznie.p2e.downloader;

import pl.niekoniecznie.polar.io.PolarEntry;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

public class EntryMapper implements Function<PolarEntry, Path> {

    private final Path backupDirectory;

    public EntryMapper(Path backupDirectory) {
        this.backupDirectory = backupDirectory;
    }

    @Override
    public Path apply(PolarEntry entry) {
        return Paths.get(backupDirectory.toString(), entry.getPath());
    }
}
