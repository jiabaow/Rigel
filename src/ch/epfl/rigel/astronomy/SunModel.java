package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

//represents a sun model
//by Marin Cohu
public enum SunModel implements CelestialObjectModel<Sun> {

    SUN;

    private static final double ECCENTRICITY = 0.016705;
    private static final double ECCENTRICITY_SQUARE = ECCENTRICITY * ECCENTRICITY;
    private static final double THETA0 = Angle.ofDeg(0.533128);
    private static final double EPSILON_G = Angle.ofDeg(279.557208);
    private static final double OMEGA_TILDE_G = Angle.ofDeg(283.112438);
    private static final double TROPICAL_YEAR = 365.242191;
    private static final double TAU_OVER_TROPICAL_YEAR = Angle.TAU / TROPICAL_YEAR;

    /**
     * @param daySinceJ2010                  number of days since J2010
     * @param eclipticToEquatorialConversion conversion from ecliptic to equatorial coordinates
     * @return sun
     */
    @Override
    public Sun at(double daySinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        final double MEAN_ANOMALY = Angle.normalizePositive(TAU_OVER_TROPICAL_YEAR * daySinceJ2010 + EPSILON_G - OMEGA_TILDE_G);
        final double TRUE_ANOMALY = MEAN_ANOMALY + 2 * ECCENTRICITY * sin(MEAN_ANOMALY);
        final double ANGULAR_SIZE = THETA0 * ((1 + ECCENTRICITY * cos(TRUE_ANOMALY)) / (1 - ECCENTRICITY_SQUARE));

        final double lonEcl = Angle.normalizePositive(TRUE_ANOMALY + OMEGA_TILDE_G);

        EclipticCoordinates ecl = EclipticCoordinates.of(lonEcl, 0.0);

        EquatorialCoordinates equ = eclipticToEquatorialConversion.apply(ecl);

        return new Sun(ecl, equ, (float) ANGULAR_SIZE, (float) MEAN_ANOMALY);

    }
}
