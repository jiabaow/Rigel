package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

//represents spherical coordinates
//by Jiabao WEN
abstract class SphericalCoordinates {
    private final double longitude, latitude;

    /**
     * @param longitude of SphericalCoordinates
     * @param latitude  of SphericalCoordinates
     */
    SphericalCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @return longitude
     */
    double lon() {
        return longitude;
    }

    /**
     * @return longitude in degree
     */
    double lonDeg() {
        return Angle.toDeg(lon());
    }

    /**
     * @return latitude
     */
    double lat() {
        return latitude;
    }

    /**
     * @return latitude in degree
     */
    double latDeg() {
        return Angle.toDeg(lat());
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
}
