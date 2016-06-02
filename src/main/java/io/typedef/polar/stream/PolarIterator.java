package io.typedef.polar.stream;

import io.typedef.polar.io.PolarEntry;
import io.typedef.polar.io.PolarFileSystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PolarIterator implements Iterator<PolarEntry> {

    private final PolarFileSystem filesystem;
    private final List<PolarEntry> queue = new ArrayList<>();

    public PolarIterator(PolarFileSystem filesystem) {
        this.filesystem = filesystem;
        queue.add(new PolarEntry("/"));
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public PolarEntry next() {
        PolarEntry next = queue.remove(0);

        if (next.isDirectory()) {
            queue.addAll(0, filesystem.list(next));
        }

        return next;
    }
}
