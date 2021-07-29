package ch.epfl.rigel.gui;

import java.time.Duration;
import java.util.List;

//enumeration contains named accelerators(name, accelerator)
//by Jiabao WEN
public enum NamedTimeAccelerator {
    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000x", TimeAccelerator.continuous(3000)),
    DAY("jour", TimeAccelerator.discrete(60, Duration.ofHours(24))),
    SIDEREAL_DAY("jour sidéral", TimeAccelerator.discrete(60, Duration.ofHours(23).plusMinutes(56).plusSeconds(4)));

    private final String name;
    private final TimeAccelerator accelerator;

    public final static List<NamedTimeAccelerator> ALL = List.of(values());

    /**
     * @param name        accelerator's name
     * @param accelerator accelerator
     */
    NamedTimeAccelerator(String name, TimeAccelerator accelerator) {
        this.name = name;
        this.accelerator = accelerator;
    }

    /**
     * @return named time accelerator's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return named time accelerator's accelerator
     */
    public TimeAccelerator getAccelerator() {
        return accelerator;
    }

    /**
     * @return named time accelerator's name
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }
}
