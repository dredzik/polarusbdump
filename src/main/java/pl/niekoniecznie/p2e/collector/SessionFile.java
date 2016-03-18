package pl.niekoniecznie.p2e.collector;

public enum SessionFile {

    SESSION("TSESS"),
    PHYSICAL("PHYSDATA");

    private final String fileId;

    SessionFile(String fileId) {
        this.fileId = fileId;
    }

    public static SessionFile of(String fileId) {
        for (SessionFile value : values()) {
            if (value.fileId.equals(fileId)) {
                return value;
            }
        }

        throw new IllegalArgumentException("Not a session file " + fileId);
    }

    public static boolean is(String fileId) {
        for (SessionFile value : values()) {
            if (value.fileId.equals(fileId)) {
                return true;
            }
        }

        return false;
    }
}
