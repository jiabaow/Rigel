package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

//represents a time accelerator - a function calculates simulated time based on real time
//by Jiabao WEN
@FunctionalInterface
public interface TimeAccelerator {

    /**
     * @param T0     initial simulated time
     * @param deltaT = t-t0 : real time since start of the animation ! in nanosecond
     * @return simulated time T
     */
    ZonedDateTime adjust(ZonedDateTime T0, long deltaT);

    /**
     * @param accFact acceleration factor
     * @return a continuous accelerator
     */
    static TimeAccelerator continuous(int accFact) {
        return ((T0, deltaT) -> T0.plus(deltaT * accFact, ChronoUnit.NANOS));

    }

    /**
     * @param freqAdvanc frequency of advancement
     * @param steps      duration
     * @return a discrete accelerator
     */
    static TimeAccelerator discrete(int freqAdvanc, Duration steps) {
        return ((T0, deltaT) -> T0.plus(steps.multipliedBy(freqAdvanc * deltaT / 1000_000_000)));
    }

}
