package pl.niekoniecznie.polar.filesystem;

import pl.niekoniecznie.polar.service.PolarRequest;
import pl.niekoniecznie.polar.service.PolarResponse;
import pl.niekoniecznie.polar.service.PolarService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ak on 07.04.15.
 */
public class PolarFileSystem {
    public boolean isDirectory(final PolarFile file) {
        return file.getPath().endsWith("/");
    }


    public List<String> list(final PolarFile file) throws IOException {
        if (!isDirectory(file)) {
            return null;
        }

        String path = file.getPath();

        PolarRequest request = new PolarRequest(path);
        PolarResponse response = PolarService.getInstance().doRequest(request);

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
