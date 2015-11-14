package pl.niekoniecznie.polar.service;

import pl.niekoniecznie.polar.usb.USBDevice;
import pl.niekoniecznie.polar.usb.USBPacket;

public class PolarService {

    private static PolarService instance;
    private USBDevice device;

    public static PolarService getInstance() {
        if (instance == null) {
            instance = new PolarService();
        }

        return instance;
    }

    private PolarService() {
        device = new USBDevice();
    }

    public PolarResponse doRequest(PolarRequest request) {
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
}
