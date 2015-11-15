package pl.niekoniecznie;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import pl.niekoniecznie.polar.filesystem.PolarDownloader;
import pl.niekoniecznie.polar.filesystem.PolarFileSystem;
import pl.niekoniecznie.polar.service.PolarService;
import pl.niekoniecznie.polar.device.PolarDevice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        ClassPathLibraryLoader.loadNativeHIDLibrary();

        int vendorId = 0x0da4;
        int productId = 0x0008;
        String source = "/U/0/";
        Path destination = Files.createTempDirectory("polar");

        HIDDevice hid = HIDManager.getInstance().openById(vendorId, productId, null);
        PolarDevice device = new PolarDevice(hid);
        PolarService service = new PolarService(device);
        PolarFileSystem filesystem = new PolarFileSystem(service);
        PolarDownloader dumper = new PolarDownloader(filesystem);

        dumper.download(destination, source);

        hid.close();
        System.exit(0);
    }
}
