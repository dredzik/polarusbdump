package pl.niekoniecznie.p2e.workflow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.polar.io.PolarFileSystem;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand implements Command<List<String>> {

    private final static String USER_HOME = "/U/0/";

    private final static Logger logger = LogManager.getLogger(ListCommand.class);

    private final PolarFileSystem fileSystem;

    public ListCommand(PolarFileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public List<String> execute() {
        return fileSystem.list(USER_HOME)
            .stream()
            .filter(x -> x.endsWith("/"))

            .map(fileSystem::list)
            .flatMap(Collection::stream)
            .filter(x -> x.endsWith("E/"))

            .map(fileSystem::list)
            .flatMap(Collection::stream)
            .filter(x -> x.endsWith("/"))

            .peek(logger::trace)
            .collect(Collectors.toList());
    }
}
