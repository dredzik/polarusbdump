package io.typedef.polar.io;

import io.typedef.polar.model.Model.ListMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class PolarFileSystem {

    private final PolarService service;

    public PolarFileSystem(final PolarService service) {
        this.service = service;
    }

    public InputStream get(final PolarEntry file) {
        String path = file.getPath();
        PolarRequest request = new PolarRequest(path);
        PolarResponse response;

        try {
            service.write(request.getData());
            response = service.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new PolarInputStream(response.getBytes());
    }

    public List<PolarEntry> list(PolarEntry directory) {
        String path = directory.getPath();
        PolarRequest request = new PolarRequest(path);
        PolarResponse response;

        try {
            service.write(request.getData());
            response = service.read();
        } catch (IOException e) {
            throw new RuntimeException("Cannot list " + directory.getPath(), e);
        }

        try {
            return ListMessage
                .parseFrom(response.getBytes())
                .getEntryList()
                .stream()
                .map(x -> new PolarEntry(path, x))
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
