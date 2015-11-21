package pl.niekoniecznie.polar.model;

public class Session {

    private PolarModel.PhysicalInformation person;
    private PolarModel.Session data;
    private PolarModel.Exercise exercise;
    private PolarModel.Route route;
    private PolarModel.LapInformation laps;
    private PolarModel.LapInformation automaticLaps;
    private PolarModel.Sample samples;
    private PolarModel.Statistic statistics;
    private PolarModel.Zone zones;

    public PolarModel.PhysicalInformation getPerson() {
        return person;
    }

    public void setPerson(PolarModel.PhysicalInformation person) {
        this.person = person;
    }

    public PolarModel.Session getData() {
        return data;
    }

    public void setData(PolarModel.Session data) {
        this.data = data;
    }

    public PolarModel.Exercise getExercise() {
        return exercise;
    }

    public void setExercise(PolarModel.Exercise exercise) {
        this.exercise = exercise;
    }

    public PolarModel.Route getRoute() {
        return route;
    }

    public void setRoute(PolarModel.Route route) {
        this.route = route;
    }

    public PolarModel.LapInformation getLaps() {
        return laps;
    }

    public void setLaps(PolarModel.LapInformation laps) {
        this.laps = laps;
    }

    public PolarModel.LapInformation getAutomaticLaps() {
        return automaticLaps;
    }

    public void setAutomaticLaps(PolarModel.LapInformation automaticLaps) {
        this.automaticLaps = automaticLaps;
    }

    public PolarModel.Sample getSamples() {
        return samples;
    }

    public void setSamples(PolarModel.Sample samples) {
        this.samples = samples;
    }

    public PolarModel.Statistic getStatistics() {
        return statistics;
    }

    public void setStatistics(PolarModel.Statistic statistics) {
        this.statistics = statistics;
    }

    public PolarModel.Zone getZones() {
        return zones;
    }

    public void setZones(PolarModel.Zone zones) {
        this.zones = zones;
    }
}
