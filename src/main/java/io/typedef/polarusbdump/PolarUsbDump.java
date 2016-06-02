/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Adam Kuczynski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.typedef.polarusbdump;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import io.typedef.polar.io.PolarFileSystem;
import io.typedef.polar.io.PolarService;
import io.typedef.polar.stream.PolarStream;
import io.typedef.polarusbdump.downloader.DirectoryDownloader;
import io.typedef.polarusbdump.downloader.DirectoryFilter;
import io.typedef.polarusbdump.downloader.FileDownloader;
import io.typedef.polarusbdump.downloader.FileFilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PolarUsbDump {

    private final static int POLAR_VENDOR_ID = 0x0da4;
    private final static int POLAR_PRODUCT_ID = 0x0008;

    public static void main(String[] args) throws IOException {
        ClassPathLibraryLoader.loadNativeHIDLibrary();
        System.out.println("[+] polarusbdump started");

        HIDDevice hid = null;

        try {
            hid = HIDManager.getInstance().openById(POLAR_VENDOR_ID, POLAR_PRODUCT_ID, null);
            System.out.println("[+] found " + hid.getProductString() + ":" + hid.getSerialNumberString());

            PolarService service = new PolarService(hid);
            PolarFileSystem filesystem = new PolarFileSystem(service);

            Path target = Paths.get(System.getProperty("user.home"), ".polar/backup/", hid.getSerialNumberString());
            System.out.println("[+] dumping into " + target);

            if (!Files.exists(target)) {
                Files.createDirectories(target);
            }

            long count = PolarStream.stream(filesystem)
                .filter(new DirectoryFilter(target))
                .filter(new FileFilter(target))
                .peek(new DirectoryDownloader(target))
                .peek(new FileDownloader(target, filesystem))
                .count();

            System.out.println("[+] " + count + " entries dumped");
        } finally {
            if (hid != null) {
                hid.close();
            }
        }

        System.exit(0);
    }
}
