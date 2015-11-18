package pl.niekoniecznie.p2e.workflow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.polar.filesystem.PolarLister;

import java.util.ArrayList;
import java.util.List;

public class ListCommand implements Command<List<String>> {

    private final static String POLAR_USER_DIRECTORY = "/U/0/";
    private final static String POLAR_SESSION_REGEX = "(/U/0/(\\d{8,})/E/(\\d{6,}))/00/SAMPLES.GZB";

    private final static Logger logger = LogManager.getLogger(ListCommand.class);

    private final PolarLister lister;

    public ListCommand(PolarLister lister) {
        this.lister = lister;
    }

    @Override
    public List<String> execute() {
        List<String> result = new ArrayList<>();

        lister.list(POLAR_USER_DIRECTORY, POLAR_SESSION_REGEX).forEach(sample -> {
            String session = sample.replaceFirst(POLAR_SESSION_REGEX, "$1/");

            logger.trace("Found session in " + session);

            result.add(session);
        });

        return result;
    }
}
