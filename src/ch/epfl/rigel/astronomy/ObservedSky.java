package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

//represents a set of celestial objects projected in the plane by a stereographic projection
//by Jiabao wen
public class ObservedSky {

    private final StereographicProjection projection;
    private final StarCatalogue catalogue;
    private final Sun sun;
    private final Moon moon;
    private final EquatorialToHorizontalConversion equatorialToHorizontalConversion;
    private final List<Planet> planets;
    private final double[] planetsPositions;
    private final double[] starsPositions;

    /**
     * @param when       the moment of observation
     * @param position   the position of observation
     * @param projection used stereographic projection
     * @param catalogue  star catalogue
     */
    public ObservedSky(ZonedDateTime when, GeographicCoordinates position, StereographicProjection projection, StarCatalogue catalogue) {
        this.projection = projection;
        this.catalogue = catalogue;

        double daySinceJ2010 = Epoch.J2010.daysUntil(when);
        EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(when);
        equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(when, position);

        this.sun = SunModel.SUN.at(daySinceJ2010, eclipticToEquatorialConversion);
        this.moon = MoonModel.MOON.at(daySinceJ2010, eclipticToEquatorialConversion);

        var planettempo = new ArrayList<Planet>();
        for (PlanetModel planetModel : PlanetModel.ALL) {
            if (planetModel != PlanetModel.EARTH) {
                planettempo.add(planetModel.at(daySinceJ2010, eclipticToEquatorialConversion));
            }
        }
        planets = Collections.unmodifiableList(planettempo);

        planetsPositions = ObjectPositions(planets());
        starsPositions = ObjectPositions(stars());
    }

    private CartesianCoordinates equaToCartesianConvension(CelestialObject O) {
        HorizontalCoordinates horizontalCoordinates = equatorialToHorizontalConversion.apply(O.equatorialPos());
        return projection.apply(horizontalCoordinates);
    }

    /**
     * @return sun
     */
    public Sun sun() {
        return sun;
    }

    /**
     * @return the Sun's position
     */
    public CartesianCoordinates sunPosition() {
        return equaToCartesianConvension(sun);
    }

    /**
     * @return moon
     */
    public Moon moon() {
        return moon;
    }

    /**
     * @return the Moon's position
     */
    public CartesianCoordinates moonPosition() {
        return equaToCartesianConvension(moon);
    }

    private double[] ObjectPositions(List<? extends CelestialObject> Os) {
        List<CartesianCoordinates> CartCoor = new ArrayList<>();
        for (CelestialObject O : Os) {
            CartCoor.add(equaToCartesianConvension(O));
        }

        List<Double> PositionsList = new ArrayList<>();
        for (CartesianCoordinates cartesianCoordinates : CartCoor) {
            PositionsList.add(cartesianCoordinates.x());
            PositionsList.add(cartesianCoordinates.y());
        }

        int length = 2 * Os.size();
        double[] positions = new double[length];
        for (int i = 0; i < length; i++) {
            positions[i] = PositionsList.get(i);
        }
        return positions;
    }

    /**
     * @return a list of the planets
     */
    public List<Planet> planets() {
        return Collections.unmodifiableList(planets);
    }

    /**
     * @return cartesian coordinates of the planets
     */
    public double[] planetPositions() {
        return Arrays.copyOf(planetsPositions, planets().size() * 2);
    }

    /**
     * @return list of stars
     */
    public List<Star> stars() {
        return Collections.unmodifiableList(catalogue.stars());
    }

    /**
     * @return cartesian coordinates of the stars
     */
    public double[] starPositions() {
        return Arrays.copyOf(starsPositions, stars().size() * 2);
    }

    /**
     * @return set of asterisms in the catalogue
     */
    public Set<Asterism> asterisms() {
        return Collections.unmodifiableSet(catalogue.asterisms());
    }

    /**
     * @param asterism a given asterism
     * @return the list of index in the catalogue of the stars which construct asterism
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        return Collections.unmodifiableList(catalogue.asterismIndices(asterism));
    }

    /**
     * @param coordinates a given point
     * @param distanceMax maximum distance
     * @return the closest celestial object to the coordinates
     * where the distance is smaller than distanceMax
     */
    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates coordinates, double distanceMax) {
        CelestialObject closestObject = null;
        double distanceMin = 0;

        double distanceSun = distanceCal(sunPosition(), coordinates);
        double distanceMoon = distanceCal(moonPosition(), coordinates);
        if (distanceMoon < distanceSun) {
            distanceMin = distanceMoon;
            closestObject = this.moon;
        } else {
            distanceMin = distanceSun;
            closestObject = this.sun;
        }

        for (int i = 0; i < planetsPositions.length; i += 2) {
            double distancePlanet = distanceCal(coordinates, planetsPositions[i], planetsPositions[i + 1]);
            if (distancePlanet < distanceMin) {
                distanceMin = distancePlanet;
                closestObject = planets().get(i / 2);
            }
        }
        for (int i = 0; i < starsPositions.length; i += 2) {
            double distanceStar = distanceCal(coordinates, starsPositions[i], starsPositions[i + 1]);
            if (distanceStar < distanceMin) {
                distanceMin = distanceStar;
                closestObject = stars().get(i / 2);
            }
        }

        if (distanceMin <= distanceMax) {
            return Optional.of(closestObject);
        }
        return Optional.empty();
    }

    private double distanceCal(CartesianCoordinates w1, CartesianCoordinates w2) {
        double x1 = w1.x();
        double x2 = w2.x();
        double y1 = w1.y();
        double y2 = w2.y();
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    private double distanceCal(CartesianCoordinates w, double x2, double y2) {
        double x1 = w.x();
        double y1 = w.y();
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }


}

