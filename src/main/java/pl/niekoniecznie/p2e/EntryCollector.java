package pl.niekoniecznie.p2e;

import pl.niekoniecznie.p2e.parser.ParseResult;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class EntryCollector implements Collector<ParseResult, EntrySet, List<Session>> {

    @Override
    public Supplier<EntrySet> supplier() {
        return EntrySet::new;
    }

    @Override
    public BiConsumer<EntrySet, ParseResult> accumulator() {
        return (builder, item) -> builder.add(item);
    }

    @Override
    public BinaryOperator<EntrySet> combiner() {
        return (left, right) -> left;
    }

    @Override
    public Function<EntrySet, List<Session>> finisher() {
        return EntrySet::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
