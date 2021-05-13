package com.qsoftware.bubbles.common.streams;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class CustomOutputStream extends OutputStream {
    private final String name;
    private final Level level;
    private final List<Character> characters = new ArrayList<>();

    public CustomOutputStream(String name, Level level) {
        this.name = name;
        this.level = level;
    }

    public final void write(int b) {
        // the correct way of doing this would be using a buffer
        // to store characters until a newline is encountered,
        // this implementation is for illustration only
        if ((char) b == '\n') {
            // create object of StringBuilder class
            StringBuilder sb = new StringBuilder();

            // Appends characters one by one
            for (Character ch : characters) {
                sb.append(ch);
            }

            // convert in string
            String string = sb.toString();

            characters.clear();

            // print string
            LogManager.getLogger(name).log(level, string);
        } else {
            characters.add((char) b);
        }
    }
}
