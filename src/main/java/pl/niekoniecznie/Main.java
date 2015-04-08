package pl.niekoniecznie;

import pl.niekoniecznie.polar.filesystem.PolarFile;
import pl.niekoniecznie.polar.filesystem.PolarFileInputStream;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {

    private static String root = "/Users/ak/Downloads/DUMP";

    public static void main(String[] args) throws IOException {
        dumpDirectory(new PolarFile("/"));
        System.out.println("done");

        System.exit(0);
    }

    private static void dumpDirectory(final PolarFile directory) throws IOException {
        String path = directory.getPath();
        String destination = root + path;

        System.out.println("Entering " + path);

        try {
            Files.createDirectory(Paths.get(destination));
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

    private static void dumpFile(final PolarFile file) throws IOException {
        String path = file.getPath();
        String destination = root + path;

        System.out.println("Dumping " + path + " into " + destination);

        PolarFileInputStream is = new PolarFileInputStream(file);
        Files.copy(is, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
    }
}
