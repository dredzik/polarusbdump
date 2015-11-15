package pl.niekoniecznie;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import pl.niekoniecznie.polar.device.PolarDevice;
import pl.niekoniecznie.polar.filesystem.PolarDownloader;
import pl.niekoniecznie.polar.filesystem.PolarFileSystem;
import pl.niekoniecznie.polar.filesystem.PolarLister;
import pl.niekoniecznie.polar.service.PolarService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DownloadSessionAction implements Runnable {

    private final static int POLAR_VENDOR_ID = 0x0da4;
    private final static int POLAR_PRODUCT_ID = 0x0008;
    private final static String POLAR_USER_DIRECTORY = "/U/0/";
    private final static String POLAR_SESSION_REGEX = "(/U/0/(\\d{8,})/E/(\\d{6,}))/00/SAMPLES.GZB";

    private final PolarLister lister;
    private final PolarDownloader downloader;
    private final Path directory;

    public DownloadSessionAction(final Path directory) throws IOException {
        ClassPathLibraryLoader.loadNativeHIDLibrary();

        HIDDevice hid = HIDManager.getInstance().openById(POLAR_VENDOR_ID, POLAR_PRODUCT_ID, null);
        PolarDevice device = new PolarDevice(hid);
        PolarService service = new PolarService(device);
        PolarFileSystem filesystem = new PolarFileSystem(service);

        lister = new PolarLister(filesystem);
        downloader = new PolarDownloader(filesystem);

        this.directory = directory;
    }

    @Override
    public void run() {
        lister.list(POLAR_USER_DIRECTORY, POLAR_SESSION_REGEX).forEach(this::save);
    }

    private void save(String samples) {
        String source = samples.replaceFirst(POLAR_SESSION_REGEX, "$1/");
        Path destination = directory.resolve(samples.replaceFirst(POLAR_SESSION_REGEX, "$2_$3/"));

        try {
            Files.createDirectories(destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        downloader.download(source, destination);
    }
}
