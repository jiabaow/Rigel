package ch.epfl.rigel.astronomy;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

//enumeration contains two astronomic epochs
//by Jiabao WEN
public enum Epoch {
    J2000(ZonedDateTime.of(2000, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC)),
    J2010(ZonedDateTime.of(2010, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).minusDays(1));

    private final ZonedDateTime time;
    private final static double MILLIS_PER_DAY = 1000 * 60 * 60 * 24;
    private final static double MILLIS_PER_JC = 1000 * 60 * 60 * 24 * 365.25 * 100;

    /**
     * @param time zoned date tine
     */
    Epoch(ZonedDateTime time) {
        this.time = time;
    }

    /**
     * @param when a given zoned date time
     * @return number of days between this epoch and given time.
     */
    public double daysUntil(ZonedDateTime when) {
        return time.until(when, ChronoUnit.MILLIS) / MILLIS_PER_DAY;
    }

    /**
     * @param when a given zoned date time
     * @return number of Julian centuries between this epoch and the given time
     */
    public double julianCenturiesUntil(ZonedDateTime when) {
        return time.until(when, ChronoUnit.MILLIS) / MILLIS_PER_JC;
    }

}
