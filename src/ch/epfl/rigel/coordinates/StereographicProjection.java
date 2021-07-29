package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;

import static java.lang.Math.*;

//represents a stereographic projection of horizontal coordinates
//by Jiabao WEN
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private final double phy1, lambda0;
    private final double sinPhy1;
    private final double cosPhy1;

    /**
     * @param center the center of the stereographic projection
     */
    public StereographicProjection(HorizontalCoordinates center) {
        this.phy1 = center.alt();
        this.lambda0 = center.az();
        this.sinPhy1 = sin(phy1);
        this.cosPhy1 = cos(phy1);
    }

    /**
     * @param hor a point on the parallel's projection
     * @return the coordinates of the circle's center correspond to the projection of the parallel passed by hor
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
        double phy = hor.alt();
        double cy = cosPhy1 / (sin(phy) + sinPhy1);
        return CartesianCoordinates.of(0.0, cy);
    }

    /**
     * @param parallel a point on the parallel's projection
     * @return the circle's radius correspond to the projection of the parallel passed by parallel
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        double phy = parallel.alt();
        return cos(phy) / (sin(phy) + sinPhy1);

    }

    /**
     * @param rad the angular size of sphere
     * @return the diameter projected by the sphere centered in the projection's center
     */
    public double applyToAngle(double rad) {
        return 2 * tan(rad / 4);
    }

    /**
     * @param azAlt Horizontal coordinates of the point where the stereographic projection projected on
     * @return the cartesian coordinates of azAlt's projection
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double phy = azAlt.alt();
        double lambda = azAlt.az();
        double cosPhy = cos(phy);
        double sinPhy = sin(phy);
        double lambdaDelta = lambda - lambda0;
        double cosLambdaDelta = cos(lambdaDelta);

        double d = 1 / (1 + sinPhy * sinPhy1 + cosPhy * cosPhy1 * cosLambdaDelta);
        double x = d * cosPhy * sin(lambdaDelta);
        double y = d * (sinPhy * cosPhy1 - cosPhy * sinPhy1 * cosLambdaDelta);
        return CartesianCoordinates.of(x, y);
    }

    /**
     * @param xy cartesian coordinates of a point
     * @return the horizontal coordinates of xy's projection
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        double x = xy.x();
        double y = xy.y();
        double rouSquare = x * x + y * y;
        double rou = Math.sqrt(rouSquare);
        double sinC = 2 * rou / (rouSquare + 1);
        double cosC = (1 - rouSquare) / (rouSquare + 1);
        double lambda, phy;

        if ((x == 0) && (y == 0)) {
            lambda = lambda0;
            phy = phy1;
        } else {
            lambda = Math.atan2(x * sinC, (rou * cosPhy1 * cosC - y * sinPhy1 * sinC)) + lambda0;
            phy = Math.asin(cosC * sinPhy1 + y * sinC * cosPhy1 / rou);
        }
        return HorizontalCoordinates.of(Angle.normalizePositive(lambda), phy);
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
     * @see String#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "StereographicProjection of the center(%.4f, %.4f)",
                lambda0, phy1);
    }


}
