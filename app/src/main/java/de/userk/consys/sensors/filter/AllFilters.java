package de.userk.consys.sensors.filter;

import java.util.Arrays;
import java.util.List;

public class AllFilters implements ValueFiltersFactory {
    @Override
    public List<ValueFilter> getFilters() {
        return Arrays.asList(new ErrorValueFilter(), new BigJumpsValueFilter());
    }
}
