package de.userk.consys.sensors.filter;

import de.userk.log.Logger;

public class BigJumpsValueFilter implements ValueFilter {
    private static final Logger log = Logger.forClass(BigJumpsValueFilter.class);
    private int lastValue = 255;
    private long lastValueTimestamp;

    @Override
    public Integer filter(int value) {
        long now = System.currentTimeMillis();
        double secondsSinceLastValue = (now - lastValueTimestamp) / 1000.0;
        int absoluteCentimetersDiff = Math.abs(value - lastValue);
        double centimetersPerSecond = absoluteCentimetersDiff / secondsSinceLastValue;
        log.debug("%f cm/s", centimetersPerSecond);
        if (centimetersPerSecond > 20.0) {
            log.debug("recognized big jump %f cm/s", centimetersPerSecond);
            return null;
        } else {
            lastValue = value;
            lastValueTimestamp = now;
            return value;
        }
    }
}
