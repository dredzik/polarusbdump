package io.typedef.cothroime.converter;

import io.typedef.cothroime.DateUtil;
import io.typedef.cothroime.model.Session;
import io.typedef.polar.model.Model.SessionData;

public class SessionConverter implements Converter<SessionData, Session> {

    public Session convert(SessionData data) {
        Session result = new Session();

        result.setDateStarted(DateUtil.toZonedDateTime(data.getStart()));
        result.setDateFinished(DateUtil.toZonedDateTime(data.getEnd()));

        return result;
    }
}
