package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;


//represents a painter which draws the sky on a canvas
//by Marin COHU & Jiabao WEN
public class SkyCanvasPainter {
    private Canvas canvas;
    private GraphicsContext ctx;
    private final static ClosedInterval INTERVAL = ClosedInterval.of(-2, 5);
    private final static double MAGNITUDE = 2 * Math.tan(Angle.ofDeg(0.5) / 4);


    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        ctx = canvas.getGraphicsContext2D();
    }

    /**
     * clear the canvas
     */
    public void clear() {
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * draw the asterismes and stars
     *
     * @param sky           observed sky
     * @param projection    used stereographic projection
     * @param planeToCanvas transformation from plane to canvas
     */
    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        //draw asterisms
        ctx.setStroke(Color.BLUE);
        ctx.setLineWidth(1);
        var position = sky.starPositions();
        planeToCanvas.transform2DPoints(position, 0, position, 0, position.length / 2);

        for (Asterism asterism : sky.asterisms()) {
            ctx.beginPath();
            // 1st star
            var as = sky.asterismIndices(asterism);
            int index1st = as.get(0);
            double x1st = position[index1st * 2];
            double y1st = position[index1st * 2 + 1];
            boolean visible1st = canvas.getBoundsInLocal().contains(x1st, y1st);
            ctx.moveTo(x1st, y1st);

            //start with index1
            for (Integer index : as.subList(1, as.size())) {
                double x0 = position[index * 2];
                double y0 = position[index * 2 + 1];
                boolean visible = canvas.getBoundsInLocal().contains(x0, y0);
                if (visible || visible1st) {
                    ctx.lineTo(x0, y0);
                }
                visible1st = visible;
                ctx.moveTo(x0, y0);
            }
            ctx.stroke();

        }

        //draw stars
        for (int i = 0; i < sky.stars().size() - 1; i += 2) {
            Star star = sky.stars().get(i / 2);
            double diameter0 = diameterCal(star.magnitude());
            double diameter = planeToCanvas.deltaTransform(diameter0, 0).getX();
            double x = position[i];
            double y = position[i + 1];
            Color color = BlackBodyColor.colorForTemperature(star.colorTemperature());
            ctx.setFill(color);
            ctx.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
        }

    }

    /**
     * draw planets
     *
     * @param sky           observed sky
     * @param projection    used stereographic projection
     * @param planeToCanvas transformation from plane to canvas
     */
    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        ctx.setFill(Color.LIGHTGRAY);
        var position = sky.planetPositions();
        planeToCanvas.transform2DPoints(position, 0, position, 0, position.length / 2);

        for (int i = 0; i < 13; i += 2) {
            int v = i / 2;
            Planet planet = sky.planets().get(v);
            double diameter0 = diameterCal(planet.magnitude());
            double diameter = planeToCanvas.deltaTransform(diameter0, 0).getX();
            double x = position[i];
            double y = position[i + 1];
            ctx.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);

            String planetsName = planetsName(v);
            ctx.fillText(planetsName, x + diameter / 2, y + diameter / 2);
        }
    }

    private String planetsName(int v) {

        switch (v) {
            case 0:
                return "Mercury";
            case 1:
                return "Venus";
            case 2:
                return "Mars";
            case 3:
                return "Jupiter";
            case 4:
                return "Saturn";
            case 5:
                return "Uranus";
            case 6:
                return "Neptune";
            default:
                throw new Error();
        }
    }


    /**
     * draw the Sun
     *
     * @param sky           observed sky
     * @param projection    used stereographic projection
     * @param planeToCanvas transformation from plane to canvas
     */
    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        CartesianCoordinates sunPos = sky.sunPosition();
        Point2D center = planeToCanvas.transform(sunPos.x(), sunPos.y());
        double x = center.getX();
        double y = center.getY();
        double diameter1 = projection.applyToAngle(sky.sun().angularSize());
        double diameter = planeToCanvas.deltaTransform(diameter1, 0).getX();

        Color yellow = Color.YELLOW;
        Color haloColor = yellow.deriveColor(yellow.getHue(), yellow.getSaturation(), yellow.getBrightness(), 0.25);
        ctx.setFill(haloColor);
        ctx.fillOval(x - diameter * 1.1, y - diameter * 1.1, diameter * 2.2, diameter * 2.2);

        ctx.setFill(Color.YELLOW);
        ctx.fillOval(x - (diameter + 2) / 2, y - (diameter + 2) / 2, diameter + 2, diameter + 2);
        ctx.fillText("Sun", x + diameter / 2, y + diameter / 2);

        ctx.setFill(Color.WHITE);
        ctx.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
    }

    /**
     * draw the Moon
     *
     * @param sky           observed sky
     * @param projection    used stereographic projection
     * @param planeToCanvas transformation from plane to canvas
     */
    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
//        CartesianCoordinates moonPos = sky.moonPosition();
//        Point2D center = planeToCanvas.transform(moonPos.x(),moonPos.y());
//
//        double diameter1 = projection.applyToAngle(sky.moon().angularSize());
//        double diameter = planeToCanvas.deltaTransform(diameter1,0).getX();
//        ctx.setFill(Color.WHITE);
//
//        ctx.fillOval(center.getX()-diameter/2, center.getY()-diameter/2, diameter, diameter);
//
//        ctx.fillText("Moon", center.getX()+diameter/2, center.getY()+diameter/2);
    }

    /**
     * draw the phase of the moon
     *
     * @param sky           observed sky
     * @param projection    stereographic projection
     * @param planeToCanvas transform from plane to canvas
     */
    public void drawMoonPhase(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas, double latDeg) {
        CartesianCoordinates moonPos = sky.moonPosition();
        Point2D center = planeToCanvas.transform(moonPos.x(), moonPos.y());

        double x = center.getX();
        double y = center.getY();

        double diameter1 = projection.applyToAngle(sky.moon().angularSize());
        double diameter = planeToCanvas.deltaTransform(diameter1, 0).getX();

        String phase = sky.moon().getMoonPhase();

        if ((phase.equals("waxing crescent") && latDeg > 0) ||
                (phase.equals("waning crescent") && latDeg < 0)) {
            drawQuarter(x, y, diameter, -1);
            drawCrescentOrGibbous(x, y, diameter, Color.BLACK);
        } else if ((phase.equals("first quarter") && latDeg > 0) ||
                (phase.equals("last quarter") && latDeg < 0)) {
            drawQuarter(x, y, diameter, -1);
        } else if ((phase.equals("waxing gibbous") && latDeg > 0) ||
                (phase.equals("waning gibbous") && latDeg < 0)) {
            drawQuarter(x, y, diameter, -1);
            drawCrescentOrGibbous(x, y, diameter, Color.WHITE);
        } else if (phase.equals("full moon")) {
            ctx.setFill(Color.WHITE);
            ctx.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
        } else if ((phase.equals("waning gibbous") && latDeg > 0) ||
                (phase.equals("waxing gibbous") && latDeg < 0)) {
            drawQuarter(x, y, diameter, 1);
            drawCrescentOrGibbous(x, y, diameter, Color.WHITE);
        } else if ((phase.equals("last quarter") && latDeg > 0) ||
                (phase.equals("first quarter") && latDeg < 0)) {
            drawQuarter(x, y, diameter, 1);
        } else if ((phase.equals("waning crescent") && latDeg > 0) ||
                (phase.equals("waxing crescent") && latDeg < 0)) {
            drawQuarter(x, y, diameter, -1);
            drawCrescentOrGibbous(x, y, diameter, Color.BLACK);
        }


        ctx.fillText("Moon", x + diameter / 2, y + diameter / 2);

    }

    private void drawQuarter(double x, double y, double diameter, int sign) {
        //first quarter
        ctx.setFill(Color.WHITE);
        ctx.fillArc(x - diameter / 4, y - diameter / 2, diameter, diameter, 90, sign * 180, ArcType.OPEN);
    }

    private void drawCrescentOrGibbous(double x, double y, double diameter, Color color) {
        //waxing crescent
        ctx.setFill(color);
        ctx.fillOval(x, y - diameter / 2, diameter / 2, diameter);

    }


    /**
     * draw horizon and cardinal points
     *
     * @param sky           observed sky
     * @param projection    stereographic projection
     * @param planeToCanvas transform from plane to canvas
     */
    public void drawHorizon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        ctx.setFill(Color.RED);
        ctx.setStroke(Color.RED);

        //draw horizon
        ctx.setLineWidth(2);
        HorizontalCoordinates hor = HorizontalCoordinates.ofDeg(0, 0);
        CartesianCoordinates cart = projection.circleCenterForParallel(hor);
        Point2D center = planeToCanvas.transform(cart.x(), cart.y());
        double r0 = projection.circleRadiusForParallel(hor);
        double r = planeToCanvas.deltaTransform(r0, 0).getX();
        ctx.strokeOval(center.getX() - r, center.getY() - r, r * 2, r * 2);

        ctx.setTextBaseline(VPos.TOP);
        ctx.setTextAlign(TextAlignment.CENTER);

        // draw cardinal points
        for (int i = 0; i < 8; ++i) {
            HorizontalCoordinates azAltCard = HorizontalCoordinates.ofDeg(i * 45, -0.5);
            String nameOfCard = azAltCard.azOctantName("N", "E", "S", "O");
            CartesianCoordinates coordinatesCard = projection.apply(azAltCard);
            Point2D cardTruePos = planeToCanvas.transform(coordinatesCard.x(), coordinatesCard.y());
            ctx.fillText(nameOfCard, cardTruePos.getX(), cardTruePos.getY());
        }
    }

    private double diameterCal(double magnitude) {
        double mPrime = INTERVAL.clip(magnitude);
        double f = (99 - 17 * mPrime) / 140;
        return f * MAGNITUDE;
    }

    /**
     * apply all the above drawing methods
     *
     * @param oS      observed sky
     * @param project used projection
     * @param pTC     transform from plan to canvas
     */
    public void drawAll(ObservedSky oS, StereographicProjection project, Transform pTC, double latDeg) {
        this.clear();
        this.drawStars(oS, project, pTC);
        this.drawPlanets(oS, project, pTC);
        this.drawSun(oS, project, pTC);
        this.drawMoon(oS, project, pTC);
        this.drawMoonPhase(oS, project, pTC, latDeg);
        this.drawHorizon(oS, project, pTC);
    }
}
