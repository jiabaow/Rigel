package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

//class represents a star
//by Marin COHU
public final class Star extends CelestialObject {

    private final int hipparcosId;
    private final double colorTemperature;
    private final static double V = 0.92;
    private final static ClosedInterval COLOR_INDEX_INTERVAL = ClosedInterval.of(-0.5, 5.5);

    /**
     * @param hipparcosId   identification number of the star in the Hipparcos catalog
     * @param name          name of the star
     * @param equatorialPos the equatorial position of the star
     * @param magnitude     the magnitude of the star
     * @param colorIndex    the color index of the star
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name, equatorialPos, 0, magnitude);
        Preconditions.checkArgument(hipparcosId >= 0);
        Preconditions.checkInInterval(COLOR_INDEX_INTERVAL, colorIndex);
        this.hipparcosId = hipparcosId;
        this.colorTemperature = 4600 * (1 / (V * colorIndex + 1.7) + 1 / (V * colorIndex + 0.62));
    }

    /**
     * @return star's Hipparcos number
     */
    public int hipparcosId() {
        return hipparcosId;
    }

    /**
     * @return star's color temperature
     */
    public int colorTemperature() {
        return (int) colorTemperature;
    }
}