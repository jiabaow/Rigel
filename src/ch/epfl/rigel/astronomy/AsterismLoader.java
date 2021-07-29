package ch.epfl.rigel.astronomy;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

//represents a loader of asterism of starcatalogue
//by Marin Cohu
public enum AsterismLoader implements StarCatalogue.Loader {

    INSTANCE;

    /**
     * @param inputStream inputStream
     * @param builder     of the star catalogue
     * @throws IOException in case of error loading
     * @see StarCatalogue.Loader#load(InputStream, StarCatalogue.Builder)
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {

            ArrayList<String[]> asterismTable = new ArrayList<>();

            String asterismLine;

            while ((asterismLine = in.readLine()) != null) {
                asterismTable.add(asterismLine.split(","));
            }

            HashMap<Integer, Star> hippMap = new HashMap<>();

            for (Star star : builder.stars()) {
                hippMap.put(star.hipparcosId(), star);
            }

            for (String[] asterism : asterismTable) {
                ArrayList<Star> stars = new ArrayList<>();
                for (String starHippId : asterism) {
                    stars.add(hippMap.get(Integer.parseInt(starHippId)));
                }
                builder.addAsterism(new Asterism(stars));
            }

        }


    }
}