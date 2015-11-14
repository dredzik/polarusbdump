package pl.niekoniecznie.polar.usb;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;

import java.io.IOException;

public class USBDevice {

    private HIDDevice device;

    public USBDevice() {
        ClassPathLibraryLoader.loadNativeHIDLibrary();

        try {
            device = HIDManager.getInstance().openById(0x0da4, 0x0008, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(USBPacket packet) {
        try {
            device.write(packet.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public USBPacket read() {
        byte[] buffer = new byte[USBPacket.BUFFER_LENGTH];

        try {
            device.read(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new USBPacket(buffer);
    }
}
