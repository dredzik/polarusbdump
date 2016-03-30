package io.typedef.cothroime.converter;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Parser;
import io.typedef.polar.model.Model.ExerciseData;
import io.typedef.polar.model.Model.LapSetData;
import io.typedef.polar.model.Model.PhysicalData;
import io.typedef.polar.model.Model.RouteData;
import io.typedef.polar.model.Model.SampleData;
import io.typedef.polar.model.Model.SessionData;
import io.typedef.polar.model.Model.StatisticData;
import io.typedef.polar.model.Model.ZoneData;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Converters {

    private static final Map<Class<? extends GeneratedMessage>, Converter> converters = new HashMap<>();

    static {
        converters.put(SessionData.class, new SessionConverter());
    }

    public static Converter of(GeneratedMessage object) {
        for (Class key : converters.keySet()) {
            if (key.isInstance(object)) {
                return converters.get(key);
            }
        }

        return null;
    }
}
