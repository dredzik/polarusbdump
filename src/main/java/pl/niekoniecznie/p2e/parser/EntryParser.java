package pl.niekoniecznie.p2e.parser;

import com.google.protobuf.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class EntryParser {

    private final static String REGEX = "^/Users/ak/.polar/backup/6A5B111D/U/0/(\\d{8})/E/(\\d{6})/(\\d{2})?/?([^.]*).(BPB|GZB)$";
    private final static Pattern PATTERN = Pattern.compile(REGEX);

    private final static Logger logger = LogManager.getLogger(EntryParser.class);

    public ParseResult parse(Path path) {
        try {
            return _parse(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            logger.trace("no parser found for " + path.toString());
            return null;
        }
    }

    private ParseResult _parse(Path path) throws IOException {
        String pathString = path.toString();
        Matcher matcher = PATTERN.matcher(pathString);

        if (!matcher.matches()) {
            return null;
        }

        logger.trace("parsing " + pathString);

        String date = matcher.group(1);
        String time = matcher.group(2);
        String exercise = matcher.group(3);
        String file = matcher.group(4);
        String extension = matcher.group(5);

        InputStream stream = new FileInputStream(path.toFile());

        if (extension.equals("GZB")) {
            stream = new GZIPInputStream(stream);
        }

        Parser parser = Parsers.valueOf(file).parser();
        ParseResult result = new ParseResult();

        result.sessionKey = date + time;
        result.exerciseKey = exercise;
        result.data = parser.parseFrom(stream);

        return result;
    }
}
