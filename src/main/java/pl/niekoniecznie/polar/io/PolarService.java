package pl.niekoniecznie.polar.io;

import com.codeminders.hidapi.HIDDevice;

import java.io.IOException;

public class PolarService {

    private final HIDDevice device;

    public PolarService(final HIDDevice device) {
        this.device = device;
    }

    public void write(byte[] message) throws IOException {
        byte[] buffer = new byte[PolarPacket.BUFFER_LENGTH];
        PolarPacket packet = new PolarPacket(buffer);

        packet.setType(0x01);
        packet.setMore(false);
        packet.setData(message);

        device.write(buffer);
    }

    public PolarResponse recv() throws IOException {
        PolarResponse response = new PolarResponse();

        byte[] buffer = new byte[PolarPacket.BUFFER_LENGTH];
        PolarPacket packet = new PolarPacket(buffer);

        while (true) {
            device.read(buffer);
            response.append(packet.getData());

            if (!packet.hasMore()) {
                break;
            }

            packet.setType(0x01);
            packet.setSize(1);

            device.write(buffer);
        }

        return response;
    }
}
