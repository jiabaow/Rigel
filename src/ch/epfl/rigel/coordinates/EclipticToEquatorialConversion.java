package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import static ch.epfl.rigel.astronomy.Epoch.J2000;
import static java.lang.Math.*;

//immutable class represents a conversion from ecliptic coordinates to equatorial coordinates
//by Marin COHU
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private final double sinEpsilon, cosEpsilon;

    private static final Polynomial POL_EPSILON = Polynomial.of(Angle.ofDMS(0, 0, 0.00181),
                                                  -Angle.ofDMS(0, 0, 0.0006),
                                                  -Angle.ofDMS(0, 0, 46.815),
                                                  Angle.ofDMS(23, 26, 21.45));

    /**
     * @param when couple date/hour of the instant in which is the observer
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        double nbrOfJulCent = J2000.julianCenturiesUntil(when);
        double epsilon = POL_EPSILON.at(nbrOfJulCent);
        this.sinEpsilon = sin(epsilon);
        this.cosEpsilon = cos(epsilon);
    }

    /**
     * @param ecl takes an object from Ecliptic Coordinates ecl(lon <longitude>, lat <latitude>)
     * @return an Equatorial Coordinates object converted from the ecl parameter
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {

        double sinLambda = sin(ecl.lon());

        double rightAscension = atan2(sinLambda * cosEpsilon - tan(ecl.lat()) * sinEpsilon, cos(ecl.lon()));
        double declination = asin(sin(ecl.lat()) * cosEpsilon + cos(ecl.lat()) * sinEpsilon * sinLambda);

        double reducedRightAscension = Angle.normalizePositive(rightAscension);

        return EquatorialCoordinates.of(reducedRightAscension, declination);
    }

    /**
     * @throws UnsupportedOperationException to avoid any mistake in using this method
     * @see Object#hashCode()
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }


    /**
     * @throws UnsupportedOperationException to avoid any mistake in using this method
     * @see boolean#equals(Object)
     */
    @Override
    public final boolean equals(final Object o) {
        throw new UnsupportedOperationException();
    }


}
