package pl.niekoniecznie.p2e;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import pl.niekoniecznie.p2e.workflow.ConvertCommand;
import pl.niekoniecznie.p2e.workflow.DownloadCommand;
import pl.niekoniecznie.p2e.workflow.ListCommand;
import pl.niekoniecznie.p2e.workflow.ParseCommand;
import pl.niekoniecznie.polar.io.PolarDevice;
import pl.niekoniecznie.polar.io.PolarFileSystem;
import pl.niekoniecznie.polar.model.Session;
import pl.niekoniecznie.polar.model.SessionFile;
import pl.niekoniecznie.polar.io.PolarService;

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

        new ListCommand(filesystem).execute().forEach(directory -> {
            Map<SessionFile, InputStream> files = new DownloadCommand(filesystem, directory).execute();
            Session session = new ParseCommand(files).execute();
            new ConvertCommand(session).execute();
        });

        hid.close();
        System.exit(0);
    }
}
