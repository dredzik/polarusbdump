package pl.niekoniecznie.polar.service;

import pl.niekoniecznie.polar.device.PolarPacket;

import java.nio.charset.Charset;

public class PolarRequest {

    private String url;

    public PolarRequest(String url) {
        this.url = url;
    }

    public PolarPacket getPacket() {
        byte[] data = new byte[url.length() + 8];
        Integer length1 = url.length() + 4;
        Integer length2 = url.length();

        data[0] = 0x00;
        data[1] = length1.byteValue();
        data[2] = 0x00;
        data[3] = 0x08;
        data[4] = 0x00;
        data[5] = 0x12;
        data[6] = length2.byteValue();

        byte[] tmp = url.getBytes(Charset.forName("UTF-8"));

        for (int i = 0; i < length2; i++) {
            data[7 + i] = tmp[i];
        }

        data[data.length - 1] = 0x00;

        PolarPacket result0 = new PolarPacket();

        result0.setType((byte) 0x01);
        result0.setData(data);

        return result0;
    }
}
