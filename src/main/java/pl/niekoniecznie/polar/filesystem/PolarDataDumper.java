package pl.niekoniecznie.polar.filesystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ak on 18.04.15.
 */
public class PolarDataDumper {

    private final static String USER_DIRECTORY_PREFIX = "/U/0/";
    private final static String DATE_DIRECTORY_REGEXP = "/U/0/\\d{8,}/";
    private final static String TIME_DIRECTORY_REGEXP = "/U/0/(\\d{8,})/E/(\\d{6,})/";
    private final static String SAMPLES_FILE_REGEXP = "/U/0/\\d{8,}/E/\\d{6,}/SAMPLES.GZB";

    private final static Logger logger = LogManager.getLogger(PolarDataDumper.class);

    private final Path destination;

    public PolarDataDumper(final Path destination) {
        this.destination = destination;
    }

    public List<String> dump() throws IOException {
        List<String> result = new ArrayList<>();

        dumpUserDirectory(result, new PolarFile(USER_DIRECTORY_PREFIX));

        return result;
    }

    private void dumpUserDirectory(final List<String> result, final PolarFile directory) throws IOException {
        for (PolarFile child : directory.listFiles()) {
            if (!child.isDirectory()) {
                continue;
            }

            String path = child.getPath();

            if (!path.matches(DATE_DIRECTORY_REGEXP)) {
                continue;
            }

            dumpDateDirectory(result, child);
        }
    }

    private void dumpDateDirectory(List<String> result, final PolarFile directory) throws IOException {
        PolarFile eDirectory = new PolarFile(directory.getPath() + "E/");

        for (PolarFile child : eDirectory.listFiles()) {
            if (!child.isDirectory()) {
                continue;
            }

            String path = child.getPath();

            if (!path.matches(TIME_DIRECTORY_REGEXP)) {
                continue;
            }

            dumpTimeDirectory(result, child);
        }
    }

    private void dumpTimeDirectory(List<String> result, final PolarFile directory) throws IOException {
        logger.trace("Entering " + directory.getPath());

        boolean dump = false;

        for (PolarFile child : directory.listFiles()) {
            String path = child.getPath();

            if (path.matches(SAMPLES_FILE_REGEXP)) {
                dump = true;
                break;
            }
        }

        if (!dump) {
            logger.trace("No samples file, skipping.");
            return;
        }

        String input = directory.getPath();
        String output = destination.toString();
        output += input.replaceFirst(TIME_DIRECTORY_REGEXP, "/$1$2/");

        try {
            Files.createDirectory(Paths.get(output));
        } catch (FileAlreadyExistsException e) {

        }

        result.add(output);

        for (PolarFile child : directory.listFiles()) {
            if (child.isDirectory()) {
                continue;
            }

            logger.trace("Dumping " + child.getPath());

            String tmp = output + child.getPath().replaceFirst(TIME_DIRECTORY_REGEXP, "");

            PolarFileInputStream is = new PolarFileInputStream(child);
            Files.copy(is, Paths.get(tmp), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
