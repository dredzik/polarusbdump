package pl.niekoniecznie.p2e;

import pl.niekoniecznie.polar.model.Model.SessionData;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Session {
    public static class Builder {

        private Session session = new Session();

        public Builder data(SessionData data) {
            session.start = DateUtil.toZonedDateTime(data.getStart());
            session.end = DateUtil.toZonedDateTime(data.getEnd());

            return this;
        }

        public Session build() {
            return session;
        }
    }

    private ZonedDateTime start;
    private ZonedDateTime end;

    public String toString() {
        return "Session " + start.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + " " + end.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}

