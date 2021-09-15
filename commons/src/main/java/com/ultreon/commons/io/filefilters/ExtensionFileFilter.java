package com.ultreon.commons.io.filefilters;

import java.io.File;
import java.io.FileFilter;

public class ExtensionFileFilter implements FileFilter {
    private String extension;

    public ExtensionFileFilter(String extension) {
        if (!extension.startsWith(".")) {
            throw new IllegalArgumentException("Extension don't starts with a dot.");
        }
        this.extension = extension;
    }

    @Override
    public boolean accept(File pathname) {
        if (pathname.isFile()) {
            return pathname.getName().endsWith(extension);
        }
        return false;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
