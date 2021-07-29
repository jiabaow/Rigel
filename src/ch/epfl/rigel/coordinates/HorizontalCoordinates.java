package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

//represents Horizontal coordinates
//by Jiabao WEN
public final class HorizontalCoordinates extends SphericalCoordinates {

    private final static double ALT_SIZE = 180;
    private final static RightOpenInterval AZ_INTERVAL = RightOpenInterval.of(0, Angle.TAU);
    private final static ClosedInterval ALT_INTERVAL = ClosedInterval.symmetric(Angle.ofDeg(ALT_SIZE));
    private final static RightOpenInterval AZ_DEG_INTERVAL = RightOpenInterval.of(0, Angle.toDeg(Angle.TAU));
    private final static ClosedInterval ALT_DEG_INTERVAL = ClosedInterval.symmetric(ALT_SIZE);

    private HorizontalCoordinates(double az, double alt) {
        super(az, alt);
    }

    /**
     * @param az  azimuth
     * @param alt altitude
     * @return the Horizontal coordinates
     * @throw IllegalArgumentException() if one of the parameter is invalid
     */
    public static HorizontalCoordinates of(double az, double alt) {
        Preconditions.checkInInterval(AZ_INTERVAL, az);
        Preconditions.checkInInterval(ALT_INTERVAL, alt);
        return new HorizontalCoordinates(az, alt);
    }

    /**
     * @param azDeg  azimuth in degree
     * @param altDeg altitude in degree
     * @return the Horizontal coordinates
     * @throw IllegalArgumentException() if one of the parameter is invalid
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        Preconditions.checkInInterval(AZ_DEG_INTERVAL, azDeg);
        Preconditions.checkInInterval(ALT_DEG_INTERVAL, altDeg);
        return new HorizontalCoordinates(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
    }


    /**
     * @return azimuth
     */
    public double az() {
        return super.lon();
    }

    /**
     * @return azimuth in degree
     */
    public double azDeg() {
        return super.lonDeg();
    }

    /**
     * @param n north
     * @param e east
     * @param s south
     * @param w west
     * @return a string correspond to the octant where we find the azimuth
     */
    public String azOctantName(String n, String e, String s, String w) {

        int azVal = (int) Math.round((az() / AZ_INTERVAL.size()) * 8);

        switch (azVal) {
            case 0:
            case 8:
                return n;
            case 1:
                return n + e;
            case 2:
                return e;
            case 3:
                return s + e;
            case 4:
                return s;
            case 5:
                return s + w;
            case 6:
                return w;
            case 7:
                return n + w;
            default:
                throw new Error();
        }
    }

    /**
     * @return altitude
     */
    public double alt() {
        return super.lat();
    }

    /**
     * @return altitude in degree
     */
    public double altDeg() {
        return super.latDeg();
    }

    /**
     * @param that another coordinates
     * @return the angular distance between the receiver and the point where gives the argument
     */
    public double angularDistanceTo(HorizontalCoordinates that) {
        return angularDistanceBetween(this, that);
    }

    private double angularDistanceBetween(HorizontalCoordinates p, HorizontalCoordinates q) {
        double pAlt = p.alt();
        double qAlt = q.alt();
        return Math.acos(sin(pAlt) * sin(qAlt) + cos(pAlt) * cos(qAlt) * cos(p.az() - q.az()));
    }

    /**
     * @see String#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(az = %f, alt = %f)",
                az(), alt());
    }

}
