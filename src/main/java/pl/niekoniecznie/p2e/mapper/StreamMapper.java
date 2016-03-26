package pl.niekoniecznie.p2e.mapper;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import pl.niekoniecznie.polar.model.Model.ExerciseData;
import pl.niekoniecznie.polar.model.Model.LapSetData;
import pl.niekoniecznie.polar.model.Model.PhysicalData;
import pl.niekoniecznie.polar.model.Model.RouteData;
import pl.niekoniecznie.polar.model.Model.SampleData;
import pl.niekoniecznie.polar.model.Model.SessionData;
import pl.niekoniecznie.polar.model.Model.StatisticData;
import pl.niekoniecznie.polar.model.Model.ZoneData;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

public class StreamMapper implements Function<Pair<String, InputStream>, Pair<String, GeneratedMessage>> {

    @Override
    public Pair<String, GeneratedMessage> apply(Pair<String, InputStream> entry) {
        try {
            return _apply(entry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Pair<String, GeneratedMessage> _apply(Pair<String, InputStream> entry) throws InvalidProtocolBufferException {
        String key = entry.getKey();
        GeneratedMessage value = null;

        if (Pattern.compile("/\\d{8}/E/\\d{6}/TSESS.BPB$").matcher(key).find()) {
            value = SessionData.PARSER.parseFrom(entry.getValue());
        } else if (Pattern.compile("/\\d{8}/E/\\d{6}/PHYSDATA.BPB$").matcher(key).find()) {
            value = PhysicalData.PARSER.parseFrom(entry.getValue());
        } else if (Pattern.compile("/\\d{8}/E/\\d{6}/\\d{2}/BASE.BPB$").matcher(key).find()) {
            value = ExerciseData.PARSER.parseFrom(entry.getValue());
        } else if (Pattern.compile("/\\d{8}/E/\\d{6}/\\d{2}/ALAPS.BPB$").matcher(key).find()) {
            value = LapSetData.PARSER.parseFrom(entry.getValue());
        } else if (Pattern.compile("/\\d{8}/E/\\d{6}/\\d{2}/LAPS.BPB$").matcher(key).find()) {
            value = LapSetData.PARSER.parseFrom(entry.getValue());
        } else if (Pattern.compile("/\\d{8}/E/\\d{6}/\\d{2}/ROUTE.GZB$").matcher(key).find()) {
            value = RouteData.PARSER.parseFrom(entry.getValue());
        } else if (Pattern.compile("/\\d{8}/E/\\d{6}/\\d{2}/SAMPLES.GZB$").matcher(key).find()) {
            value = SampleData.PARSER.parseFrom(entry.getValue());
        } else if (Pattern.compile("/\\d{8}/E/\\d{6}/\\d{2}/STATS.BPB$").matcher(key).find()) {
            value = StatisticData.PARSER.parseFrom(entry.getValue());
        } else if (Pattern.compile("/\\d{8}/E/\\d{6}/\\d{2}/ZONES.BPB$").matcher(key).find()) {
            value = ZoneData.PARSER.parseFrom(entry.getValue());
        }

        if (Objects.nonNull(value)) {
            return new Pair<>(key, value);
        }

        return null;
    }
}
