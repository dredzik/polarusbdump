package pl.niekoniecznie.polar.io;

import java.util.Arrays;

public class PolarResponse {

    private byte[] bytes = new byte[0];

    public void append(PolarPacket packet) {
        byte[] data = packet.getData();
        byte[] tmp = new byte[bytes.length + data.length - 1];
        System.arraycopy(bytes, 0, tmp, 0, bytes.length);
        System.arraycopy(data, 1, tmp, bytes.length, data.length - 1);
        bytes = tmp;
    }

    public byte[] getBytes() {
        return Arrays.copyOfRange(bytes, 2, bytes.length - 1);
    }
}
