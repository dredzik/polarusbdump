package pl.niekoniecznie.polar.io;

import com.codeminders.hidapi.HIDDevice;

import java.io.IOException;

public class PolarDevice {

    private final HIDDevice device;

    public PolarDevice(final HIDDevice device) {
        this.device = device;
    }

    public void write(PolarPacket packet) {
        try {
            device.write(packet.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PolarPacket read() {
        byte[] buffer = new byte[PolarPacket.BUFFER_LENGTH];

        try {
            device.read(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new PolarPacket(buffer);
    }
}
