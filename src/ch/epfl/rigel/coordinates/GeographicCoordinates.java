package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

//represent geographic coordinates extends spherical coordinates
//by Jiabao WEN
public final class GeographicCoordinates extends SphericalCoordinates {
    private final static ClosedInterval LATDEG_INTERVAL = ClosedInterval.symmetric(180);
    private final static RightOpenInterval LONDEG_INTERVAL = RightOpenInterval.symmetric(360);

    /**
     *
     * @param longitude of the GeographicCoordinates
     * @param latitude of the GeographicCoordinates
     */
    private GeographicCoordinates (double longitude, double latitude){
        super(longitude,latitude);
    }

    /**
     *
     * @param lonDeg longitude in degree
     * @param latDeg latitude in degree
     * @return the geographic coordinates
     * @throw IllegalArgumentException if one of the parameter is invalid
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg){
        Preconditions.checkArgument(isValidLonDeg(lonDeg));
        Preconditions.checkArgument(isValidLatDeg(latDeg));
        return new GeographicCoordinates(Angle.ofDeg(lonDeg),Angle.ofDeg(latDeg));
    }

    /**
     *
     * @param lonDeg longitude in degree
     * @return true iff the angle represent a valid longitude in degree
     */
    public static boolean isValidLonDeg(double lonDeg) {
        return LONDEG_INTERVAL.contains(lonDeg);
    }

    /**
     *
     * @param latDeg latitude in degree
     * @return true iff the angle represet a valid latitude in degree
     */
    public static boolean isValidLatDeg(double latDeg){
        return LATDEG_INTERVAL.contains(latDeg);
    }

    /**
     *
     * @return longitude
     */
    @Override
    public double lon(){
        return super.lon();
    }

    /**
     *
     * @return longitude in degree
     */
    @Override
    public double lonDeg(){
        return super.lonDeg();
    }

    /**
     *
     * @return latitude
     */
    @Override
    public double lat(){
        return super.lat();
    }

    /**
     *
     * @return the latitude in degree
     */
    @Override
    public double latDeg(){
        return super.latDeg();
    }

    /**
     * @see String#toString()
     * @return a formatted String which represent the coordinates
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT,
                            "(lon = %.4f°, lat = %.4f°)",
                             Angle.toDeg(super.lon()),Angle.toDeg(super.lat()));
    }
}
