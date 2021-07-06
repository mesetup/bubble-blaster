package net.querz.nbt.io;

import net.querz.io.StringDeserializer;
import net.querz.nbt.tag.Tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.stream.Collectors;

public class SNBTDeserializer implements StringDeserializer<net.querz.nbt.tag.Tag<?>> {

    @Override
    public net.querz.nbt.tag.Tag<?> fromReader(Reader reader) throws IOException {
        return fromReader(reader, net.querz.nbt.tag.Tag.DEFAULT_MAX_DEPTH);
    }

    public Tag<?> fromReader(Reader reader, int maxDepth) throws IOException {
        BufferedReader bufferedReader;
        if (reader instanceof BufferedReader) {
            bufferedReader = (BufferedReader) reader;
        } else {
            bufferedReader = new BufferedReader(reader);
        }
        return new SNBTParser(bufferedReader.lines().collect(Collectors.joining())).parse(maxDepth);
    }
}
