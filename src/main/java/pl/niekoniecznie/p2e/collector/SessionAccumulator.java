package pl.niekoniecznie.p2e.collector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SessionAccumulator {

    private final static Pattern PATTERN = Pattern.compile("(\\d{8})/E/(\\d{6})/(\\d{2})?/?([^.]*).(BPB|GZB)$");
    private final static Logger logger = LogManager.getLogger(SessionAccumulator.class);
    private final Map<String, SessionMap> map = new HashMap<>();

    public static void accumulate(SessionAccumulator accumulator, Path item) {
        String path = item.toString();
        Matcher matcher = PATTERN.matcher(path);

        if (!matcher.find()) {
            return;
        }

        String sessionId = matcher.group(1) + matcher.group(2);
        String exerciseId = matcher.group(3);
        String fileId = matcher.group(4);

        if (!SessionFile.is(fileId) && !ExerciseFile.is(fileId)) {
            return;
        }

        if (!accumulator.map.containsKey(sessionId)) {
            accumulator.map.put(sessionId, new SessionMap());
        }

        if (SessionFile.is(fileId)) {
            accumulator.map.get(sessionId).put(SessionFile.of(fileId), item);
            logger.trace("session file added " + path);
        }

        if (ExerciseFile.is(fileId)) {
            accumulator.map.get(sessionId).getExercise(exerciseId).put(ExerciseFile.of(fileId), item);
            logger.trace("exercise file added " + path);
        }
    }

    public static List<SessionMap> build(SessionAccumulator accumulator) {
        return new ArrayList<>(accumulator.map.values());
    }
}
