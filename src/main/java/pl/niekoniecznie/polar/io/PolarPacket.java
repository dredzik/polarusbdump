package pl.niekoniecznie.polar.io;

import java.util.Arrays;

public class PolarPacket {

    public static final int BUFFER_LENGTH = 64;
    public static final int MAX_DATA_SIZE = 61;

    private byte type;
    private boolean continuation;
    private byte[] data;

    public PolarPacket() {
    }

    public PolarPacket(byte[] packet) {
        if (packet.length > BUFFER_LENGTH) {
            throw new IllegalArgumentException("Packet size greater than BUFFER_LENGTH");
        }

        type = packet[0];
        continuation = (packet[1] & 1) == 1;

        int size = (packet[1] & 0xff) >> 2;
        data = Arrays.copyOfRange(packet, 2, size + 2);
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public boolean isContinuation() {
        return continuation;
    }

    public void setContinuation(boolean continuation) {
        this.continuation = continuation;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        if (data.length > MAX_DATA_SIZE) {
            throw new IllegalArgumentException("Packet data greater than MAX_DATA_SIZE");
        }

        this.data = data;
    }

    public byte[] getBytes() {
        byte[] result = new byte[BUFFER_LENGTH];
        Arrays.fill(result, (byte) 0x00);

        result[0] = type;
        result[1] = new Integer((data.length << 2) | (continuation ? 1 : 0)).byteValue();

        for (int i = 0; i < data.length; i++) {
            result[i + 2] = data[i];
        }

        return result;
    }
}
