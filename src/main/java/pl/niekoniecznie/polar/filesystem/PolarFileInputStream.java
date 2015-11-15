package pl.niekoniecznie.polar.filesystem;

import pl.niekoniecznie.polar.service.PolarRequest;
import pl.niekoniecznie.polar.service.PolarResponse;
import pl.niekoniecznie.polar.service.PolarService;

import java.io.InputStream;

public class PolarFileInputStream extends InputStream {

    private final String path;
    private Object[] buffer;
    private int pointer;

    public PolarFileInputStream(final String path) {
        this.path = path;
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
        PolarRequest request = new PolarRequest(path);
        PolarResponse response = PolarService.getInstance().doRequest(request);

        buffer = response.getBody().toArray();
    }
}
