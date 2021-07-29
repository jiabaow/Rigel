package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.List;

import static java.lang.Math.*;

//class contains models of the 8 plants of the solar system
//by Jiabao WEN
public enum PlanetModel implements CelestialObjectModel<Planet> {
    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    private final String frName;
    private final double period, longitudeJ2010, longitudePerigee, eccentricity, demiAxis, inclination, longitudeNode, angularSize, magnitude;

    /**
     * @param frName              french name
     * @param period              revolution's period
     * @param longitudeJ2010Deg   longitude at J2010 of degree
     * @param longitudePerigeeDeg longitude of perigee of degree
     * @param eccentricity        orbit's eccentricity
     * @param demiAxis            half major axis of the orbit
     * @param inclinationDeg      inclination of the orbit at the ecliptic of degree
     * @param longitudeNodeDeg    longitude of orbital node of degree
     * @param angularSizeSec      angular size of the planet at 1UA of arc second
     * @param magnitude           magnitude of the planet at 1UA
     */
    PlanetModel(String frName, double period, double longitudeJ2010Deg, double longitudePerigeeDeg, double eccentricity, double demiAxis, double inclinationDeg, double longitudeNodeDeg, double angularSizeSec, double magnitude) {
        this.frName = frName;
        this.period = period;
        this.longitudeJ2010 = Angle.ofDeg(longitudeJ2010Deg);
        this.longitudePerigee = Angle.ofDeg(longitudePerigeeDeg);
        this.eccentricity = eccentricity;
        this.demiAxis = demiAxis;
        this.inclination = Angle.ofDeg(inclinationDeg);
        this.longitudeNode = Angle.ofDeg(longitudeNodeDeg);
        this.angularSize = Angle.ofArcsec(angularSizeSec);
        this.magnitude = magnitude;
    }

    private final static double TAU = 2 * Math.PI;
    public final static List<PlanetModel> ALL = List.of(PlanetModel.values());
    private final static double TROPICAL_YEAR = 365.242191;
    private final static double TAU_OVER_TROPICAL_YEAR = TAU / TROPICAL_YEAR;

    /**
     * @param daySinceJ2010                  number of days after epoch J2010
     * @param eclipticToEquatorialConversion conversion from ecliptic Coordinates to Equatorial Coordinates
     * @return Planet
     */
    @Override
    public Planet at(double daySinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double trueAnomaly = trueAnomaly(daySinceJ2010);
        double radius = r(trueAnomaly);
        double lonHelio = l(trueAnomaly);
        double lonDiff = lonHelio - longitudeNode;
        double sinLonDiff = sin(lonDiff);
        double latEclHelio = asin(sinLonDiff * sin(inclination));
        double cosLatEclDiff = cos(latEclHelio);
        double radProj = radius * cosLatEclDiff;
        double lonProj = atan2((sinLonDiff * cos(inclination)), cos(lonDiff)) + longitudeNode;
        double radEarth = EARTH.r(EARTH.trueAnomaly(daySinceJ2010));
        double lonEarth = EARTH.l(EARTH.trueAnomaly(daySinceJ2010));
        double lonProjEarth = lonProj - lonEarth;
        double lonEarthProj = lonEarth - lonProj;
        double lonEclGeo;
        final double v = radEarth * sin(lonProjEarth);

        if ((this == MERCURY) || (this == VENUS)) {
            lonEclGeo = Math.PI + lonEarth + atan2((radProj * sin(lonEarthProj)), (radEarth - radProj * cos(lonEarthProj)));
        } else {
            lonEclGeo = lonProj + atan2(v, (radProj - radEarth * cos(lonProjEarth)));
        }
        double latEclGeo = atan((radProj * tan(latEclHelio) * sin(lonEclGeo - lonProj)) / v);

        double rou = sqrt(radEarth * radEarth + radius * radius - 2 * radEarth * radius * cos(lonHelio - lonEarth) * cosLatEclDiff);
        double angularSize = this.angularSize / rou;
        double phase = ((1 + cos(lonEclGeo - lonHelio)) / 2);
        double magnitude = this.magnitude + 5 * Math.log10((radius * rou) / sqrt(phase));
        EclipticCoordinates ecl = EclipticCoordinates.of(Angle.normalizePositive(lonEclGeo), latEclGeo);
        EquatorialCoordinates equa = eclipticToEquatorialConversion.apply(ecl);

        return new Planet(this.frName, equa, (float) angularSize, (float) magnitude);
    }

    private double trueAnomaly(double D) {
        double M = TAU_OVER_TROPICAL_YEAR * (D / period) + longitudeJ2010 - longitudePerigee;
        return M + 2 * eccentricity * sin(M);
    }

    private double r(double nu) {
        return (demiAxis * (1 - eccentricity * eccentricity)) / (1 + eccentricity * cos(nu));
    }

    private double l(double nu) {
        return (nu + longitudePerigee);
    }


}
