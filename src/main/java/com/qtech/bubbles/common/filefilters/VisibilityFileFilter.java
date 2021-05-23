package com.qtech.bubbles.common.filefilters;

import java.io.File;
import java.io.FileFilter;

public class VisibilityFileFilter implements FileFilter {
    private final boolean filterVisible;

    public VisibilityFileFilter(boolean filterVisible) {
        this.filterVisible = filterVisible;
    }

    @Override
    public boolean accept(File pathname) {
        if (filterVisible) {
            return !pathname.isHidden();
        } else {
            return pathname.isHidden();
        }
    }
}
