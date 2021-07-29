package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

//class contains conditions which need to be satisfied before execute the methods
//by Marin Cohu and Jiabao Wen
public final class Preconditions {
    private Preconditions() {
    }

    /**
     * @param isTrue the argument to be check
     * @throws IllegalArgumentException if condition is not verified
     */
    public static void checkArgument(boolean isTrue) {
        if (!isTrue) {
            throw new IllegalArgumentException("argument not verified");
        }
    }

    /**
     * @param interval interval
     * @param value    value to be checked
     * @return value if the value is in the interval given
     * @throws IllegalArgumentException otherwise
     */
    public static double checkInInterval(Interval interval, double value) {
        if ((interval.contains(value))) {
            return value;
        } else {
            throw new IllegalArgumentException("given value is not in the given interval");
        }
    }
}
