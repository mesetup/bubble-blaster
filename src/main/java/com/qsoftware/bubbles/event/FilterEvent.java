package com.qsoftware.bubbles.event;

import java.util.ArrayList;

public class FilterEvent extends Event {
    private final ArrayList<Object> filters = new ArrayList<>();

    public FilterEvent() {

    }

    public void addFilter(Object filter) {
        this.filters.add(filter);
    }

    public ArrayList<Object> getFilters() {
        return filters;
    }
}
