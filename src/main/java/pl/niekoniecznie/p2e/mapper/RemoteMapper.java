package pl.niekoniecznie.p2e.mapper;

import pl.niekoniecznie.polar.io.PolarEntry;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

public class RemoteMapper implements Function<PolarEntry, String> {

    private final Path backupDirectory;

    public RemoteMapper(Path backupDirectory) {
        this.backupDirectory = backupDirectory;
    }

    @Override
    public String apply(PolarEntry entry) {
        return backupDirectory.toString() + entry.getPath();
    }
}
