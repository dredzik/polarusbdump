package pl.niekoniecznie.p2e.workflow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.polar.model.PolarModel;
import pl.niekoniecznie.polar.model.Session;
import pl.niekoniecznie.polar.model.SessionFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ParseCommand implements Command<Session> {

    private final static Logger logger = LogManager.getLogger(ParseCommand.class);

    private final Map<SessionFile, InputStream> files;

    public ParseCommand(Map<SessionFile, InputStream> files) {
        this.files = files;
    }

    @Override
    public Session execute() {
        try {
            Session result = new Session();

            logger.trace("PhysicalData");
            result.setPerson(PolarModel.PhysicalInformation.parseFrom(files.get(SessionFile.PHYSICAL)));

            logger.trace("SessionData");
            result.setData(PolarModel.Session.parseFrom(files.get(SessionFile.SESSION)));

            logger.trace("ExerciseData");
            result.setExercise(PolarModel.Exercise.parseFrom(files.get(SessionFile.EXCERCISE)));

            logger.trace("RouteData");
            result.setRoute(PolarModel.Route.parseFrom(files.get(SessionFile.ROUTE)));

            logger.trace("LapData");
            result.setLaps(PolarModel.LapInformation.parseFrom(files.get(SessionFile.LAP)));

            logger.trace("LapData - automatic");
            result.setAutomaticLaps(PolarModel.LapInformation.parseFrom(files.get(SessionFile.LAP_AUTOMATIC)));

            logger.trace("SampleData");
            result.setSamples(PolarModel.Sample.parseFrom(files.get(SessionFile.SAMPLE)));

            logger.trace("StatisticData");
            result.setStatistics(PolarModel.Statistic.parseFrom(files.get(SessionFile.STATISTIC)));

            logger.trace("ZoneData");
            result.setZones(PolarModel.Zone.parseFrom(files.get(SessionFile.ZONE)));

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
