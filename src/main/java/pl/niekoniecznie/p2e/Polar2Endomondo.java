package pl.niekoniecznie.p2e;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.p2e.download.DirectoryDownloader;
import pl.niekoniecznie.p2e.download.DirectoryFilter;
import pl.niekoniecznie.p2e.download.EntryComparator;
import pl.niekoniecznie.p2e.download.EntryMapper;
import pl.niekoniecznie.p2e.download.FileDownloader;
import pl.niekoniecznie.p2e.download.FileFilter;
import pl.niekoniecznie.p2e.parse.EntryParser;
import pl.niekoniecznie.polar.io.PolarFileSystem;
import pl.niekoniecznie.polar.io.PolarService;
import pl.niekoniecznie.polar.stream.PolarStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Polar2Endomondo {

    private final static int POLAR_VENDOR_ID = 0x0da4;
    private final static int POLAR_PRODUCT_ID = 0x0008;

    private final static Logger logger = LogManager.getLogger(Polar2Endomondo.class);

    public static void main(String[] args) throws IOException {
        ClassPathLibraryLoader.loadNativeHIDLibrary();

        HIDDevice hid = HIDManager.getInstance().openById(POLAR_VENDOR_ID, POLAR_PRODUCT_ID, null);
        logger.trace("device " + hid.getProductString() + " " + hid.getSerialNumberString() + " found");

        PolarService service = new PolarService(hid);
        PolarFileSystem filesystem = new PolarFileSystem(service);

        Path backupDirectory = Paths.get(System.getProperty("user.home"), ".polar/backup/", hid.getSerialNumberString());
        logger.trace("backup directory set to " + backupDirectory);

        if (!Files.exists(backupDirectory)) {
            Files.createDirectories(backupDirectory);
        }

        long downloaded = PolarStream.stream(filesystem)
            .sorted(new EntryComparator())
            .filter(new DirectoryFilter(backupDirectory))
            .filter(new FileFilter(backupDirectory))
            .peek(new DirectoryDownloader(backupDirectory))
            .peek(new FileDownloader(backupDirectory, filesystem))
            .map(new EntryMapper(backupDirectory)::map)
            .map(new EntryParser()::parse)
            .count();

        if (downloaded > 0) {
            System.out.println("Downloaded " + downloaded + " files and directories");
        } else {
            System.out.println("All files are up-to-date");
        }

        hid.close();
        System.exit(0);
    }
}
