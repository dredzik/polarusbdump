package pl.niekoniecznie.p2e.collector;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionMap extends EnumMap<SessionFile, Path> {

    private Map<String, ExerciseMap> exercises = new HashMap<>();

    public SessionMap() {
        super(SessionFile.class);
    }

    public ExerciseMap getExercise(String exerciseId) {
        if (!exercises.containsKey(exerciseId)) {
            exercises.put(exerciseId, new ExerciseMap());
        }

        return exercises.get(exerciseId);
    }

    public List<ExerciseMap> getExercises() {
        return new ArrayList<>(exercises.values());
    }
}
