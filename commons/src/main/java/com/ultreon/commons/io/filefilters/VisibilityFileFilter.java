package com.ultreon.commons.io.filefilters;

import java.io.File;
import java.io.FileFilter;

public record VisibilityFileFilter(boolean filterVisible) implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        return filterVisible != pathname.isHidden();
    }
}
