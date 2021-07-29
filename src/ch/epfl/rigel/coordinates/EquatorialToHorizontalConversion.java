package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;

import ch.epfl.rigel.math.Angle;

import java.util.function.Function;

import ch.epfl.rigel.astronomy.SiderealTime;

//represents conversion from equatorial to horizontal coordinates
//by Marin COHU
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private final double siderealTime, sinPhi, cosPhi;

    /**
     * @param when  couple date/hour of the instant in which is the observer
     * @param where location of the observer in geographic coordinates parameters
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        double phi = where.lat();
        this.sinPhi = Math.sin(phi);
        this.cosPhi = Math.cos(phi);
        this.siderealTime = SiderealTime.local(when, where);
    }

    /**
     * @param equ takes an object from Equatorial Coordinates ecl(ra <right ascension>, dec <declination>)
     * @return a Horizontal Coordinates object converted from the equ parameter
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {

        double majH = siderealTime - equ.ra();
        double declination = equ.dec();
        double sinDeclination = Math.sin(declination);
        double cosDeclination = Math.cos(declination);

        double beforeAsin = sinDeclination * sinPhi + cosDeclination * cosPhi * Math.cos(majH);

        double h = Math.asin(beforeAsin);
        double azimuth = Math.atan2(-cosDeclination * cosPhi * Math.sin(majH), sinDeclination - sinPhi * beforeAsin);
        double reducedAzimuth = Angle.normalizePositive(azimuth);

        return HorizontalCoordinates.of(reducedAzimuth, h);
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