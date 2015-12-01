package pl.niekoniecznie.polar.io;

import java.util.Arrays;

public class PolarPacket {

    public static final int BUFFER_LENGTH = 64;
    public static final int MAX_DATA_SIZE = 61;

    private byte[] buffer;

    public PolarPacket(byte[] buffer) {
        if (buffer.length != BUFFER_LENGTH) {
            throw new IllegalArgumentException("Buffer size set to " + buffer.length + ", should be " + BUFFER_LENGTH);
        }

        this.buffer = buffer;
    }

    public int getType() {
        return buffer[0];
    }

    public void setType(int type) {
        buffer[0] = (byte) type;
    }

    public boolean hasMore() {
        return (buffer[1] & 0x03) == 1;
    }

    public void setMore(boolean more) {
        buffer[1] &= 0xfc;
        buffer[1] |= more ? 1 : 0;
    }

    public int getSize() {
        return ((buffer[1] & 0xfc) >> 2) - 1;
    }

    public void setSize(int size) {
        buffer[1] &= 0x03;
        buffer[1] |= (size + 1) << 2;
    }

    public int getSequence() {
        return buffer[2];
    }

    public void setSequence(int sequence) {
        buffer[2] = (byte) sequence;
    }

    public byte[] getData() {
        return Arrays.copyOfRange(buffer, 3, getSize() + 3);
    }

    public void setData(byte[] data) {
        if (data.length > MAX_DATA_SIZE) {
            throw new IllegalArgumentException("Data size set to " + data.length + ", shouldn't be greater than " + MAX_DATA_SIZE);
        }

        System.arraycopy(data, 0, buffer, 3, data.length);
        setSize(data.length);
    }

    public byte[] getBytes() {
        return buffer;
    }
}
