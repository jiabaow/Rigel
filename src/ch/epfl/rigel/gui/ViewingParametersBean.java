package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

//JavaFX bean contains parameters determinate the portion of visible sky on the image
//by Jiabao WEN
public class ViewingParametersBean {
    private final DoubleProperty fieldOfViewDeg = new SimpleDoubleProperty();
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>();

    //fieldOfViewDeg

    /**
     * @return field of view's property
     */
    public DoubleProperty fieldOfViewDegProperty() {
        return fieldOfViewDeg;
    }

    /**
     * @return field of view in degree
     */
    public double getFieldOfViewDeg() {
        return fieldOfViewDeg.get();
    }

    /**
     * @param fieldOfViewDeg field of view in degree
     *                       set field of view with the given param
     */
    public void setFieldOfViewDeg(double fieldOfViewDeg) {
        this.fieldOfViewDeg.set(fieldOfViewDeg);
    }


    //center

    /**
     * @return center's property
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty() {
        return center;
    }

    /**
     * @return center in horizontal coordinates
     */
    public HorizontalCoordinates getCenter() {
        return center.get();
    }

    /**
     * @param center horizontal coordinates
     *               set the center with given param
     */
    public void setCenter(HorizontalCoordinates center) {
        this.center.set(center);
    }
}
