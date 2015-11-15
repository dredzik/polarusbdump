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

public class DownloadSessionAction {

    private final static int POLAR_VENDOR_ID = 0x0da4;
    private final static int POLAR_PRODUCT_ID = 0x0008;
    private final static String POLAR_USER_DIRECTORY = "/U/0/";

    public void run() throws IOException {
        ClassPathLibraryLoader.loadNativeHIDLibrary();

        Path destination = Files.createTempDirectory("polar");

        HIDDevice hid = HIDManager.getInstance().openById(POLAR_VENDOR_ID, POLAR_PRODUCT_ID, null);
        PolarDevice device = new PolarDevice(hid);
        PolarService service = new PolarService(device);
        PolarFileSystem filesystem = new PolarFileSystem(service);

        PolarLister lister = new PolarLister(filesystem);
        PolarDownloader dumper = new PolarDownloader(filesystem);

        lister.list(POLAR_USER_DIRECTORY);
        dumper.download(POLAR_USER_DIRECTORY, destination);

        hid.close();

    }
}
