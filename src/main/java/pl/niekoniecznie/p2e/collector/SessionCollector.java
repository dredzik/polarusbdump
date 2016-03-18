package pl.niekoniecznie.p2e.collector;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class SessionCollector implements Collector<Path, SessionAccumulator, List<SessionMap>> {

    @Override
    public Supplier<SessionAccumulator> supplier() {
        return SessionAccumulator::new;
    }

    @Override
    public BiConsumer<SessionAccumulator, Path> accumulator() {
        return SessionAccumulator::accumulate;
    }

    @Override
    public BinaryOperator<SessionAccumulator> combiner() {
        return null;
    }

    @Override
    public Function<SessionAccumulator, List<SessionMap>> finisher() {
        return SessionAccumulator::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
