package pl.niekoniecznie.polar.filesystem;

import pl.niekoniecznie.polar.service.PolarRequest;
import pl.niekoniecznie.polar.service.PolarResponse;
import pl.niekoniecznie.polar.service.PolarService;

import java.io.InputStream;

public class PolarFileInputStream extends InputStream {

    private final PolarFile file;
    private Object[] buffer;
    private int pointer;

    public PolarFileInputStream(final PolarFile file) {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("Cannot read directory as a file");
        }

        this.file = file;
    }

    @Override
    public int read() {
        if (buffer == null) {
            load();
        }

        if (pointer >= buffer.length) {
            return -1;
        }

        return (byte) buffer[pointer++] & 0xff;
    }

    @Override
    public int available() {
        if (buffer == null) {
            return 0;
        }

        return buffer.length - pointer;
    }

    private void load() {
        String path = file.getPath();

        PolarRequest request = new PolarRequest(path);
        PolarResponse response = PolarService.getInstance().doRequest(request);

        buffer = response.getBody().toArray();
    }
}
