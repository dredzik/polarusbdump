package pl.niekoniecznie.polar.filesystem;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by ak on 18.04.15.
 */
public class PolarDataDumper {

    private final static String USER_DIRECTORY_PREFIX = "/U/0/";
    private final Path destination;

    public PolarDataDumper(final Path destination) {
        this.destination = destination;
    }

    public void dump() throws IOException {
        dumpDirectory(new PolarFile(USER_DIRECTORY_PREFIX));
    }

    private void dumpDirectory(final PolarFile directory) throws IOException {
        String input = directory.getPath();
        String output = outputPath(input);

        System.out.println("Entering " + input);

        try {
            Files.createDirectory(Paths.get(output));
        } catch (FileAlreadyExistsException e) {

        }

        for (PolarFile child : directory.listFiles()) {
            if (child.isDirectory()) {
                dumpDirectory(child);
            } else {
                dumpFile(child);
            }
        }
    }

    private void dumpFile(final PolarFile file) throws IOException {
        String input = file.getPath();
        String output = outputPath(input);

        System.out.println("Dumping " + input + " into " + output);

        PolarFileInputStream is = new PolarFileInputStream(file);
        Files.copy(is, Paths.get(output), StandardCopyOption.REPLACE_EXISTING);
    }

    private String outputPath(String input) {
        String result = destination.toString();

        result += input.substring(USER_DIRECTORY_PREFIX.length() - 1);

        return result;
    }
}
