package pl.niekoniecznie.p2e.parser;

import com.google.protobuf.Parser;
import pl.niekoniecznie.polar.model.Model.ExerciseData;
import pl.niekoniecznie.polar.model.Model.LapData;
import pl.niekoniecznie.polar.model.Model.PhysicalData;
import pl.niekoniecznie.polar.model.Model.RouteData;
import pl.niekoniecznie.polar.model.Model.SampleData;
import pl.niekoniecznie.polar.model.Model.SessionData;
import pl.niekoniecznie.polar.model.Model.StatisticData;
import pl.niekoniecznie.polar.model.Model.ZoneData;

enum Parsers {
    ALAPS(LapData.PARSER),
    BASE(ExerciseData.PARSER),
    LAPS(LapData.PARSER),
    PHYSDATA(PhysicalData.PARSER),
    ROUTE(RouteData.PARSER),
    SAMPLES(SampleData.PARSER),
    STATS(StatisticData.PARSER),
    TSESS(SessionData.PARSER),
    ZONES(ZoneData.PARSER);

    private final Parser parser;

    Parsers(Parser parser) {
        this.parser = parser;
    }

    public Parser parser() {
        return parser;
    }
}
