package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;

import java.time.ZonedDateTime;

//represents a time animator
//by Jiabao WEN
public final class TimeAnimator extends AnimationTimer {

    private boolean isRunning;
    private boolean fstStart;
    private long startTime = 0;
    private ZonedDateTime T0;
    private final DateTimeBean dateTimeB;
    private final ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>();
    private final ReadOnlyBooleanProperty running = new SimpleBooleanProperty();

    /**
     * @param dateTimeB date time bean
     */
    public TimeAnimator(DateTimeBean dateTimeB) {
        this.dateTimeB = dateTimeB;
    }

    /**
     * @return time's accelerator property
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return accelerator;
    }

    /**
     * @return accelerator
     */
    public TimeAccelerator getAccelerator() {
        return acceleratorProperty().get();
    }

    /**
     * @param newAccelerator change accelerator to the new accelerator
     */
    public void setAccelerator(TimeAccelerator newAccelerator) {
        this.acceleratorProperty().set(newAccelerator);
    }

    /**
     * @return animator's status property
     */
    public ReadOnlyBooleanProperty runningProperty() {
        return running;
    }


    /**
     * @see AnimationTimer#start()
     */
    @Override
    public void start() {
        super.start();
        isRunning = true;
        fstStart = true;
    }

    /**
     * @see AnimationTimer#stop()
     */
    @Override
    public void stop() {
        super.stop();
        isRunning = false;
    }

    /**
     * This method needs to be overridden by extending classes. It is going to
     * be called in every frame while the {@code AnimationTimer} is active.
     *
     * @param now The timestamp of the current frame given in nanoseconds. This
     *            value will be the same for all {@code AnimationTimers} called
     *            during one frame.
     */
    @Override
    public void handle(long now) {
        if (fstStart) {
            fstStart = false;
            startTime = now;
            T0 = dateTimeB.getZonedDateTime();
        } else {
            long deltaT = now - startTime;
            dateTimeB.setZonedDateTime(getAccelerator().adjust(T0, deltaT));
        }


    }

}
