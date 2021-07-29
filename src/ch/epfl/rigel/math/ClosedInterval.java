package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

//represents a closed interval
//by Marin Cohu & Jiabao WEN
public final class ClosedInterval extends Interval {

    private ClosedInterval (double low, double high){
        super(low, high);
    }

    /**
     * @see Interval#contains(double)
     */
    @Override
    public boolean contains(double v) {
            return ((v >= low())&&(v <= high()));
    }

    /**
     *
     * @param low lower bound of the interval
     * @param high upper bound of the interval
     * @return closed interval bounded by low and high
     */
    public static ClosedInterval of(double low, double high){
        Preconditions.checkArgument(low < high);
        return new ClosedInterval(low, high);
    }

    /**
     *
     * @param size of the symmetric interval
     * @return symmetric closed interval of size given
     */
    public static ClosedInterval symmetric(double size){
        Preconditions.checkArgument(size > 0);
        double value = size/2;
        return new ClosedInterval(-value, value);
    }

    /**
     *
     * @param v value in double
     * @return clip function of v
     */
    public double clip(double v){
        if(v <= low()){
            return low();
        } else return Math.min(v, high());
    }

    /**
     * @see String#toString()
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT,
                     "[%f, %f]",
                             low(), high());
    }


}
