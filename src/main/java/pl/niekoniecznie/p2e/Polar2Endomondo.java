package pl.niekoniecznie.p2e;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import pl.niekoniecznie.polar.io.PolarEntry;
import pl.niekoniecznie.polar.io.PolarFileSystem;
import pl.niekoniecznie.polar.io.PolarService;
import pl.niekoniecznie.polar.stream.PolarStream;

import java.io.IOException;

public class Polar2Endomondo {

    private final static int POLAR_VENDOR_ID = 0x0da4;
    private final static int POLAR_PRODUCT_ID = 0x0008;

    public static void main(String[] args) throws IOException {
        ClassPathLibraryLoader.loadNativeHIDLibrary();

        HIDDevice hid = HIDManager.getInstance().openById(POLAR_VENDOR_ID, POLAR_PRODUCT_ID, null);
        PolarService service = new PolarService(hid);
        PolarFileSystem filesystem = new PolarFileSystem(service);

        long updated = PolarStream
            .stream(filesystem)
            .filter(new DirectoryFilter())
            .filter(new FileFilter())
            .peek(new DirectoryDownloader())
            .count();

        System.out.println("Updated " + updated + " files and directories.");
        System.exit(0);
    }
}
