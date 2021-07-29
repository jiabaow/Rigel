package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import static java.lang.Math.*;

//represents a moon model
//by Jiabao WEN
public enum MoonModel implements CelestialObjectModel<Moon> {
    MOON;

    private static final double LONGITUDE_AVERAGE = Angle.ofDeg(91.929336);
    private static final double LONGITUDE_PERIGEE = Angle.ofDeg(130.143076);
    private static final double LONGITUDE_NODE = Angle.ofDeg(291.682547);
    private static final double INCLINATION = Angle.ofDeg(5.145396);
    private static final double ECCENTRICITY = 0.0549;
    private static final double ECCENTRICITY_SQUARE = ECCENTRICITY * ECCENTRICITY;

    private final static double v1 = Angle.ofDeg(13.1763966);
    private final static double v2 = Angle.ofDeg(0.1114041);
    private final static double v3 = Angle.ofDeg(1.2739);
    private final static double v4 = Angle.ofDeg(0.1858);
    private final static double v5 = Angle.ofDeg(0.37);
    private final static double v6 = Angle.ofDeg(6.2886);
    private final static double v7 = Angle.ofDeg(0.214);
    private final static double v8 = Angle.ofDeg(0.6583);

    private final static double v9 = Angle.ofDeg(0.0529539);
    private final static double v10 = Angle.ofDeg(0.16);
    private final static double theta0 = Angle.ofDeg(0.5181);

    /**
     * @param daySinceJ2010                  number of days after epoch J2010
     * @param eclipticToEquatorialConversion conversion from ecliptic to equatorial coordinates
     * @return Moon calculated under the above parameters
     */
    @Override
    public Moon at(double daySinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double orbLonMoy = v1 * daySinceJ2010 + LONGITUDE_AVERAGE;
        double meanAnomaly = orbLonMoy - v2 * daySinceJ2010 - LONGITUDE_PERIGEE;
        Sun sun = SunModel.SUN.at(daySinceJ2010, eclipticToEquatorialConversion);
        double longitudeSum = sun.eclipticPos().lon();
        double evection = v3 * sin(2 * (orbLonMoy - longitudeSum) - meanAnomaly);
        double anomalySun = sun.meanAnomaly();
        double sinAnomalySun = sin(anomalySun);
        double annualEqCorrec = v4 * sinAnomalySun;
        double correction3 = v5 * sinAnomalySun;
        double anomalyCor = meanAnomaly + evection - annualEqCorrec - correction3;
        double centerEquCor = v6 * sin(anomalyCor);
        double correction4 = v7 * sin(2 * anomalyCor);
        double orbLonCor = orbLonMoy + evection + centerEquCor - annualEqCorrec + correction4;
        double variation = v8 * sin(2 * (orbLonCor - longitudeSum));
        double orbLonTrue = orbLonCor + variation;
        double lonNodeMoy = LONGITUDE_NODE - v9 * daySinceJ2010;
        double lonNodeCor = lonNodeMoy - v10 * sinAnomalySun;
        double diff = orbLonTrue - lonNodeCor;

        double lonEcl = Math.atan2(sin(diff) * cos(INCLINATION), cos(diff)) + lonNodeCor;
        double latEcl = Math.asin(sin(diff) * sin(INCLINATION));
        EclipticCoordinates eclCoor = EclipticCoordinates.of(Angle.normalizePositive(lonEcl), latEcl);
        EquatorialCoordinates equaCoor = eclipticToEquatorialConversion.apply(eclCoor);

        double lonDiff = orbLonTrue - sun.eclipticPos().lon();
        double phase = (1 - cos(lonDiff)) / 2;
        String moonPhase = moonPhaseCalc(lonDiff);

        double rou = (1 - ECCENTRICITY_SQUARE) / (1 + ECCENTRICITY * cos(anomalyCor + centerEquCor));
        double angularSize = theta0 / rou;
        return new Moon(equaCoor, (float) angularSize, 0, (float) phase, moonPhase);
    }

    private String moonPhaseCalc(double lonDiff) {

        int diff = (int) Math.round((Angle.normalizePositive(lonDiff) / (2 * PI)) * 8);

        switch (diff) {
            case 0:
            case 8:
                return "new moon";
            case 1:
                return "waxing crescent";
            case 2:
                return "first quarter";
            case 3:
                return "waxing gibbous";
            case 4:
                return "full moon";
            case 5:
                return "waning gibbous";
            case 6:
                return "last quarter";
            case 7:
                return "waning crescent";
            default:
                throw new IllegalArgumentException();
        }
    }


}
