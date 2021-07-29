package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// represent an asterism
// by Marin Cohu
public final class Asterism {

    private final List<Star> asterism;

    /**
     * @param stars list of stars which forms the asterism
     * @throws IllegalArgumentException if stars is empty
     */
    public Asterism(List<Star> stars) {
        Preconditions.checkArgument(!stars.isEmpty());
        asterism = List.copyOf(stars);
    }

    /**
     * @return the list of stars that forms an asterism (also known as constellation)
     */
    public List<Star> stars() {
        return asterism;
    }
}