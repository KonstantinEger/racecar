package de.userk.consys.sensors.filter;

import de.userk.log.Logger;

public class ErrorValueFilter implements ValueFilter {
    private static final Logger log = Logger.forClass(ErrorValueFilter.class);

    @Override
    public Integer filter(int value) {
        if (value >= 255) {
            log.debug("recognized error value 255");
            return 254;
        } else
            return value;
    }
}
