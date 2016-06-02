package io.typedef.polarusbdump.downloader;

import io.typedef.polar.io.PolarEntry;

import java.util.Comparator;

public class EntryComparator implements Comparator<PolarEntry> {

    @Override
    public int compare(PolarEntry o1, PolarEntry o2) {
        if (o1.isDirectory() && o2.isFile()) {
            return -1;
        }

        if (o1.isFile() && o2.isDirectory()) {
            return 1;
        }

        return o1.getPath().compareTo(o2.getPath());
    }
}
