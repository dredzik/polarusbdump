package pl.niekoniecznie.p2e.workflow;

import com.garmin.xmlschemas.trainingcenterdatabase.v2.*;
import pl.niekoniecznie.polar.model.PolarModel.Date;
import pl.niekoniecznie.polar.model.PolarModel.Time;
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
import java.util.GregorianCalendar;

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

            Date tmp1 = session.getData().getStart().getDate();
            Time tmp2 = session.getData().getStart().getTime();
            LocalDate date = LocalDate.of(tmp1.getYear(), tmp1.getMonth(), tmp1.getDay());
            LocalTime time = LocalTime.of(tmp2.getHour(), tmp2.getMinute(), tmp2.getSecond()).plus(tmp2.getMilisecond(), ChronoUnit.MILLIS);
            ZonedDateTime sdt = ZonedDateTime.of(date, time, ZoneId.systemDefault());
            GregorianCalendar start = GregorianCalendar.from(sdt);

            ActivityT activity = factory.createActivityT();
            activity.setSport(SportT.RUNNING);
            activity.setId(datatypeFactory.newXMLGregorianCalendar(start));

            session.getAutomaticLaps().getLapList().forEach(data -> {
                ActivityLapT lap = factory.createActivityLapT();

                Time durationData = data.getHeader().getDuration();
                Duration duration = Duration.ofHours(durationData.getHour())
                        .plusMinutes(durationData.getMinute())
                        .plusSeconds(durationData.getSecond())
                        .plusMillis(durationData.getMilisecond());

                Time splitData = data.getHeader().getSplit();
                Duration split = Duration.ofHours(splitData.getHour())
                        .plusMinutes(splitData.getMinute())
                        .plusSeconds(splitData.getSecond())
                        .plusMillis(splitData.getMilisecond());

                Duration lstart = split.minus(duration);
                ZonedDateTime ldt = sdt.plus(lstart);

                lap.setStartTime(datatypeFactory.newXMLGregorianCalendar(GregorianCalendar.from(ldt)));
                lap.setTotalTimeSeconds(duration.toMillis() / 1000.);
                lap.setDistanceMeters(data.getHeader().getDistance());
                lap.setMaximumSpeed((double) data.getStatistic().getSpeed().getMaximum());
                lap.setCalories(session.getExercise().getCalories() / session.getAutomaticLaps().getLapCount());

                if (data.getStatistic().hasHeartrate()) {
                    lap.setMaximumHeartRateBpm(factory.createHeartRateInBeatsPerMinuteT());
                    lap.getMaximumHeartRateBpm().setValue((short) data.getStatistic().getHeartrate().getMaximum());

                    lap.setAverageHeartRateBpm(factory.createHeartRateInBeatsPerMinuteT());
                    lap.getAverageHeartRateBpm().setValue((short) data.getStatistic().getHeartrate().getAverage());
                }

                lap.getTrack().add(factory.createTrackT());

                for (int i = 0; i < session.getRoute().getDurationCount(); i++) {
                    Duration pduration = Duration.ofMillis(session.getRoute().getDuration(i));

                    if (pduration.compareTo(lstart) < 0 || pduration.compareTo(split) >= 0) {
                        continue;
                    }

                    TrackpointT point = factory.createTrackpointT();

                    if (session.getSamples().getHeartrateCount() > 0) {
                        point.setHeartRateBpm(factory.createHeartRateInBeatsPerMinuteT());
                        point.getHeartRateBpm().setValue((short) session.getSamples().getHeartrate(i));
                    }

                    point.setTime(datatypeFactory.newXMLGregorianCalendar(GregorianCalendar.from(sdt.plus(pduration))));
                    point.setAltitudeMeters((double) session.getSamples().getAltitude(i));
                    point.setPosition(factory.createPositionT());
                    point.getPosition().setLatitudeDegrees(session.getRoute().getLatitude(i));
                    point.getPosition().setLongitudeDegrees(session.getRoute().getLongitude(i));

                    lap.getTrack().get(0).getTrackpoint().add(point);
                }

                activity.getLap().add(lap);
            });

            TrainingCenterDatabaseT tcx = factory.createTrainingCenterDatabaseT();
            tcx.setActivities(factory.createActivityListT());
            tcx.getActivities().getActivity().add(activity);

            Path output = Files.createFile(Paths.get("/Users/ak/Downloads/polar_data/", sdt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".tcx"));

            JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(factory.createTrainingCenterDatabase(tcx), new FileOutputStream(output.toString()));

            return null;
        } catch (JAXBException | DatatypeConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
