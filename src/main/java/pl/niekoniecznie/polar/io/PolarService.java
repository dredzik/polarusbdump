package pl.niekoniecznie.polar.io;

public class PolarService {

    private final PolarDevice device;

    public PolarService(final PolarDevice device) {
        this.device = device;
    }

    public void send(PolarRequest request) {
        device.write(request.getPacket().getBytes());
    }

    public PolarResponse recv() {
        PolarResponse response = new PolarResponse();

        byte[] buffer = new byte[PolarPacket.BUFFER_LENGTH];
        PolarPacket packet = new PolarPacket(buffer);

        do {
            device.read(buffer);
            response.append(packet);

            if (packet.hasMore()) {
                device.write(response.getPacket().getBytes());
            }
        } while (packet.hasMore());

        return response;
    }
}
