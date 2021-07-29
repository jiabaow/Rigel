package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

//a bean javaFX contains the moment of observation(date, time, time zone)
//by Jiabao wen & Marin COHU
public final class DateTimeBean {
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>();
    private final ObjectProperty<ZoneId> zone = new SimpleObjectProperty<>();

    //date

    /**
     * @return observation's date property
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    /**
     * @return observation's date in localDate
     */
    public LocalDate getDate() {
        return date.get();
    }

    /**
     * @param newDate in LocalDate
     *                set the date of observation to given date
     */
    public void setDate(LocalDate newDate) {
        this.date.set(newDate);
    }


    //time

    /**
     * @return observation's time property
     */
    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    /**
     * @return observation's time in localTime
     */
    public LocalTime getTime() {
        return time.get();
    }

    /**
     * @param newTime in localTime
     *                set the observation's time to the given time
     */
    public void setTime(LocalTime newTime) {
        this.time.set(newTime);
    }


    //zone

    /**
     * @return observation's time zone property
     */
    public ObjectProperty<ZoneId> zoneProperty() {
        return zone;
    }

    /**
     * @return observation's time zone in zoneId
     */
    public ZoneId getZone() {
        return zone.get();
    }

    /**
     * @param newZone in zoneId
     *                set observation's time zone to the given zone
     */
    public void setZone(ZoneId newZone) {
        this.zone.set(newZone);
    }

    /**
     * @return moment of the observation in zonedDateTime
     */
    public ZonedDateTime getZonedDateTime() {

        return ZonedDateTime.of(this.getDate(), this.getTime(), this.getZone());
    }

    /**
     * set zoned date time with given zoned date time
     *
     * @param zdt zoned date time
     */
    public void setZonedDateTime(ZonedDateTime zdt) {
        LocalDate d = zdt.toLocalDate();
        LocalTime t = zdt.toLocalTime();
        ZoneId z = zdt.getZone();
        this.setDate(d);
        this.setTime(t);
        this.setZone(z);
    }


}
