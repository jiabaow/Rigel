package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;



import static java.lang.Math.abs;

//a manager of the canvas where we paint the sky
//by Jiabao WEN
public class SkyCanvasManager {
    private final Canvas canvas;
    private final SkyCanvasPainter painter;
    private final ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>(Point2D.ZERO);
    private final ObservableObjectValue<StereographicProjection> projection;
    private final ObservableObjectValue<HorizontalCoordinates> mouseHorizontalPosition;
    private final ObservableObjectValue<ObservedSky> observedSky;
    private final ObservableObjectValue<Transform> planeToCanvas;
    private final ObjectBinding<CelestialObject> objectUnderMouse;
    public ObservableDoubleValue mouseAzDeg, mouseAltDeg;
    private final static RightOpenInterval AZ_INTERVAL = RightOpenInterval.of(0, 360);
    private final static ClosedInterval ALT_INTERVAL = ClosedInterval.of(5, 90);
    private final static ClosedInterval FOV_INTERVAL = ClosedInterval.of(30, 150);
    private final static double AZ_CHANGE = 10;
    private final static double ALT_CHANGE = 5;
    private final static int DISTANCE_MAX = 10;

    /**
     * a manager of the canvas where we paint the sky
     *
     * @param catalogue             star catalogue
     * @param dateTimeBean          bean for date time
     * @param observerLocationBean  bean for observer's location
     * @param viewingParametersBean bean for viewing parameters
     */
    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dateTimeBean,
                            ObserverLocationBean observerLocationBean,
                            ViewingParametersBean viewingParametersBean) {

        canvas = new Canvas(800, 600);

        painter = new SkyCanvasPainter(canvas);

        projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(viewingParametersBean.getCenter()),
                viewingParametersBean.centerProperty());


        planeToCanvas = Bindings.createObjectBinding(
                () -> {
                    double width = projection.get().applyToAngle(Angle.ofDeg(viewingParametersBean.getFieldOfViewDeg()));
                    double scale = canvas.getWidth() / width;
                    return Transform.affine(scale, 0, 0, -scale, canvas.getWidth() * 0.5, canvas.getHeight() * 0.5);
                },
                projection, canvas.widthProperty(), canvas.heightProperty(), viewingParametersBean.fieldOfViewDegProperty());


        observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(), projection.get(), catalogue),
                dateTimeBean.dateProperty(), dateTimeBean.timeProperty(), dateTimeBean.zoneProperty(), observerLocationBean.coordinates, projection);


        observedSky.addListener(o ->
                painter.drawAll(observedSky.get(), projection.get(), planeToCanvas.get(), observerLocationBean.getLatDeg())
        );

        planeToCanvas.addListener(o ->
                painter.drawAll(observedSky.get(), projection.get(), planeToCanvas.get(), observerLocationBean.getLatDeg())
        );


        canvas.setOnKeyPressed(event -> {
            HorizontalCoordinates center = viewingParametersBean.getCenter();
            double centerAz = center.azDeg();
            double centerAlt = center.altDeg();
            if (event.getCode() == KeyCode.LEFT) {
                viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(
                        AZ_INTERVAL.reduce(centerAz - AZ_CHANGE), centerAlt));
                event.consume();
            } else if (event.getCode() == KeyCode.RIGHT) {
                viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(
                        AZ_INTERVAL.reduce(centerAz + AZ_CHANGE), centerAlt));
                event.consume();
            } else if (event.getCode() == KeyCode.UP) {
                viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(
                        centerAz, ALT_INTERVAL.clip(centerAlt + ALT_CHANGE)));
                event.consume();
            } else if (event.getCode() == KeyCode.DOWN) {
                viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(
                        centerAz, ALT_INTERVAL.clip(centerAlt - ALT_CHANGE)));
                event.consume();
            }
        });

        canvas.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown())
                canvas.requestFocus();
        });

        canvas.setOnMouseMoved(event ->
                mousePosition.set(new Point2D(event.getX(), event.getY()))
        );

        canvas.setOnScroll(event -> {
            double FoV = (abs(event.getDeltaX()) > abs(event.getDeltaY()) ? event.getDeltaX() : event.getDeltaY());
            viewingParametersBean.setFieldOfViewDeg(FOV_INTERVAL.clip(FoV + viewingParametersBean.getFieldOfViewDeg()));
        });


        mouseHorizontalPosition = Bindings.createObjectBinding(
                () -> {
                    try {
                        Point2D horizontalCoordinatesAfter = planeToCanvas.get().inverseTransform(mousePosition.get());

                        return projection.get().inverseApply(CartesianCoordinates.of(horizontalCoordinatesAfter.getX(), horizontalCoordinatesAfter.getY()));

                    } catch (NonInvertibleTransformException e) {
                        return null;
                    }
                },
                mousePosition, projection, planeToCanvas);


        mouseAzDeg = Bindings.createDoubleBinding(
                () -> {
                    if (mouseHorizontalPosition.get() != null)
                        return mouseHorizontalPosition.get().azDeg();
                    return 0.0;
                },
                mouseHorizontalPosition
        );


        mouseAltDeg = Bindings.createDoubleBinding(
                () -> {
                    if (mouseHorizontalPosition.get() != null)
                        return mouseHorizontalPosition.get().altDeg();
                    return 0.0;
                },
                mouseHorizontalPosition
        );


        objectUnderMouse = Bindings.createObjectBinding(
                () -> {
                    try {
                        Point2D mousePositionBefore = planeToCanvas.get().inverseTransform(mousePosition.get());
                        CartesianCoordinates mPBCoor = CartesianCoordinates.of(mousePositionBefore.getX(), mousePositionBefore.getY());
                        double distanceMax = planeToCanvas.get().inverseDeltaTransform(10, 0).getX();

                        return observedSky.get().objectClosestTo(mPBCoor, distanceMax).orElse(null);
                    } catch (NonInvertibleTransformException e) {
                        return null;
                    }
                },
                observedSky, mousePosition, planeToCanvas
        );

    }


    /**
     * @return canvas
     */
    public Canvas canvas() {
        return canvas;
    }

    /**
     * @return object under mouse property
     */
    public ObjectBinding<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }
}