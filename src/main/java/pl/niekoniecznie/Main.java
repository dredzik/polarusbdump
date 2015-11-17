package pl.niekoniecznie;

import pl.niekoniecznie.polar.model.PolarModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        Path directory = Files.createTempDirectory("polar");

        new DownloadSessionAction(directory).run();

        FileInputStream physdata = new FileInputStream("/Users/ak/Downloads/polar_data/20151109_212633/PHYSDATA.BPB");
        PolarModel.PhysicalInformation physical = PolarModel.PhysicalInformation.parseFrom(physdata);

        FileInputStream tsessdata = new FileInputStream("/Users/ak/Downloads/polar_data//20151109_212633/TSESS.BPB");
        PolarModel.Session session = PolarModel.Session.parseFrom(tsessdata);

        FileInputStream basedata = new FileInputStream("/Users/ak/Downloads/polar_data//20151109_212633/00/BASE.BPB");
        PolarModel.Exercise exercise = PolarModel.Exercise.parseFrom(basedata);

        FileInputStream routedata = new FileInputStream("/Users/ak/Downloads/polar_data//20151109_212633/00/ROUTE.BPB");
        PolarModel.Route route = PolarModel.Route.parseFrom(routedata);

        FileInputStream lapsdata = new FileInputStream("/Users/ak/Downloads/polar_data//20151109_212633/00/LAPS.BPB");
        PolarModel.LapInfo laps = PolarModel.LapInfo.parseFrom(lapsdata);

        FileInputStream alapsdata = new FileInputStream("/Users/ak/Downloads/polar_data//20151109_212633/00/ALAPS.BPB");
        PolarModel.LapInfo alaps = PolarModel.LapInfo.parseFrom(alapsdata);

        FileInputStream samplesdata = new FileInputStream("/Users/ak/Downloads/polar_data//20151109_212633/00/SAMPLES.BPB");
        PolarModel.Sample samples = PolarModel.Sample.parseFrom(samplesdata);

        FileInputStream statsdata = new FileInputStream("/Users/ak/Downloads/polar_data//20151109_212633/00/STATS.BPB");
        PolarModel.Statistic statistic = PolarModel.Statistic.parseFrom(statsdata);

        FileInputStream zonedata = new FileInputStream("/Users/ak/Downloads/polar_data//20151109_212633/00/ZONES.BPB");
        PolarModel.Zone zone = PolarModel.Zone.parseFrom(zonedata);

        System.out.println(zone);

        System.exit(0);
    }
}
