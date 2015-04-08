package pl.niekoniecznie.polar.usb;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by ak on 05.04.15.
 */
public class USBDevice implements Closeable {

    private HIDDevice device;

    public USBDevice() throws IOException {
        ClassPathLibraryLoader.loadNativeHIDLibrary();

        device = HIDManager.getInstance().openById(0x0da4, 0x0008, null);
    }

    public void write(USBPacket packet) throws IOException {
        device.write(packet.getBytes());
    }

    public USBPacket read() throws IOException {
        byte[] buffer = new byte[USBPacket.BUFFER_LENGTH];

        device.read(buffer);

        return new USBPacket(buffer);
    }

    public void close() throws IOException {
        device.close();
    }
}
