package pl.niekoniecznie.p2e;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.niekoniecznie.p2e.downloader.DirectoryDownloader;
import pl.niekoniecznie.p2e.downloader.DirectoryFilter;
import pl.niekoniecznie.p2e.downloader.EntryComparator;
import pl.niekoniecznie.p2e.mapper.StreamMapper;
import pl.niekoniecznie.p2e.mapper.LocalMapper;
import pl.niekoniecznie.p2e.mapper.RemoteMapper;
import pl.niekoniecznie.p2e.downloader.FileDownloader;
import pl.niekoniecznie.p2e.downloader.FileFilter;
import pl.niekoniecznie.polar.io.PolarFileSystem;
import pl.niekoniecznie.polar.io.PolarService;
import pl.niekoniecznie.polar.stream.PolarStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class Polar2Endomondo {

    private final static int POLAR_VENDOR_ID = 0x0da4;
    private final static int POLAR_PRODUCT_ID = 0x0008;

    private final static Logger logger = LogManager.getLogger(Polar2Endomondo.class);

    public static void main(String[] args) throws IOException {
        boolean debug = true;
        Stream<String> files;

        if (debug) {
            files = Files.walk(Paths.get(System.getProperty("user.home"), ".polar/backup/"))
                .map(Path::toString);
        } else {
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

            files = PolarStream.stream(filesystem)
                .sorted(new EntryComparator())
                .filter(new DirectoryFilter(backupDirectory))
                .filter(new FileFilter(backupDirectory))
                .peek(new DirectoryDownloader(backupDirectory))
                .peek(new FileDownloader(backupDirectory, filesystem))
                .map(new RemoteMapper(backupDirectory));

            hid.close();
        }

        long a = files
            .map(new LocalMapper())
            .map(new StreamMapper())
            .filter(Objects::nonNull)
            .peek(x -> logger.trace(x.getKey()))
            .count();

        System.out.println(a);

        System.exit(0);
    }
}
