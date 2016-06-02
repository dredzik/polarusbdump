package io.typedef.cothroime.converter;

import com.google.protobuf.GeneratedMessage;
import io.typedef.cothroime.mapper.Pair;
import io.typedef.cothroime.model.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

public class DataConverter implements Function<Pair<String, GeneratedMessage>, Entity> {

    private final static Logger logger = LogManager.getLogger(DataConverter.class);

    @Override
    public Entity apply(Pair<String, GeneratedMessage> entry) {
        String key = entry.getKey();
        GeneratedMessage value = entry.getValue();
        Converter converter = Converters.of(value);

        if (converter == null) {
            return null;
        }

        logger.trace("converting " + key);
        Entity result = converter.convert(value);

        result.setPath(key);

        return result;
    }
}
