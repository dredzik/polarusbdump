package pl.niekoniecznie;

import pl.niekoniecznie.polar.filesystem.PolarDataDumper;

import java.io.IOException;
import java.nio.file.Files;

public class Main {

    private static String root;

    public static void main(String[] args) throws IOException {
        PolarDataDumper dumper = new PolarDataDumper(Files.createTempDirectory("polar"));

        for (String session : dumper.dump()) {
            System.out.println(session);
        }

        System.exit(0);
    }
}
