package pl.niekoniecznie.p2e.workflow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.polar.filesystem.PolarFileSystem;
import pl.niekoniecznie.polar.model.SessionFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class DownloadCommand implements Command<Map<SessionFile, InputStream>> {

    private final static Logger logger = LogManager.getLogger(DownloadCommand.class);

    private final PolarFileSystem filesystem;
    private final String session;

    public DownloadCommand(PolarFileSystem filesystem, String session) {
        this.filesystem = filesystem;
        this.session = session;
    }

    @Override
    public Map<SessionFile, InputStream> execute() {
        Map<SessionFile, InputStream> result = new EnumMap<>(SessionFile.class);

        for (SessionFile file : SessionFile.values()) {
            String path = file.get(session);

            logger.trace("Downloading session file from " + path);

            InputStream stream = filesystem.get(path);

            if (path.endsWith(".GZB")) {
                try {
                    stream = new GZIPInputStream(stream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            result.put(file, stream);
        }

        return result;
    }

}
