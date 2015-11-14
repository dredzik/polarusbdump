package pl.niekoniecznie;

import pl.niekoniecznie.polar.filesystem.PolarDataDumper;
import pl.niekoniecznie.polar.filesystem.PolarFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        Path destination = Files.createTempDirectory("polar");

        PolarDataDumper dumper = new PolarDataDumper();

        dumper.dump(new PolarFile("/U/0/"), destination);

        System.exit(0);
    }
}
