package pl.niekoniecznie.p2e;

import java.io.IOException;

public class Polar2Endomondo {

    public static void main(String[] args) throws IOException {
        Downloader downloader = new Downloader();

        downloader.run();

        System.exit(0);
    }
}
