package pl.niekoniecznie.polar.model;

public enum SessionFile {

    PHYSICAL("PHYSDATA.BPB"),
    SESSION("TSESS.BPB"),
    EXCERCISE("00/BASE.BPB"),
    ROUTE("00/ROUTE.GZB"),
    LAP("00/LAPS.BPB"),
    LAP_AUTOMATIC("00/ALAPS.BPB"),
    SAMPLE("00/SAMPLES.GZB"),
    STATISTIC("00/STATS.BPB"),
    ZONE("00/ZONES.BPB");

    private final String path;

    SessionFile(String path) {
        this.path = path;
    }

    public String get(final String root) {
        String result = root;

        if (!result.endsWith("/")) {
            result += "/";
        }

        return result + path;
    }
}
