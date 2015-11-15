package pl.niekoniecznie.polar.filesystem;

import pl.niekoniecznie.polar.service.PolarRequest;
import pl.niekoniecznie.polar.service.PolarResponse;
import pl.niekoniecznie.polar.service.PolarService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PolarFileSystem {

    private class PolarInputStream extends InputStream {

        private final Object[] buffer;
        private int pointer;

        public PolarInputStream(Object[] buffer) {
            this.buffer = buffer;
        }

        @Override
        public int read() {
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
    }

    private final PolarService service;

    public PolarFileSystem(final PolarService service) {
        this.service = service;
    }

    public InputStream get(final String path) {
        PolarRequest request = new PolarRequest(path);
        PolarResponse response = service.doRequest(request);

        return new PolarInputStream(response.getBody().toArray());
    }

    public List<String> list(final String path) {
        PolarRequest request = new PolarRequest(path);
        PolarResponse response = service.doRequest(request);

        List<String> result = new ArrayList<>();
        List<Byte> body = response.getBody();

        for (int i = 0; i < body.size(); ) {
            if (body.get(i) != 0x0a || body.get(i + 2) != 0x0a) {
                throw new IllegalStateException("something went wrong");
            }

            int entrySize = body.get(i + 1) + 2;
            int nameSize = body.get(i + 3);

            StringBuffer sb = new StringBuffer();

            for (int j = 0; j < nameSize; j++) {
                sb.append((char) (byte) body.get(i + 4 + j));
            }

            result.add(path + sb.toString());

            i += entrySize;
        }

        return result;
    }
}
