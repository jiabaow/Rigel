package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

//represents a right open interval
//by Marin COHU & Jiabao WEN
public final class RightOpenInterval extends Interval {


    private RightOpenInterval(double low, double high) {
        super(low, high);
    }

    /**
     * @see Interval#contains(double)
     */
    @Override
    public boolean contains(double v) {
        return ((v >= low()) && (v < high()));
    }

    /**
     * @param low  lower bound
     * @param high upper bound
     * @return right Open Interval of given low and high
     */
    public static RightOpenInterval of(double low, double high) {
        Preconditions.checkArgument(low < high);
        return new RightOpenInterval(low, high);
    }

    /**
     * @param size size of the symmetric interval
     * @return right open symmetric interval of given size
     */
    public static RightOpenInterval symmetric(double size) {
        Preconditions.checkArgument(size > 0);
        double value = size / 2;
        return new RightOpenInterval(-value, value);
    }

    /**
     * @see String#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%f, %f[", low(), high());
    }

    /**
     * @param v value in double
     * @return interval reduced by v
     */
    public double reduce(double v) {
        double x = v - low();
        double y = size();
        double floorMod = x - y * Math.floor(x / y);
        return low() + floorMod;
    }

}
