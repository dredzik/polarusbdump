package pl.niekoniecznie.polar.io;

import com.codeminders.hidapi.HIDDevice;

import java.io.IOException;

public class PolarService {

    private final HIDDevice device;

    public PolarService(final HIDDevice device) {
        this.device = device;
    }

    public void send(PolarRequest request) throws IOException {
        device.write(request.getPacket().getBytes());
    }

    public PolarResponse recv() throws IOException {
        PolarResponse response = new PolarResponse();

        byte[] buffer = new byte[PolarPacket.BUFFER_LENGTH];
        PolarPacket packet = new PolarPacket(buffer);

        while (true) {
            device.read(buffer);
            response.append(packet);

            if (!packet.hasMore()) {
                break;
            }

            device.write(response.getPacket().getBytes());
        }

        return response;
    }
}
