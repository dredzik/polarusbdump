package io.typedef.cothroime;

import io.typedef.polar.model.Model.Date;
import io.typedef.polar.model.Model.DateTime;
import io.typedef.polar.model.Model.Time;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    public static ZonedDateTime toZonedDateTime(DateTime dateTime) {
        Date date = dateTime.getDate();
        Time time = dateTime.getTime();

        ZonedDateTime result = ZonedDateTime.of(
            date.getYear(),
            date.getMonth(),
            date.getDay(),
            time.getHour(),
            time.getMinute(),
            time.getSecond(),
            0,
            ZoneId.systemDefault());

        if (time.hasMilisecond()) {
            result.plus(time.getMilisecond(), ChronoUnit.MILLIS);
        }

        return result;
    }
}
