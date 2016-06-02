package io.typedef.polarusbdump;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import io.typedef.polar.io.PolarFileSystem;
import io.typedef.polar.io.PolarService;
import io.typedef.polar.stream.PolarStream;
import io.typedef.polarusbdump.converter.DataConverter;
import io.typedef.polarusbdump.downloader.DirectoryDownloader;
import io.typedef.polarusbdump.downloader.DirectoryFilter;
import io.typedef.polarusbdump.downloader.FileDownloader;
import io.typedef.polarusbdump.downloader.FileFilter;
import io.typedef.polarusbdump.mapper.LocalMapper;
import io.typedef.polarusbdump.mapper.RemoteMapper;
import io.typedef.polarusbdump.parser.StreamParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class PolarUsbDump {

    private final static int POLAR_VENDOR_ID = 0x0da4;
    private final static int POLAR_PRODUCT_ID = 0x0008;

    private final static Logger logger = LogManager.getLogger(PolarUsbDump.class);

    public static void main(String[] args) throws IOException {
        ClassPathLibraryLoader.loadNativeHIDLibrary();
        Stream<String> files;
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
            .filter(new DirectoryFilter(backupDirectory))
            .filter(new FileFilter(backupDirectory))
            .peek(new DirectoryDownloader(backupDirectory))
            .peek(new FileDownloader(backupDirectory, filesystem))
            .map(new RemoteMapper(backupDirectory));

        long a = files
            .map(new LocalMapper())
            .map(new StreamParser())
            .filter(Objects::nonNull)
            .map(new DataConverter())
            .filter(Objects::nonNull)
            .count();

        hid.close();
        System.exit(0);
    }
}
