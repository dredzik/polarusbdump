package pl.niekoniecznie.p2e;

import pl.niekoniecznie.polar.io.PolarEntry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

public class DirectoryFilter implements Predicate<PolarEntry> {

    private final Path backupDirectory;

    public DirectoryFilter(Path backupDirectory) {
        this.backupDirectory = backupDirectory;
    }

    @Override
    public boolean test(PolarEntry entry) {
        if (entry.isFile()) {
            return true;
        }

        Path directory = Paths.get(backupDirectory.toString(), entry.getPath());

        return Files.notExists(directory);
    }
}
