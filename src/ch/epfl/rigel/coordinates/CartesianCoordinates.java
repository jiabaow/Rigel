package ch.epfl.rigel.coordinates;

import java.util.Locale;

//represents cartesian coordinates
//by Jiabao WEN
public final class CartesianCoordinates {

    private final double abscissa, ordinate;

    private CartesianCoordinates(double abscissa, double ordinate) {
        this.abscissa = abscissa;
        this.ordinate = ordinate;
    }

    /**
     * @param x abscissa
     * @param y ordinate
     * @return cartesian Coordinates of (x,y);
     */
    public static CartesianCoordinates of(double x, double y) {
        return new CartesianCoordinates(x, y);
    }

    /**
     * @return abscissa
     */
    public double x() {
        return abscissa;
    }

    /**
     * @return ordinate
     */
    public double y() {
        return ordinate;
    }

    /**
     * @see int#hashCode()
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see boolean#equals(Object)
     */
    @Override
    public final boolean equals(final Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return format string
     * @see String#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(x = %f, y = %f))",
                x(), y());
    }


}
