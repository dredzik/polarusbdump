package pl.niekoniecznie.p2e.download;

import pl.niekoniecznie.polar.io.PolarEntry;

import java.nio.file.Path;
import java.nio.file.Paths;

public class EntryMapper {

    private final Path backupDirectory;

    public EntryMapper(Path backupDirectory) {
        this.backupDirectory = backupDirectory;
    }

    public Path map(PolarEntry entry) {
        return Paths.get(backupDirectory.toString(), entry.getPath());
    }
}
