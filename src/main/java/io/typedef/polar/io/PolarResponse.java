package io.typedef.polar.io;

import java.util.Arrays;

public class PolarResponse {

    private byte[] bytes = new byte[0];

    public void append(byte[] data) {
        byte[] tmp = new byte[bytes.length + data.length];
        System.arraycopy(bytes, 0, tmp, 0, bytes.length);
        System.arraycopy(data, 0, tmp, bytes.length, data.length);
        bytes = tmp;
    }

    public byte[] getBytes() {
        return Arrays.copyOfRange(bytes, 2, bytes.length - 1);
    }
}
