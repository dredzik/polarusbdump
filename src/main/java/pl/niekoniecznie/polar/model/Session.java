package pl.niekoniecznie.polar.model;

import pl.niekoniecznie.polar.model.PolarModel.*;

public class Session {

    private PhysicalData person;
    private SessionData data;
    private ExerciseData exercise;
    private RouteData route;
    private LapData laps;
    private LapData automaticLaps;
    private SampleData samples;
    private StatisticData statistics;
    private ZoneData zones;

    public PhysicalData getPerson() {
        return person;
    }

    public void setPerson(PhysicalData person) {
        this.person = person;
    }

    public SessionData getData() {
        return data;
    }

    public void setData(SessionData data) {
        this.data = data;
    }

    public ExerciseData getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseData exercise) {
        this.exercise = exercise;
    }

    public RouteData getRoute() {
        return route;
    }

    public void setRoute(RouteData route) {
        this.route = route;
    }

    public LapData getLaps() {
        return laps;
    }

    public void setLaps(LapData laps) {
        this.laps = laps;
    }

    public LapData getAutomaticLaps() {
        return automaticLaps;
    }

    public void setAutomaticLaps(LapData automaticLaps) {
        this.automaticLaps = automaticLaps;
    }

    public SampleData getSamples() {
        return samples;
    }

    public void setSamples(SampleData samples) {
        this.samples = samples;
    }

    public StatisticData getStatistics() {
        return statistics;
    }

    public void setStatistics(StatisticData statistics) {
        this.statistics = statistics;
    }

    public ZoneData getZones() {
        return zones;
    }

    public void setZones(ZoneData zones) {
        this.zones = zones;
    }
}
