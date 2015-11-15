package pl.niekoniecznie;

import pl.niekoniecznie.polar.filesystem.PolarDownloader;
import pl.niekoniecznie.polar.filesystem.PolarFileSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        String source = "/U/0/";
        Path destination = Files.createTempDirectory("polar");

        PolarFileSystem filesystem = new PolarFileSystem();
        PolarDownloader dumper = new PolarDownloader(filesystem);

        dumper.download(destination, source);

        System.exit(0);
    }
}
