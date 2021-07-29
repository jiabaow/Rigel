package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

//represents a loader of HYG catalogue
//by Marin Cohu
public enum HygDatabaseLoader implements StarCatalogue.Loader {

    INSTANCE;

    // index of the concerned columns   &       correspondance             &&        String("")/Number(##)

    private final static int HIP = 1;          // Hipparcos number                              ##
    private final static int PROPER = 6;       // Proper name                                   ""
    private final static int MAGNITUDE = 13;   // Magnitude                                     ##
    private final static int COLOR_INDEX = 16; // B-V color index                               ##
    private final static int RA_RAD = 23;      // Right ascension in radians                    ##
    private final static int DEC_RAD = 24;     // Declination in radians                        ##
    private final static int BAYER = 27;       // Bayer's designation                           ""
    private final static int CON = 29;         // Abbreviated name of the constellation         ""

    private final static RightOpenInterval RIGHT_OPEN_INTERVAL = RightOpenInterval.of(0, Angle.TAU);
    private final static ClosedInterval CLOSED_INTERVAL = ClosedInterval.symmetric(Angle.TAU / 2);

    /**
     * @param inputStream inputStream
     * @param builder     star catalogue's builder
     * @throws IOException if error happens
     * @see StarCatalogue.Loader#load(InputStream, StarCatalogue.Builder)
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {

            ArrayList<String[]> starTable = new ArrayList<>();

            String readFirstLine = in.readLine();
            String starLine;

            while ((starLine = in.readLine()) != null) {
                starTable.add(starLine.split(","));
            }

            int hip;
            String name, bayer;
            double rarad, reducedRarad, decrad, clippedDecrad, magnitude, colorIndex;


            for (String[] star : starTable) {

                hip = !star[HIP].isBlank() ? Integer.parseInt(star[HIP]) : 0;
                bayer = !star[BAYER].isBlank() ? star[BAYER] : "?";
                name = !star[PROPER].isBlank() ? star[PROPER] : bayer + " " + star[CON];
                magnitude = !star[MAGNITUDE].isBlank() ? Double.parseDouble(star[MAGNITUDE]) : 0;
                colorIndex = !star[COLOR_INDEX].isBlank() ? Double.parseDouble(star[COLOR_INDEX]) : 0;


                rarad = Double.parseDouble(star[RA_RAD]);
                reducedRarad = RIGHT_OPEN_INTERVAL.reduce(rarad);
                decrad = Double.parseDouble(star[DEC_RAD]);
                clippedDecrad = CLOSED_INTERVAL.clip(decrad);

                EquatorialCoordinates equ = EquatorialCoordinates.of(reducedRarad, clippedDecrad);

                builder.addStar(new Star(hip, name, equ, (float) magnitude, (float) colorIndex));
            }
            String ok;
        }
    }
}