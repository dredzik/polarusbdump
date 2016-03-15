package pl.niekoniecznie.polar.stream;

import pl.niekoniecznie.polar.io.PolarEntry;
import pl.niekoniecznie.polar.io.PolarFileSystem;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PolarStream {

    public static Stream<PolarEntry> stream(PolarFileSystem filesystem) {
        PolarIterator iterator = new PolarIterator(filesystem);
        Spliterator<PolarEntry> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED);

        return StreamSupport.stream(spliterator, false);
    }
}
