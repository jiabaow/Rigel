package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static ch.epfl.rigel.astronomy.Epoch.J2000;

//class contains two methods for calculating sidereal time
//by Jiabao WEN
public final class SiderealTime {

    private final static double MILLIS_PER_HOUR = 3600000.0;
    private final static double COEFF = 1.002737909;
    private final static Polynomial POLYNOMIAL = Polynomial.of(0.000025862, 2400.051336, 6.697374558);

    private SiderealTime() {
    }

    /**
     * @param when a given zonedDateTime in couple date/hour
     * @return the sidereal time of Greenwich of the given time in double
     */
    public static double greenwich(ZonedDateTime when) {
        ZonedDateTime inUTC = when.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime days = inUTC.truncatedTo(ChronoUnit.DAYS);
        double T = J2000.julianCenturiesUntil(days);
        double t = days.until(inUTC, ChronoUnit.MILLIS) / MILLIS_PER_HOUR;
        double S0 = POLYNOMIAL.at(T);
        double S1 = COEFF * t;
        double SgHr = S0 + S1;
        double SgRad = Angle.ofHr(SgHr);
        return Angle.normalizePositive(SgRad);
    }

    /**
     * @param when  a zonedDateTime in couple date/hour
     * @param where a geographicCoordinates
     * @return the local sidereal time in radius
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }
}
