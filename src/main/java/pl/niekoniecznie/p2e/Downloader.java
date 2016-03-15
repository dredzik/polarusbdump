package pl.niekoniecznie.p2e;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.polar.io.PolarFileSystem;
import pl.niekoniecznie.polar.io.PolarService;
import pl.niekoniecznie.polar.model.Model.Date;
import pl.niekoniecznie.polar.model.Model.DateTime;
import pl.niekoniecznie.polar.model.Model.ListMessage.ListEntry;
import pl.niekoniecznie.polar.model.Model.Time;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.*;

public class Downloader {

//    private final static String DOWNLOAD_DIRECTORY = "/Users/ak/Downloads/polar/";
//    private final static int POLAR_VENDOR_ID = 0x0da4;
//    private final static int POLAR_PRODUCT_ID = 0x0008;
//    private final static Logger logger = LogManager.getLogger(Downloader.class);
//
//    public void run() throws IOException {
//        ClassPathLibraryLoader.loadNativeHIDLibrary();
//
//        HIDDevice hid = HIDManager.getInstance().openById(POLAR_VENDOR_ID, POLAR_PRODUCT_ID, null);
//        PolarService service = new PolarService(hid);
//        PolarFileSystem filesystem = new PolarFileSystem(service);
//
//        downloadDirectory("/", filesystem);
//
//        hid.close();
//    }
//
//    private void downloadDirectory(String directory, PolarFileSystem filesystem) throws IOException {
//        logger.trace(directory);
//
//        Path destination = Paths.get(DOWNLOAD_DIRECTORY, directory);
//
//        if (Files.notExists(destination)) {
//            Files.createDirectory(destination);
//        }
//
//        for (ListEntry entry : filesystem.list(directory)) {
//            String item = directory + entry.getPath();
//
//            if (item.endsWith("/")) {
//                downloadDirectory(item, filesystem);
//            } else {
//                downloadFile(entry, directory, filesystem);
//            }
//        }
//    }
//
//    private void downloadFile(ListEntry file, String prefix, PolarFileSystem filesystem) throws IOException {
//        InputStream input = filesystem.get(prefix + file.getPath());
//        Path output = Paths.get(DOWNLOAD_DIRECTORY, prefix, file.getPath());
//
//        logger.trace("File " + output);
//
//        DateTime dt = file.getModified();
//        Date d = dt.getDate();
//        Time t = dt.getTime();
//        ZonedDateTime remote = ZonedDateTime.of(d.getYear(), d.getMonth(), d.getDay(), t.getHour(), t.getMinute(), t.getSecond(), 0, ZoneId.systemDefault());
//
//        if (Files.exists(output)) {
//            logger.trace("File already exists, checking for updates...");
//            ZonedDateTime local = ZonedDateTime.ofInstant(Files.getLastModifiedTime(output).toInstant(), ZoneId.systemDefault());
//
//            if (local.equals(remote)) {
//                logger.trace("No update needed.");
//                return;
//            } else {
//                logger.trace("Redownloading file.");
//                Files.delete(output);
//            }
//        }
//
//        Files.copy(input, output);
//        Files.setLastModifiedTime(output, FileTime.from(remote.toInstant()));
//    }
}
