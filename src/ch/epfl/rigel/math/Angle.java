package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

//class of angle in radian by default
//by Marin COHU & Jiabao WEN
public final class Angle {
    public final static double TAU = 2*Math.PI;
    public static final int MAX = 60;
    private static final double DEG_PER_RAD = 360.0/TAU;
    private static final double HR_PER_RAD = 24.0/TAU;
    private static final double SEC_PER_RAD =  3600*360/TAU;
    private static final double MIN_PER_RAD = 60*360/TAU;
    private static final RightOpenInterval NOR_INTER = RightOpenInterval.of(0,TAU);

    private Angle(){
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param rad angle in radian
     * @return angle normalize between 0 and 2pi
     */
    public static double normalizePositive(double rad){
        return NOR_INTER.reduce(rad) ;
    }

    /**
     *
     * @param sec second of arc
     * @return angle in radian corresponds to sec
     */
    public static double ofArcsec(double sec){
        return sec/SEC_PER_RAD;
    }

    /**
     *
     * @param deg degree
     * @param min minute
     * @param sec second
     * @return angle in radian corresponds to given values
     * @throws IllegalArgumentException if min/sec is not in the right interval
     */
    public static double ofDMS(int deg, int min, double sec){
        Preconditions.checkArgument(deg >=0);
        Preconditions.checkArgument(min>=0 && min<MAX);
        Preconditions.checkArgument(sec>=0 && sec<MAX);
        return deg/DEG_PER_RAD + min/MIN_PER_RAD + ofArcsec(sec);
    }

    /**
     *
     * @param deg angle in degree
     * @return angle in radian corresponds to given degree
     */
    public static double ofDeg(double deg){
        return Math.toRadians(deg);
    }

    /**
     *
     * @param rad radian
     * @return angle in degree of given radian
     */
    public static double toDeg(double rad){
        return Math.toDegrees(rad);
    }

    /**
     *
     * @param hr hour
     * @return angle in radian of given hour
     */
    public static double ofHr(double hr){
        return hr / HR_PER_RAD;
    }

    /**
     *
     * @param rad radian
     * @return angle in hour of given radian
     */
    public static double toHr(double rad){
        return rad * HR_PER_RAD;
    }
}

