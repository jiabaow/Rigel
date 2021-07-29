package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

//represents ecliptic coordinates
//by Marin Cohu
public final class EclipticCoordinates extends SphericalCoordinates {

    private final static RightOpenInterval LON_INTERVAL = RightOpenInterval.of(0, Angle.TAU);
    private final static ClosedInterval LAT_INTERVAL = ClosedInterval.symmetric(Angle.TAU / 2);

    /**
     * @param longitude the longitude in radians of the object
     * @param latitude  the latitude in radians of the object
     */
    private EclipticCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }


    /**
     * @param lon the longitude in radians of the object
     * @param lat the latitude in radians of the object
     * @return create a new object of EclipticCoordinates with valid values for lon and lat or
     * @throw an IllegalArgumentException
     */
    public static EclipticCoordinates of(double lon, double lat) {
        Preconditions.checkInInterval(LON_INTERVAL, lon);
        Preconditions.checkInInterval(LAT_INTERVAL, lat);
        return new EclipticCoordinates(lon, lat);
    }


    /**
     * @return value of lon (in radians) with the lon() getter from the super class
     */
    @Override
    public double lon() {
        return super.lon();
    }


    /**
     * @return value of lon (in degree) with the lonDeg() getter from the super class
     */
    @Override
    public double lonDeg() {
        return super.lonDeg();
    }


    /**
     * @return value of lat (in radians) with the lat() getter from the super class
     */
    @Override
    public double lat() {
        return super.lat();
    }


    /**
     * @return value of lat (in degree) with the latDeg() getter from the super class
     */
    @Override
    public double latDeg() {
        return super.latDeg();
    }


    /**
     * @return String of the form "(lon=<value of lon>°, lat=<value of lat>°)"
     * @see String#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(λ = %.4f °, β = %.4f °)",
                lonDeg(), latDeg());
    }

}
