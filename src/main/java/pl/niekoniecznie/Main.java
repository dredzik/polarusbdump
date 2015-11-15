package pl.niekoniecznie;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        DownloadSessionAction action = new DownloadSessionAction();

        action.run();

        System.exit(0);
    }
}
