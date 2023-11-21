package de.userk.consys.sensors.filter;

import java.util.Optional;

import de.userk.log.Logger;

public class ErrorValueFilter implements ValueFilter {
    private static final Logger log = Logger.forClass(ErrorValueFilter.class);

    @Override
    public Optional<Integer> filter(int value) {
        if (value == 255) {
            log.debug("recognized error value 255");
            return Optional.empty();
        } else
            return Optional.of(value);
    }
}
