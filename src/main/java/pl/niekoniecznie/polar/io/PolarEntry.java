package pl.niekoniecznie.polar.io;

import pl.niekoniecznie.polar.model.Model.ListMessage.ListEntry;

public class PolarEntry {

    private final String path;
    private final int size;

    public PolarEntry(String path) {
        this.path = path;
        this.size = 0;
    }

    PolarEntry(String rootPath, ListEntry entry) {
        this.path = rootPath + entry.getPath();
        this.size = entry.getSize();
    }

    public boolean isDirectory() {
        return path.endsWith("/");
    }

    public String getPath() {
        return path;
    }

    public int getSize() {
        return size;
    }
}
