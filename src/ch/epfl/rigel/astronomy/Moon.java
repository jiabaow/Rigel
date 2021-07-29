package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Locale;

//class represents the Moon at a given instance
//by Marin Cohu
public final class Moon extends CelestialObject {

    private final float percentagePhase;
    private final static ClosedInterval INTERVAL = ClosedInterval.of(0, 1);
    private final String moonPhase;

    /**
     * @param equatorialPos equatorial coordinates
     * @param angularSize   angular size in float
     * @param magnitude     magnitude in float
     * @param phase         phase in float
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase, String moonPhase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        Preconditions.checkInInterval(INTERVAL, phase);
        this.percentagePhase = 100 * phase;
        this.moonPhase = moonPhase;
    }

    /**
     * @return the information about the object in the form of a text string
     * @see CelestialObject#info()
     */
    @Override
    public String info() {
        return String.format(Locale.ROOT,
                name() + "(%.1f%%)",
                percentagePhase);
    }

    /**
     * @return phase in percentage
     */
    public double percentagePhase() {
        return percentagePhase;
    }

    /**
     * @return moon phase name
     */
    public String getMoonPhase() {
        return moonPhase;
    }

}