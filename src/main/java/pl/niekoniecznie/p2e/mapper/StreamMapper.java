package pl.niekoniecznie.p2e.mapper;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import pl.niekoniecznie.polar.model.Model.SessionData;

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
        }

        if (Objects.nonNull(value)) {
            return new Pair<>(key, value);
        }

        return null;
    }
}
