package pl.niekoniecznie.p2e.collector;

import java.nio.file.Path;
import java.util.EnumMap;

public class ExerciseMap extends EnumMap<ExerciseFile, Path> {

    public ExerciseMap() {
        super(ExerciseFile.class);
    }
}
