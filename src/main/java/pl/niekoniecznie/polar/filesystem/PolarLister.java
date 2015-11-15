package pl.niekoniecznie.polar.filesystem;

import java.util.ArrayList;
import java.util.List;

public class PolarLister {

    private final PolarFileSystem filesystem;

    public PolarLister(final PolarFileSystem filesystem) {
        this.filesystem = filesystem;
    }

    public List<String> list(String source) {
        List<String> result = new ArrayList<>();

        list(result, source);

        return result;
    }

    private void list(List<String> result, String source) {
        result.add(source);

        if (source.endsWith("/")) {
            filesystem.list(source).forEach((x) -> list(result, x));
        }
    }
}
