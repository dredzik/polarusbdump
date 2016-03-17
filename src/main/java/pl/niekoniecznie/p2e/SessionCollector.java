package pl.niekoniecznie.p2e;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;

public class SessionCollector implements Collector<Path, Map<String, List<Path>>, Map<String, List<Path>>> {

    private final static Pattern PATTERN = Pattern.compile("(\\d{8})/E/(\\d{6})/(\\d{2})?/?([^.]*).(BPB|GZB)$");

    @Override
    public Supplier<Map<String, List<Path>>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<String, List<Path>>, Path> accumulator() {
        return (map, item) -> {
            String path = item.toString();
            Matcher matcher = PATTERN.matcher(path);

            if (!matcher.find()) {
                return;
            }

            String key = matcher.group(1) + matcher.group(2);

            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }

            map.get(key).add(item);
        };
    }

    @Override
    public BinaryOperator<Map<String, List<Path>>> combiner() {
        return null;
    }

    @Override
    public Function<Map<String, List<Path>>, Map<String, List<Path>>> finisher() {
        return (map) -> map;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
