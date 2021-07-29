package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.*;
import java.util.*;

//represents a catalogue of stars and asterisms
//by Jiabao Wen
public final class StarCatalogue {
    private final List<Star> stars;
    private final Map<Asterism, List<Integer>> starCat;

    /**
     * @param stars     a list of star
     * @param asterisms a list of asterism
     * @throws IllegalArgumentException if one of the asterism doesn't belong to the stars
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        for (Asterism asterism : asterisms) {
            Preconditions.checkArgument(stars.containsAll(asterism.stars()));
        }
        this.stars = List.copyOf(stars);

        Map<Star, Integer> starInt = new HashMap<>();

        for (int i = 0; i < stars.size(); i++) {
            starInt.put(stars.get(i), i);
        }

        Map<Asterism, List<Integer>> starCatTempo = new HashMap<>();

        for (Asterism asterism : asterisms) {
            List<Integer> index = new ArrayList<>();
            for (Star star : asterism.stars()) {
                index.add(starInt.get(star));
            }
            starCatTempo.put(asterism, Collections.unmodifiableList(index));
        }
        starCat = Collections.unmodifiableMap(starCatTempo);

    }

    /**
     * @return the list of stars of the catalogue
     */
    public List<Star> stars() {
        return stars;
    }

    /**
     * @return the set of asterisms of the catalogue
     */
    public Set<Asterism> asterisms() {
        return starCat.keySet();
    }

    /**
     * @param asterism a given asterism
     * @return the list of index in the catalogue of the stars which construct asterism
     * @throws IllegalArgumentException if asterism doesn't belong to the catalogue
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        Preconditions.checkArgument(starCat.containsKey(asterism));
        return starCat.get(asterism);
    }

    //static class creats a builder of starCatalogue
    //by Jiabao WEN
    static public final class Builder {
        private final List<Star> stars = new ArrayList<>();
        private final List<Asterism> asterisms = new ArrayList<>();

        /**
         * @param star a star
         * @return the builder with the adding star
         */
        public Builder addStar(Star star) {
            this.stars.add(star);
            return this;
        }

        /**
         * @return a view of the list of stars
         */
        public List<Star> stars() {
            return Collections.unmodifiableList(this.stars);
        }

        /**
         * @param asterism an asterism
         * @return the builder with the adding asterism
         */
        public Builder addAsterism(Asterism asterism) {
            this.asterisms.add(asterism);
            return this;
        }

        /**
         * @return a view of the list of asterisms
         */
        public List<Asterism> asterisms() {
            return Collections.unmodifiableList(this.asterisms);
        }

        /**
         * @param inputStream input stream
         * @param loader      loader
         * @return the builder
         * @throws IOException if error happens
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        /**
         * @return the catalogue with all the adding stars and asterisms
         */
        public StarCatalogue build() {
            return new StarCatalogue(stars(), asterisms());
        }


    }

    //Interface represents a loader of starCatalogue
    //by Jiabao WEN
    public interface Loader {
        /**
         * @param inputStream inputStream
         * @param builder     a star catalogue builder
         */
        void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException;
    }


}
