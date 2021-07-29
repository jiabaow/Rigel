package ch.epfl.rigel.astronomy;

import java.util.Objects;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

//an abstract class represents a celestial object
//by Marin COHU
public abstract class CelestialObject {

    private final String name;
    private final double angularSize, magnitude;
    private final EquatorialCoordinates equatorialPos;

    /**
     * @param name          string represents the name of this celestial object
     * @param equatorialPos equatorial coordinates of the object
     * @param angularSize   angular size of the object in float
     * @param magnitude     magnitude of the object in float
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {

        Preconditions.checkArgument(angularSize >= 0);

        this.name = Objects.requireNonNull(name);
        this.angularSize = angularSize;
        this.magnitude = magnitude;
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
    }

    /**
     * @return the name of the Celestial Object in string
     */
    public String name() {
        return name;
    }


    /**
     * @return the angular size of the object in double
     */
    public double angularSize() {
        return angularSize;
    }


    /**
     * @return the magnitude of the object in double
     */
    public double magnitude() {
        return magnitude;
    }


    /**
     * @return the position of the object in Equatorial Coordinates
     */
    public EquatorialCoordinates equatorialPos() {
        return equatorialPos;
    }

    /**
     * @return information about the object, for now it gives only the name
     */
    public String info() {
        //later it will return some short informative text about the celestial object shown to the user
        return name();
    }

    /**
     * @return the information about the object in the form of a text string
     * @see String#toString()
     */
    @Override
    public String toString() {
        return info();
    }
}