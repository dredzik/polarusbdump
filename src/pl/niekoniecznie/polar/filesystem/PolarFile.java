package pl.niekoniecznie.polar.filesystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ak on 07.04.15.
 */
public class PolarFile {

    private final PolarFileSystem fs = new PolarFileSystem();
    private final String path;

    public PolarFile(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public boolean isDirectory() {
        return fs.isDirectory(this);
    }

    public List<String> list() throws IOException {
        return fs.list(this);
    }

    public List<PolarFile> listFiles() throws IOException {
        if (!isDirectory()) {
            return null;
        }

        List<PolarFile> result = new ArrayList<>();

        for (String child : list()) {
            result.add(new PolarFile(child));
        }

        return result;
    }
}
