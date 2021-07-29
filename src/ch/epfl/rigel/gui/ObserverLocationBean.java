package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableObjectValue;


//JavaFX bean contains observer's location in degree
//by Jiabao WEN
public class ObserverLocationBean {
    private final DoubleProperty lonDeg = new SimpleDoubleProperty();
    private final DoubleProperty latDeg = new SimpleDoubleProperty();

    //lonDeg

    /**
     * @return longitude property
     */
    public DoubleProperty lonDegProperty() {
        return lonDeg;
    }

    /**
     * @return longitude in degree
     */
    public double getLonDeg() {
        return lonDeg.get();
    }

    /**
     * @param lonDeg longitude in degree in double
     *               set longitude with the given degree
     */
    public void setLonDeg(double lonDeg) {
        this.lonDeg.set(lonDeg);
    }


    //latDeg

    /**
     * @return latitude property
     */
    public DoubleProperty latDegProperty() {
        return latDeg;
    }

    /**
     * @return latitude in degree
     */
    public double getLatDeg() {
        return latDeg.get();
    }

    /**
     * @param latDeg latitude in degree in double
     *               set latitude with the given degree
     */
    public void setLatDeg(double latDeg) {
        this.latDeg.set(latDeg);
    }

    /**
     * observable geographic coordinates
     */
    public ObservableObjectValue<GeographicCoordinates> coordinates = Bindings.createObjectBinding(
            () -> GeographicCoordinates.ofDeg(getLonDeg(), getLatDeg()),
            lonDeg, latDeg);

    /**
     * set the coordinates to the given value
     *
     * @param coordinatesDeg geographic coordinates
     */
    public void setCoordinates(GeographicCoordinates coordinatesDeg) {
        this.setLonDeg(coordinatesDeg.lonDeg());
        this.setLatDeg(coordinatesDeg.latDeg());
    }

    /**
     * @return geographic coordinates
     */
    public GeographicCoordinates getCoordinates() {
        return coordinates.get();
    }

}
