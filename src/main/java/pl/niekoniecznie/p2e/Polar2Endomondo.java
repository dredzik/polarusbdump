package pl.niekoniecznie.p2e;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import pl.niekoniecznie.p2e.workflow.DownloadCommand;
import pl.niekoniecznie.p2e.workflow.ListCommand;
import pl.niekoniecznie.polar.device.PolarDevice;
import pl.niekoniecznie.polar.filesystem.PolarFileSystem;
import pl.niekoniecznie.polar.filesystem.PolarLister;
import pl.niekoniecznie.polar.model.SessionFile;
import pl.niekoniecznie.polar.service.PolarService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class Polar2Endomondo {

    private final static int POLAR_VENDOR_ID = 0x0da4;
    private final static int POLAR_PRODUCT_ID = 0x0008;

    public static void main(String[] args) throws IOException {
        ClassPathLibraryLoader.loadNativeHIDLibrary();

        HIDDevice hid = HIDManager.getInstance().openById(POLAR_VENDOR_ID, POLAR_PRODUCT_ID, null);
        PolarDevice device = new PolarDevice(hid);
        PolarService service = new PolarService(device);
        PolarFileSystem filesystem = new PolarFileSystem(service);
        PolarLister lister = new PolarLister(filesystem);

        new ListCommand(lister).execute().forEach(session -> new Thread(() -> {
            Map<SessionFile, InputStream> files = new DownloadCommand(filesystem, session).execute();
        }).start());

//        hid.close();
    }
}
