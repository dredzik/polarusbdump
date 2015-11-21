package pl.niekoniecznie.p2e.workflow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.polar.filesystem.PolarFileSystem;
import pl.niekoniecznie.polar.model.SessionFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
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

//    private void parse(String source) {
//
//            logger.trace(source + ": Creating TCX...");
//            ObjectFactory factory = new ObjectFactory();
//
//            GregorianCalendar calendar = new GregorianCalendar();
//            calendar.set(session.getStart().getDate().getYear(), session.getStart().getDate().getMonth(), session.getStart().getDate().getDay(), session.getStart().getTime().getHour(), session.getStart().getTime().getMinute(), session.getStart().getTime().getSecond());
//            calendar.set(calendar.MILLISECOND, session.getStart().getTime().getMilisecond());
//
//            TrainingCenterDatabaseT tcx = factory.createTrainingCenterDatabaseT();
//
//            ActivityListT activities = factory.createActivityListT();
//            tcx.setActivities(activities);
//
//            ActivityT activity = factory.createActivityT();
//            activities.getActivity().add(activity);
//
//            activity.setSport(SportT.RUNNING);
//            activity.setId(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
//
//            ActivityLapT lap = factory.createActivityLapT();
//            lap.setStartTime(new XMLGregorianCalendarImpl());
//
//            JAXBContext c = JAXBContext.newInstance(ObjectFactory.class);
//            Marshaller m = c.createMarshaller();
//            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//            m.marshal(factory.createTrainingCenterDatabase(tcx), System.out);
//
//            logger.trace(source + ": TCX created.");
//    }
}
