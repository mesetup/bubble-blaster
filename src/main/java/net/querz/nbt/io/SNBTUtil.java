package net.querz.nbt.io;

import net.querz.nbt.tag.Tag;

import java.io.IOException;

public class SNBTUtil {

    public static String toSNBT(net.querz.nbt.tag.Tag<?> tag) throws IOException {
        return new SNBTSerializer().toString(tag);
    }

    public static net.querz.nbt.tag.Tag<?> fromSNBT(String string) throws IOException {
        return new SNBTDeserializer().fromString(string);
    }

    public static net.querz.nbt.tag.Tag<?> fromSNBT(String string, boolean lenient) throws IOException {
        return new SNBTParser(string).parse(Tag.DEFAULT_MAX_DEPTH, lenient);
    }
}
