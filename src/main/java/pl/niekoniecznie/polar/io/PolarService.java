package pl.niekoniecznie.polar.io;

public class PolarService {

    private final PolarDevice device;

    public PolarService(final PolarDevice device) {
        this.device = device;
    }

    public void send(PolarRequest request) {
        device.write(request.getPacket());
    }

    public PolarResponse recv() {
        PolarResponse response = new PolarResponse();

        PolarPacket packet;

        do {
            packet = device.read();
            response.append(packet);

            if (packet.hasMore()) {
                device.write(response.getPacket());
            }
        } while (packet.hasMore());

        return response;
    }
}
