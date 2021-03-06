package com.ultreon.commons.util;

import java.io.File;
import java.io.PrintWriter;

public class FileUtils {
    @SuppressWarnings("UnusedReturnValue")
    public static boolean setCwd(File directory_name) {
        boolean result = false; // Boolean indicating whether directory was set  

        // Desired current working directory
        File directory = directory_name.getAbsoluteFile();
        if (directory.exists() || directory.mkdirs()) {
            result = (System.setProperty("user.dir", directory.getAbsolutePath()) != null);
        }

        return result;
    }

    public static PrintWriter openOutputFile(String file_name) {
        PrintWriter output = null;  // File to open for writing

        try {
            output = new PrintWriter(new File(file_name).getAbsoluteFile());
        } catch (Exception ignored) {
        }

        return output;
    }
}