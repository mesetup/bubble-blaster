package com.qtech.bubbles.common.streams

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.io.OutputStream

class CustomOutputStream(private val name: String, private val level: Level) : OutputStream() {
    private val characters: MutableList<Char> = ArrayList()
    override fun write(b: Int) {
        // the correct way of doing this would be using a buffer
        // to store characters until a newline is encountered,
        // this implementation is for illustration only
        if (b.toChar() == '\n') {
            // create object of StringBuilder class
            val sb = StringBuilder()

            // Appends characters one by one
            for (ch in characters) {
                sb.append(ch)
            }

            // convert in string
            val string = sb.toString()
            characters.clear()

            // print string
            LogManager.getLogger(name).log(level, string)
        } else {
            characters.add(b.toChar())
        }
    }
}