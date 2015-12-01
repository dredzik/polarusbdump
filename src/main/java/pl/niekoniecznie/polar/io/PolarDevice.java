package pl.niekoniecznie.polar.io;

import com.codeminders.hidapi.HIDDevice;

import java.io.IOException;

public class PolarDevice {

    private final HIDDevice device;

    public PolarDevice(final HIDDevice device) {
        this.device = device;
    }

    public int write(byte[] buffer) {
        try {
            return device.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int read(byte[] buffer) {
        try {
            return device.read(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
