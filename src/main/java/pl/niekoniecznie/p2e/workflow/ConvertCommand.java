package pl.niekoniecznie.p2e.workflow;

import com.garmin.xmlschemas.trainingcenterdatabase.v2.*;
import pl.niekoniecznie.polar.model.Model.*;
import pl.niekoniecznie.polar.model.Session;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class ConvertCommand implements Command<Void> {

    private final Session session;

    public ConvertCommand(Session session) {
        this.session = session;
    }

    @Override
    public Void execute() {
        try {
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            ObjectFactory factory = new ObjectFactory();

            ZonedDateTime sessionStart = convertDateTime(session.getData().getStart());
            ZonedDateTime sessionEnd = convertDateTime(session.getData().getEnd());

            List<ZonedDateTime> lapStartDates = new ArrayList<>();

            lapStartDates.add(sessionStart);

            session.getAutomaticLaps().getLapList().forEach(data -> {
                Duration split = convertDuration(data.getHeader().getSplit());
                lapStartDates.add(sessionStart.plus(split));
            });

            lapStartDates.add(sessionEnd);


            ZonedDateTime lastLapStart = lapStartDates.get(lapStartDates.size() - 2);
            double lastLapDuration = convertDuration(session.getExercise().getDuration()).toMillis() / 1000.;
            double lastLapDistance = session.getExercise().getDistance();
            int lastLapCalories = session.getExercise().getCalories() / (lapStartDates.size() - 1);

            ActivityT activity = factory.createActivityT();
            activity.setSport(SportT.RUNNING);
            activity.setId(datatypeFactory.newXMLGregorianCalendar(GregorianCalendar.from(sessionStart)));

            for (int i = 0; i < session.getAutomaticLaps().getLapCount(); i++) {
                Lap data = session.getAutomaticLaps().getLap(i);


                ZonedDateTime lapStart = lapStartDates.get(i);
                double lapDuration = convertDuration(data.getHeader().getDuration()).toMillis() / 1000.;
                double lapDistance = data.getHeader().getDistance();
                double lapSpeedMax = data.getStatistic().getSpeed().getMaximum();
                int lapCalories = session.getExercise().getCalories() / (lapStartDates.size() - 1);

                boolean lapHasHeartrate = data.getStatistic().hasHeartrate();
                short lapHeartrateMax = lapHasHeartrate ? (short) data.getStatistic().getHeartrate().getMaximum() : 0;
                short lapHeartrateAvg = lapHasHeartrate ? (short) data.getStatistic().getHeartrate().getAverage() : 0;


                ActivityLapT lap = factory.createActivityLapT();
                lap.getTrack().add(factory.createTrackT());
                lap.setStartTime(datatypeFactory.newXMLGregorianCalendar(GregorianCalendar.from(lapStart)));
                lap.setTotalTimeSeconds(lapDuration);
                lap.setDistanceMeters(lapDistance);
                lap.setMaximumSpeed(lapSpeedMax);
                lap.setCalories(lapCalories);

                if (lapHasHeartrate) {
                    lap.setMaximumHeartRateBpm(factory.createHeartRateInBeatsPerMinuteT());
                    lap.getMaximumHeartRateBpm().setValue(lapHeartrateMax);

                    lap.setAverageHeartRateBpm(factory.createHeartRateInBeatsPerMinuteT());
                    lap.getAverageHeartRateBpm().setValue(lapHeartrateAvg);
                }

                activity.getLap().add(lap);


                lastLapDuration -= lapDuration;
                lastLapDistance -= lapDistance;
            }

            ActivityLapT lastLap = factory.createActivityLapT();
            lastLap.getTrack().add(factory.createTrackT());
            lastLap.setStartTime(datatypeFactory.newXMLGregorianCalendar(GregorianCalendar.from(lastLapStart)));
            lastLap.setTotalTimeSeconds(lastLapDuration);
            lastLap.setDistanceMeters(lastLapDistance);
            lastLap.setCalories(lastLapCalories);

            activity.getLap().add(lastLap);


            int currentLap = 0;

            for (int i = 0; i < session.getRoute().getDurationCount(); i++) {
                Duration pointDuration = Duration.ofMillis(session.getRoute().getDuration(i));
                ZonedDateTime pointTime = sessionStart.plus(pointDuration);
                double pointAltitude = session.getSamples().getAltitude(i);
                double pointLatitude = session.getRoute().getLatitude(i);
                double pointLongitude = session.getRoute().getLongitude(i);

                boolean pointHasHeartrate = session.getSamples().getHeartrateCount() > 0;
                short pointHeartrate = pointHasHeartrate ? (short) session.getSamples().getHeartrate(i) : 0;


                TrackpointT point = factory.createTrackpointT();
                point.setPosition(factory.createPositionT());
                point.setAltitudeMeters(pointAltitude);
                point.getPosition().setLatitudeDegrees(pointLatitude);
                point.getPosition().setLongitudeDegrees(pointLongitude);
                point.setTime(datatypeFactory.newXMLGregorianCalendar(GregorianCalendar.from(pointTime)));

                if (pointHasHeartrate) {
                    point.setHeartRateBpm(factory.createHeartRateInBeatsPerMinuteT());
                    point.getHeartRateBpm().setValue(pointHeartrate);
                }


                if (pointTime.compareTo(lapStartDates.get(currentLap + 1)) >= 0) {
                    currentLap++;
                }

                activity.getLap().get(currentLap).getTrack().get(0).getTrackpoint().add(point);
            }


            TrainingCenterDatabaseT tcx = factory.createTrainingCenterDatabaseT();
            tcx.setActivities(factory.createActivityListT());
            tcx.getActivities().getActivity().add(activity);

            Path output = Files.createFile(Paths.get("/Users/ak/Downloads/polar_data/", sessionStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".tcx"));

            JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(factory.createTrainingCenterDatabase(tcx), new FileOutputStream(output.toString()));

            return null;
        } catch (JAXBException | DatatypeConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ZonedDateTime convertDateTime(DateTime dt) {
        Date date = dt.getDate();
        Time time = dt.getTime();

        LocalDate ldate = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
        LocalTime ltime = LocalTime.of(time.getHour(), time.getMinute(), time.getSecond()).plus(time.getMilisecond(), ChronoUnit.MILLIS);

        return ZonedDateTime.of(ldate, ltime, ZoneId.systemDefault());
    }

    private Duration convertDuration(Time t) {
        return Duration.ofHours(t.getHour())
                .plusMinutes(t.getMinute())
                .plusSeconds(t.getSecond())
                .plusMillis(t.getMilisecond());
    }
}
