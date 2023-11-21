package de.userk.consys.sensors.filter;

import java.util.List;
import java.util.Optional;

import de.userk.consys.sensors.Sensor;
import de.userk.consys.sensors.SensorObserver;
import de.userk.log.Logger;

/**
 * A SensorFilterAdapter is a Sensor which wraps another Sensor and sanitizes
 * its values.
 */
public class SensorFilterAdapter implements Sensor {
    private static final Logger log = Logger.forClass(SensorFilterAdapter.class);
    private final Sensor unfiltered;
    private final List<ValueFilter> filters;

    public SensorFilterAdapter(Sensor unfiltered, List<ValueFilter> filters) {
        this.unfiltered = unfiltered;
        this.filters = filters;
    }

    public SensorFilterAdapter(Sensor unfiltered, ValueFiltersFactory filtersFactory) {
        this(unfiltered, filtersFactory.getFilters());
    }

    @Override
    public void registerObserver(SensorObserver observer) {
        log.info("registering observer %s to sensor %s", observer, unfiltered);
        unfiltered.registerObserver(value -> {
            Optional<Integer> filteredValue = Optional.of(value);
            for (int i = 0; i < filters.size() && filteredValue.isPresent(); i++) {
                filteredValue = filters.get(i).filter(filteredValue.get());
            }

            if (filteredValue.isPresent()) {
                observer.newValue(filteredValue.get());
            }
        });
    }
}
