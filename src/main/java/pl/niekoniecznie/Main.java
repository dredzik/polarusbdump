package pl.niekoniecznie;

import pl.niekoniecznie.polar.model.PolarModel;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new ParseSessionAction().run();

        System.exit(0);
    }
}
