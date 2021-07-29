package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

//represents equatorial coordinates
//by Marin COHU
public final class EquatorialCoordinates extends SphericalCoordinates { // cette classe est immuable

    private static final RightOpenInterval RA_INTERVAL = RightOpenInterval.of(0, Angle.TAU);
    private static final ClosedInterval DEC_INTERVAL = ClosedInterval.symmetric(Angle.TAU / 2);

    /**
     * @param ra  (right ascension) in radians of the object
     * @param dec (declination) in radians of the object
     */
    private EquatorialCoordinates(double ra, double dec) {
        super(ra, dec);
    }


    /**
     * @param ra  (right ascension) in radians, correspond to the longitude in the super class
     * @param dec (declination) in radians, correspond to the latitude in the super class
     * @return create a new object of EquatorialCoordinates with valid values for ra and dec or
     * @throw an IllegalArgumentException
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        Preconditions.checkInInterval(RA_INTERVAL, ra);
        Preconditions.checkInInterval(DEC_INTERVAL, dec);
        return new EquatorialCoordinates(ra, dec);
    }


    /**
     * @return value of ra (in radians) with the lon() getter of the super class
     */
    public double ra() {
        return lon();
    }


    /**
     * @return value of ra (in degree) with the lonDeg() getter of the super class
     */
    public double raDeg() {
        return lonDeg();
    }


    /**
     * @return value of ra (in hour) with the lon() getter from of the super class
     * and the converter from radians to hour from the imported Angle class
     */
    public double raHr() {
        return Angle.toHr(lon());
    }


    /**
     * @return value of dec (in radians) with the lat() getter of the super class
     */
    public double dec() {
        return lat();
    }


    /**
     * @return value of dec (in degree) with the latDeg() getter from the super class and
     * the converter from radians to degree from the imported Angle class
     */
    public double decDeg() {
        return Angle.toDeg(lat());
    }


    /**
     * @return String of the form : "(ra=<value of ra>h, dec=<value of dec>°)"
     * @see String#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(ra = %.4f h, dec = %.4f °)",
                raHr(), decDeg());
    }

}
