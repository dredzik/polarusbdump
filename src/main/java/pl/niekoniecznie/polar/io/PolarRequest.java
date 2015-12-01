package pl.niekoniecznie.polar.io;

import pl.niekoniecznie.polar.model.Model.CommandMessage;
import pl.niekoniecznie.polar.model.Model.CommandType;

public class PolarRequest {

    private String url;

    public PolarRequest(String url) {
        this.url = url;
    }

    public byte[] getData() {
        byte[] message = CommandMessage.newBuilder().setType(CommandType.READ).setPath(url).build().toByteArray();
        byte[] data = new byte[message.length + 4];

        data[0] = 0x00;

        data[1] = (byte) message.length;
        data[2] = 0x00;

        data[data.length - 1] = 0x00;

        System.arraycopy(message, 0, data, 3, message.length);
        return data;
    }
}
