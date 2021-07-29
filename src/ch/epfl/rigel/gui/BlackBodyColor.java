package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.*;

//class offers a function which gives us the color of a black body given its temperature
//by Marin COHU
public class BlackBodyColor {

    private static final int MIN_TEMP = 1000, MAX_TEMP = 40000;
    private final static ClosedInterval COLOR_INTERVAL = ClosedInterval.of(MIN_TEMP, MAX_TEMP);


    /**
     * @param tempInKelvin : temperature in Kelvin of the concerned black body (valid values goes from 1000K to 40 000 included)
     * @return an object of type Color that is corresponding to the given temperature in kelvin
     */
    public static Color colorForTemperature(int tempInKelvin) {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                BlackBodyColor.class.getResourceAsStream("/bbr_color.txt")))) {

            int roundedGivenTemp = (int) Preconditions.checkInInterval(COLOR_INTERVAL, tempInKelvin);

            //checking if the given temp is a multiple of 100
            if (tempInKelvin % 100 != 0) {
                roundedGivenTemp = (int) Math.round(tempInKelvin / 100.0) * 100;
            }

            //skipping the 20 first lines (all of the # starting lines and the first 2deg)
            for (int i = 0; i < 20; ++i) {
                in.readLine();
            }

            int nbrOfLinesToSkip = (roundedGivenTemp - 1000) / 100;
            int CHAR_PER_LGN = 88;
            int charSkipped = nbrOfLinesToSkip * CHAR_PER_LGN * 2;

            in.skip(charSkipped);

            return Color.web(in.readLine().substring(80, 87));

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }
}
