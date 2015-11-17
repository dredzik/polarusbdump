package pl.niekoniecznie;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.polar.device.PolarDevice;
import pl.niekoniecznie.polar.filesystem.PolarFileSystem;
import pl.niekoniecznie.polar.filesystem.PolarLister;
import pl.niekoniecznie.polar.model.PolarModel;
import pl.niekoniecznie.polar.service.PolarService;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class ParseSessionAction implements Runnable {

    private final static int POLAR_VENDOR_ID = 0x0da4;
    private final static int POLAR_PRODUCT_ID = 0x0008;
    private final static String POLAR_USER_DIRECTORY = "/U/0/";
    private final static String POLAR_SESSION_REGEX = "(/U/0/(\\d{8,})/E/(\\d{6,}))/00/SAMPLES.GZB";

    private final static Logger logger = LogManager.getLogger(ParseSessionAction.class);

    private final PolarFileSystem filesystem;
    private final PolarLister lister;

    public ParseSessionAction() throws IOException {
        ClassPathLibraryLoader.loadNativeHIDLibrary();

        HIDDevice hid = HIDManager.getInstance().openById(POLAR_VENDOR_ID, POLAR_PRODUCT_ID, null);
        PolarDevice device = new PolarDevice(hid);
        PolarService service = new PolarService(device);

        filesystem = new PolarFileSystem(service);
        lister = new PolarLister(filesystem);
    }

    @Override
    public void run() {
        lister.list(POLAR_USER_DIRECTORY, POLAR_SESSION_REGEX).forEach(x -> parse(x.replaceFirst(POLAR_SESSION_REGEX, "$1/")));
    }

    private void parse(String source) {
        logger.trace("Parsing " + source);

        try {
            PolarModel.PhysicalInformation physical = PolarModel.PhysicalInformation.parseFrom(filesystem.get(source + "PHYSDATA.BPB"));
            PolarModel.Session session = PolarModel.Session.parseFrom(filesystem.get(source + "TSESS.BPB"));
            PolarModel.Exercise exercise = PolarModel.Exercise.parseFrom(filesystem.get(source + "00/BASE.BPB"));
            PolarModel.Route route = PolarModel.Route.parseFrom(new GZIPInputStream(filesystem.get(source + "00/ROUTE.GZB")));
            PolarModel.LapInfo laps = PolarModel.LapInfo.parseFrom(filesystem.get(source + "00/LAPS.BPB"));
            PolarModel.LapInfo alaps = PolarModel.LapInfo.parseFrom(filesystem.get(source + "00/ALAPS.BPB"));
            PolarModel.Sample sample = PolarModel.Sample.parseFrom(new GZIPInputStream(filesystem.get(source + "00/SAMPLES.GZB")));
            PolarModel.Statistic statistic = PolarModel.Statistic.parseFrom(filesystem.get(source + "00/STATS.BPB"));
            PolarModel.Zone zone = PolarModel.Zone.parseFrom(filesystem.get(source + "00/ZONES.BPB"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
