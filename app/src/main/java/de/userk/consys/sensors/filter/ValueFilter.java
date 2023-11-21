package de.userk.consys.sensors.filter;

import java.util.Optional;

public interface ValueFilter {
    Optional<Integer> filter(int value);
}
