package pl.niekoniecznie.p2e;

import pl.niekoniecznie.p2e.collector.SessionFile;
import pl.niekoniecznie.p2e.collector.SessionMap;
import pl.niekoniecznie.polar.model.Model.SessionData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

public class EntryParser implements Function<SessionMap, Session> {

    @Override
    public Session apply(SessionMap map) {
        Session.Builder builder = new Session.Builder();

        try {
            for (SessionFile file : map.keySet()) {
                Path value = map.get(file);
                InputStream stream = Files.newInputStream(value);

                if (value.toString().endsWith("GZB")) {
                    stream = new GZIPInputStream(stream);
                }


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return null;
    }
}
