package com.ultreon.commons.io.filefilters;

import java.io.File;

public class FileFilter implements java.io.FileFilter {
    @Override
    public boolean accept(File pathname) {
        return pathname.isFile();
    }
}
