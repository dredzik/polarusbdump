package pl.niekoniecznie.polar.service;

import pl.niekoniecznie.polar.usb.USBDevice;
import pl.niekoniecznie.polar.usb.USBPacket;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by ak on 05.04.15.
 */
public class PolarService implements Closeable {

    private static PolarService instance;
    private USBDevice device;

    public static PolarService getInstance() throws IOException {
        if (instance == null) {
            instance = new PolarService();
        }

        return instance;
    }

    private PolarService() throws IOException {
        device = new USBDevice();
    }

    public PolarResponse doRequest(PolarRequest request) throws IOException {
        device.write(request.getPacket());

        PolarResponse response = new PolarResponse();

        while (true) {
            USBPacket packet = device.read();
            response.append(packet);

            if (packet.isBoolean0() ^ packet.isBoolean1()) {
                device.write(response.getPacket());
            } else {
                break;
            }
        }

        return response;
    }

    public void close() throws IOException {
        device.close();
    }
}
