package pl.niekoniecznie.p2e.collector;

public enum ExerciseFile {

    AUTOMATIC_LAP("ALAPS"),
    EXERCISE("BASE"),
    LAP("LAPS"),
    ROUTE("ROUTE"),
    SAMPLE("SAMPLES"),
    STATISTIC("STATS"),
    ZONE("ZONES");

    private final String fileId;

    ExerciseFile(String fileId) {
        this.fileId = fileId;
    }

    public static ExerciseFile of(String fileId) {
        for (ExerciseFile value : values()) {
            if (value.fileId.equals(fileId)) {
                return value;
            }
        }

        throw new IllegalArgumentException("Not a exercise file " + fileId);
    }

    public static boolean is(String fileId) {
        for (ExerciseFile value : values()) {
            if (value.fileId.equals(fileId)) {
                return true;
            }
        }

        return false;
    }
}
