package pl.niekoniecznie;

import pl.niekoniecznie.polar.filesystem.PolarDownloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        Path destination = Files.createTempDirectory("polar");

        PolarDownloader dumper = new PolarDownloader();

        dumper.download(destination, "/U/0/");

        System.exit(0);
    }
}
