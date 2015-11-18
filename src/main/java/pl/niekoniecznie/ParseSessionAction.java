package pl.niekoniecznie;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import com.garmin.xmlschemas.trainingcenterdatabase.v2.*;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.polar.device.PolarDevice;
import pl.niekoniecznie.polar.filesystem.PolarFileSystem;
import pl.niekoniecznie.polar.filesystem.PolarLister;
import pl.niekoniecznie.polar.model.PolarModel;
import pl.niekoniecznie.polar.service.PolarService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.IOException;
import java.math.BigInteger;
import java.util.GregorianCalendar;
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
        lister.list(POLAR_USER_DIRECTORY, POLAR_SESSION_REGEX).forEach(x -> {
            String source = x.replaceFirst(POLAR_SESSION_REGEX, "$1/");
            new Thread(() -> parse(source)).start();
        });
    }

    private void parse(String source) {
        logger.trace(source + ": Parsing...");

        try {
            logger.trace(source + ": Parsing physical...");
            PolarModel.PhysicalInformation physical = PolarModel.PhysicalInformation.parseFrom(filesystem.get(source + "PHYSDATA.BPB"));
            logger.trace(source + ": Parsing session...");
            PolarModel.Session session = PolarModel.Session.parseFrom(filesystem.get(source + "TSESS.BPB"));
            logger.trace(source + ": Parsing exercise...");
            PolarModel.Exercise exercise = PolarModel.Exercise.parseFrom(filesystem.get(source + "00/BASE.BPB"));
            logger.trace(source + ": Parsing route...");
            PolarModel.Route route = PolarModel.Route.parseFrom(new GZIPInputStream(filesystem.get(source + "00/ROUTE.GZB")));
            logger.trace(source + ": Parsing laps...");
            PolarModel.LapInfo laps = PolarModel.LapInfo.parseFrom(filesystem.get(source + "00/LAPS.BPB"));
            logger.trace(source + ": Parsing alaps...");
            PolarModel.LapInfo alaps = PolarModel.LapInfo.parseFrom(filesystem.get(source + "00/ALAPS.BPB"));
            logger.trace(source + ": Parsing sample...");
            PolarModel.Sample sample = PolarModel.Sample.parseFrom(new GZIPInputStream(filesystem.get(source + "00/SAMPLES.GZB")));
            logger.trace(source + ": Parsing statistic...");
            PolarModel.Statistic statistic = PolarModel.Statistic.parseFrom(filesystem.get(source + "00/STATS.BPB"));
            logger.trace(source + ": Parsing zone...");
            PolarModel.Zone zone = PolarModel.Zone.parseFrom(filesystem.get(source + "00/ZONES.BPB"));

            logger.trace(source + ": Creating TCX...");
            ObjectFactory factory = new ObjectFactory();

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(session.getStart().getDate().getYear(), session.getStart().getDate().getMonth(), session.getStart().getDate().getDay(), session.getStart().getTime().getHour(), session.getStart().getTime().getMinute(), session.getStart().getTime().getSecond());
            calendar.set(calendar.MILLISECOND, session.getStart().getTime().getMilisecond());

            TrainingCenterDatabaseT tcx = factory.createTrainingCenterDatabaseT();

            ActivityListT activities = factory.createActivityListT();
            tcx.setActivities(activities);

            ActivityT activity = factory.createActivityT();
            activities.getActivity().add(activity);

            activity.setSport(SportT.RUNNING);
            activity.setId(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));

            ActivityLapT lap = factory.createActivityLapT();
            lap.setStartTime(new XMLGregorianCalendarImpl());

            JAXBContext c = JAXBContext.newInstance(ObjectFactory.class);
            Marshaller m = c.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(factory.createTrainingCenterDatabase(tcx), System.out);

            logger.trace(source + ": TCX created.");
        } catch (DatatypeConfigurationException | JAXBException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
