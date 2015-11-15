package pl.niekoniecznie.polar.service;

import pl.niekoniecznie.polar.device.PolarPacket;

import java.util.ArrayList;
import java.util.List;

public class PolarResponse {

    private byte lastSequenceNumber;
    private List<Byte> body = new ArrayList<>();

    public PolarPacket getPacket() {
        byte[] data = new byte[1];
        data[0] = lastSequenceNumber;

        PolarPacket result0 = new PolarPacket();

        result0.setType((byte) 0x01);
        result0.setContinuation(true);
        result0.setData(data);

        return result0;
    }

    public void append(PolarPacket packet) {
        byte[] data = packet.getData();
        lastSequenceNumber = data[0];

        for (int i = 1; i < data.length; i++) {
            body.add(data[i]);
        }
    }

    public List<Byte> getBody() {
        return body.subList(2, body.size() - 1);
    }
}
