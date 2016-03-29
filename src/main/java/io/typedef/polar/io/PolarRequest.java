package io.typedef.polar.io;

import io.typedef.polar.model.Model.CommandMessage;
import io.typedef.polar.model.Model.CommandType;

public class PolarRequest {

    private String url;

    public PolarRequest(String url) {
        this.url = url;
    }

    public byte[] getData() {
        byte[] message = CommandMessage.newBuilder().setType(CommandType.READ).setPath(url).build().toByteArray();
        byte[] data = new byte[message.length + 3];

        data[0] = (byte) message.length;
        data[1] = 0x00;

        data[data.length - 1] = 0x00;

        System.arraycopy(message, 0, data, 2, message.length);
        return data;
    }
}
