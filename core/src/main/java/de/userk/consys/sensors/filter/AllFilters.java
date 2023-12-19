package de.userk.consys.sensors.filter;

import java.util.ArrayList;
import java.util.List;

public class AllFilters implements ValueFiltersFactory {
    @Override
    public List<ValueFilter> getFilters() {
        List<ValueFilter> filters = new ArrayList<>();

        filters.add(new ErrorValueFilter());
        // filters.add(new ExtrapolatingFilter());
        // filters.add(new BigJumpsValueFilter());

        return filters;
    }
}
