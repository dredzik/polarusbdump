package pl.niekoniecznie.polar.filesystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PolarLister {

    private final PolarFileSystem filesystem;

    private final static Logger logger = LogManager.getLogger(PolarDownloader.class);

    public PolarLister(final PolarFileSystem filesystem) {
        this.filesystem = filesystem;
    }

    public List<String> list(String source) {
        return list(source, null);
    }

    public List<String> list(String source, String regex) {
        List<String> result = new ArrayList<>();

        list(result, source, regex);

        return result;
    }

    private void list(List<String> result, String source, String regex) {
        if (regex == null || source.matches(regex)) {
            logger.trace("Found " + source);

            result.add(source);
        }

        if (source.endsWith("/")) {
            filesystem.list(source).forEach((x) -> list(result, x, regex));
        }
    }
}
