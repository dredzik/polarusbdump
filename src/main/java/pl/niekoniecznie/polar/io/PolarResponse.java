package pl.niekoniecznie.polar.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolarResponse {

    private byte lastSequenceNumber;
    private byte[] bytes = new byte[0];
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

        byte[] tmp = new byte[bytes.length + data.length - 1];
        System.arraycopy(bytes, 0, tmp, 0, bytes.length);
        System.arraycopy(data, 1, tmp, bytes.length, data.length - 1);
        bytes = tmp;
    }

    public byte[] getBytes() {
        return Arrays.copyOfRange(bytes, 2, bytes.length - 1);
    }

    public List<Byte> getBody() {
        return body.subList(2, body.size() - 1);
    }
}
