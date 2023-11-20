package de.userk.consys.sensors;

import de.userk.log.Logger;

/**
 * A SensorFilterAdapter is a Sensor which wraps another Sensor and sanitizes
 * its values.
 */
public class SensorFilterAdapter implements Sensor {
    private static final Logger log = Logger.forClass(SensorFilterAdapter.class);
    private final Sensor unfiltered;
    private long lastValueTimestamp;
    private int lastValue = 255;

    public SensorFilterAdapter(Sensor unfiltered) {
        this.unfiltered = unfiltered;
    }

    @Override
    public void registerObserver(SensorObserver observer) {
        log.info("registering observer %s to sensor %s", observer, unfiltered);
        unfiltered.registerObserver(value -> {
            if (errorValue(value) || jumpSinceLastTooBig(value)) {
                return;
            }

            observer.newValue(value);
        });
    }

    /**
     * Returns true if the value is an error.
     */
    private boolean errorValue(int value) {
        boolean isError = value == 255;
        if (isError) {
            log.debug("detected error value 255");
        }
        return value == 255;
    }

    /**
     * Returns true if the value is probably an error because the jump from the last
     * value is too big.
     */
    private boolean jumpSinceLastTooBig(int value) {
        long now = System.currentTimeMillis();
        double secondsSinceLastValue = (now - lastValueTimestamp) / 1000.0;
        int absoluteCentimetersDiff = Math.abs(value - lastValue);
        double centimetersPerSecond = absoluteCentimetersDiff / secondsSinceLastValue;
        log.debug("%f cm/s", centimetersPerSecond);
        if (centimetersPerSecond > 10) {
            log.debug("recognized value jump: %f cm/s", centimetersPerSecond);
            return true;
        } else {
            lastValue = value;
            lastValueTimestamp = now;
            return false;
        }
    }
}
