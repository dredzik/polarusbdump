package pl.niekoniecznie.p2e;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.p2e.collector.SessionCollector;
import pl.niekoniecznie.p2e.collector.SessionMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Polar2Endomondo {

    private final static int POLAR_VENDOR_ID = 0x0da4;
    private final static int POLAR_PRODUCT_ID = 0x0008;

    private final static Logger logger = LogManager.getLogger(Polar2Endomondo.class);

    public static void main(String[] args) throws IOException {
//        ClassPathLibraryLoader.loadNativeHIDLibrary();
//
//        HIDDevice hid = HIDManager.getInstance().openById(POLAR_VENDOR_ID, POLAR_PRODUCT_ID, null);
//        logger.trace("device " + hid.getProductString() + " " + hid.getSerialNumberString() + " found");
//
//        PolarService service = new PolarService(hid);
//        PolarFileSystem filesystem = new PolarFileSystem(service);
//
//        Path backupDirectory = Paths.get(System.getProperty("user.home"), ".polar/backup/", hid.getSerialNumberString());
//        logger.trace("backup directory set to " + backupDirectory);
//
//        if (!Files.exists(backupDirectory)) {
//            Files.createDirectories(backupDirectory);
//        }

        List<Path> files =
            Files.walk(Paths.get(System.getProperty("user.home"), ".polar/backup/")).collect(Collectors.toList());

//            PolarStream.stream(filesystem)
//            .sorted(new EntryComparator())
//            .filter(new DirectoryFilter(backupDirectory))
//            .filter(new FileFilter(backupDirectory))
//            .peek(new DirectoryDownloader(backupDirectory))
//            .peek(new FileDownloader(backupDirectory, filesystem))
//            .map(new EntryMapper(backupDirectory))
//            .collect(Collectors.toList());

        if (files.size() > 0) {
            System.out.println("Downloaded " + files.size() + " file(s)");
        } else {
            System.out.println("All files are up-to-date");
        }

//        hid.close();


        List<Session> sessions =
//        List<SessionMap> sessions =
            files.stream()
            .collect(new SessionCollector())
            .parallelStream()
            .map(new EntryParser())
            .collect(Collectors.toList());

        System.out.println(sessions.size());

        System.exit(0);
    }
}
