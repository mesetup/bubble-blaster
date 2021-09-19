package com.ultreon.hydro.event;

import java.awt.image.BufferedImageOp;
import java.util.ArrayList;

public class FilterEvent extends Event {
    private final ArrayList<BufferedImageOp> filters = new ArrayList<>();

    public FilterEvent() {

    }

    public void addFilter(BufferedImageOp filter) {
        this.filters.add(filter);
    }

    public ArrayList<BufferedImageOp> getFilters() {
        return filters;
    }
}
