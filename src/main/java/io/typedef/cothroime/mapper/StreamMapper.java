package io.typedef.cothroime.mapper;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Function;

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
        Parser parser = Parsers.matching(key);

        if (Objects.isNull(parser)) {
            return null;
        }

        GeneratedMessage value = (GeneratedMessage) parser.parseFrom(entry.getValue());

        return new Pair<>(key, value);
    }
}
