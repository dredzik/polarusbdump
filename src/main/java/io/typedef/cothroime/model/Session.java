package io.typedef.cothroime.model;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Session extends Entity {

    private ZonedDateTime dateStarted;
    private ZonedDateTime dateFinished;
}
