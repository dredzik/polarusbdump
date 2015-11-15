package pl.niekoniecznie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        Path directory = Files.createTempDirectory("polar");

        new DownloadSessionAction(directory).run();

        System.exit(0);
    }
}
