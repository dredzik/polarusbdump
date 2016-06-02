package io.typedef.polar.io;

import io.typedef.polar.model.Model.Date;
import io.typedef.polar.model.Model.ListMessage.ListEntry;
import io.typedef.polar.model.Model.Time;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class PolarEntry {

    private final Instant modified;
    private final String path;
    private final int size;

    public PolarEntry(String path) {
        this.modified = Instant.now();
        this.path = path;
        this.size = 0;
    }

    PolarEntry(String rootPath, ListEntry entry) {
        Date date = entry.getModified().getDate();
        Time time = entry.getModified().getTime();

        ZonedDateTime modified = ZonedDateTime.of(date.getYear(), date.getMonth(), date.getDay(),
            time.getHour(), time.getMinute(), time.getSecond(),
            0, ZoneId.systemDefault());

        this.modified = modified.toInstant();
        this.path = rootPath + entry.getPath();
        this.size = entry.getSize();
    }

    public boolean isDirectory() {
        return path.endsWith("/");
    }

    public boolean isFile() {
        return !isDirectory();
    }

    public Instant getModified() {
        return modified;
    }

    public String getPath() {
        return path;
    }

    public int getSize() {
        return size;
    }
}
