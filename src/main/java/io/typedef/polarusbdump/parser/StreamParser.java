package io.typedef.polarusbdump.parser;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import io.typedef.polarusbdump.mapper.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Function;

public class StreamParser implements Function<Pair<String, InputStream>, Pair<String, GeneratedMessage>> {

    private final static Logger logger = LogManager.getLogger(StreamParser.class);

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
        InputStream value = entry.getValue();

        Parser parser = Parsers.matching(key);

        if (Objects.isNull(parser)) {
            return null;
        }

        logger.trace("parsing " + key);
        GeneratedMessage result = (GeneratedMessage) parser.parseFrom(value);

        return new Pair<>(key, result);
    }
}
