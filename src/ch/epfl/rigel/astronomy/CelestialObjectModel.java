package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

//interface represents a celestial object model
//by Marin COHU
public interface CelestialObjectModel<O> {
    /**
     *
     * @param daySinceJ2010 number of days since J2020
     * @param eclipticToEquatorialConversion ecliptic to equatorial conversion
     * @return object modeled by the model
     */
    public abstract O at(double daySinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}
