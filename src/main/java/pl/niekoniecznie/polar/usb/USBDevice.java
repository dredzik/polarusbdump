package pl.niekoniecznie.polar.usb;

import com.codeminders.hidapi.HIDDevice;

import java.io.IOException;

public class USBDevice {

    private final HIDDevice device;

    public USBDevice(final HIDDevice device) {
        this.device = device;
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
