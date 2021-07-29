package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

//represents a planet
//by Marin COHU
public final class Planet extends CelestialObject {

    /**
     * @param name          name of the planet
     * @param equatorialPos equatorial coordinates
     * @param angularSize   angular size in float
     * @param magnitude     magnitude in float
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }

}
