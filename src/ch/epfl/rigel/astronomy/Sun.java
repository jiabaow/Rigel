package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

//represents the Sun at given instance
//by Marin Cohu
public final class Sun extends CelestialObject {

    private static final float SUN_MAGNITUDE = -26.7f;
    private final EclipticCoordinates eclipticPos;
    private final double meanAnomaly;

    /**
     * @param eclipticPos   ecliptic coordinates
     * @param equatorialPos equatorial coordinates
     * @param angularSize   angular size in float
     * @param meanAnomaly   mean anomaly
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos,
               float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize, SUN_MAGNITUDE);

        this.eclipticPos = Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
    }

    /**
     * @return the position of the Sun in Ecliptic Coordinates
     */
    public EclipticCoordinates eclipticPos() {
        return eclipticPos;
    }


    /**
     * @return the mean anomaly of the sun
     */
    public double meanAnomaly() {
        return meanAnomaly;
    }

}
