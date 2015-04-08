package pl.niekoniecznie.polar.usb;

import java.util.Arrays;

/**
 * Created by ak on 05.04.15.
 */
public class USBPacket {

    public static final int BUFFER_LENGTH = 64;
    public static final int MAX_DATA_SIZE = 61;

    private byte byte0;
    private boolean boolean0;
    private boolean boolean1;
    private byte[] data;

    public USBPacket() {
    }

    public USBPacket(byte[] packet) {
        if (packet.length > BUFFER_LENGTH) {
            throw new IllegalArgumentException("Packet size greater than BUFFER_LENGTH");
        }

        byte0 = packet[0];
        boolean0 = (packet[1] & 1) == 1;
        boolean1 = (packet[1] & 2) == 1;

        int size = (packet[1] & 0xff) >> 2;
        data = Arrays.copyOfRange(packet, 2, size + 2);
    }

    public byte getByte0() {
        return byte0;
    }

    public void setByte0(byte byte0) {
        this.byte0 = byte0;
    }

    public boolean isBoolean0() {
        return boolean0;
    }

    public void setBoolean0(boolean boolean0) {
        this.boolean0 = boolean0;
    }

    public boolean isBoolean1() {
        return boolean1;
    }

    public void setBoolean1(boolean boolean1) {
        this.boolean1 = boolean1;
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

        result[0] = byte0;
        result[1] = new Integer((data.length << 2) | (boolean1 ? 1 << 1 : 0) | (boolean0 ? 1 : 0)).byteValue();

        for (int i = 0; i < data.length; i++) {
            result[i + 2] = data[i];
        }

        return result;
    }
}
