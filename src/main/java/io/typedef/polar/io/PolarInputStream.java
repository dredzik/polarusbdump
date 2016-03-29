package io.typedef.polar.io;

import java.io.InputStream;

class PolarInputStream extends InputStream {

    private final byte[] buffer;
    private int pointer;

    public PolarInputStream(byte[] buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() {
        if (pointer >= buffer.length) {
            return -1;
        }

        return buffer[pointer++] & 0xff;
    }

    @Override
    public int available() {
        if (buffer == null) {
            return 0;
        }

        return buffer.length - pointer;
    }
}
