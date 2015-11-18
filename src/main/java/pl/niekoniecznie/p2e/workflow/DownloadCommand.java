package pl.niekoniecznie.p2e.workflow;

import com.garmin.xmlschemas.trainingcenterdatabase.v2.*;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.polar.filesystem.PolarFileSystem;
import pl.niekoniecznie.polar.model.PolarModel;
import pl.niekoniecznie.polar.model.SessionFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class DownloadCommand implements Command<Map<SessionFile, InputStream>> {

    private final static Logger logger = LogManager.getLogger(DownloadCommand.class);

    private final PolarFileSystem filesystem;
    private final String session;

    public DownloadCommand(PolarFileSystem filesystem, String session) {
        this.filesystem = filesystem;
        this.session = session;
    }

    @Override
    public Map<SessionFile, InputStream> execute() {
        Map<SessionFile, InputStream> result = new EnumMap<>(SessionFile.class);

        for (SessionFile file : SessionFile.values()) {
            String path = file.get(session);

            logger.trace("Downloading session file from " + path);

            InputStream stream = filesystem.get(path);

            try {
                if (path.endsWith(".GZB")) {
                    stream = new GZIPInputStream(stream);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            result.put(file, stream);
        }

        return result;
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
