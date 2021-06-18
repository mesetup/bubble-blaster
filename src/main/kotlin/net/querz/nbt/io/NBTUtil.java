package net.querz.nbt.io;

import net.querz.nbt.tag.Tag;

import java.io.*;
import java.util.zip.GZIPInputStream;

public final class NBTUtil {

    private NBTUtil() {
    }

    public static void write(net.querz.nbt.io.NamedTag tag, File file, boolean compressed) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            new net.querz.nbt.io.NBTSerializer(compressed).toStream(tag, fos);
        }
    }

    public static void write(net.querz.nbt.io.NamedTag tag, OutputStream out, boolean compressed) throws IOException {
        new net.querz.nbt.io.NBTSerializer(compressed).toStream(tag, out);
    }

    public static void write(net.querz.nbt.io.NamedTag tag, String file, boolean compressed) throws IOException {
        write(tag, new File(file), compressed);
    }

    public static void write(net.querz.nbt.io.NamedTag tag, File file) throws IOException {
        write(tag, file, true);
    }

    public static void write(net.querz.nbt.io.NamedTag tag, OutputStream out) throws IOException {
        write(tag, out, true);
    }

    public static void write(net.querz.nbt.io.NamedTag tag, String file) throws IOException {
        write(tag, new File(file), true);
    }

    public static void write(Tag<?> tag, File file, boolean compressed) throws IOException {
        write(new net.querz.nbt.io.NamedTag(null, tag), file, compressed);
    }

    public static void write(Tag<?> tag, OutputStream out, boolean compressed) throws IOException {
        write(new net.querz.nbt.io.NamedTag(null, tag), out, compressed);
    }

    public static void write(Tag<?> tag, String file, boolean compressed) throws IOException {
        write(new net.querz.nbt.io.NamedTag(null, tag), new File(file), compressed);
    }

    public static void write(Tag<?> tag, File file) throws IOException {
        write(new net.querz.nbt.io.NamedTag(null, tag), file, true);
    }

    public static void write(Tag<?> tag, OutputStream out) throws IOException {
        write(new net.querz.nbt.io.NamedTag(null, tag), out, true);
    }

    public static void write(Tag<?> tag, String file) throws IOException {
        write(new net.querz.nbt.io.NamedTag(null, tag), new File(file), true);
    }

    public static void writeLE(net.querz.nbt.io.NamedTag tag, File file, boolean compressed) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            new net.querz.nbt.io.NBTSerializer(compressed, true).toStream(tag, fos);
        }
    }

    public static void writeLE(net.querz.nbt.io.NamedTag tag, OutputStream out, boolean compressed) throws IOException {
        new NBTSerializer(compressed, true).toStream(tag, out);
    }

    public static void writeLE(net.querz.nbt.io.NamedTag tag, String file, boolean compressed) throws IOException {
        writeLE(tag, new File(file), compressed);
    }

    public static void writeLE(net.querz.nbt.io.NamedTag tag, File file) throws IOException {
        writeLE(tag, file, true);
    }

    public static void writeLE(net.querz.nbt.io.NamedTag tag, OutputStream out) throws IOException {
        writeLE(tag, out, true);
    }

    public static void writeLE(net.querz.nbt.io.NamedTag tag, String file) throws IOException {
        writeLE(tag, new File(file), true);
    }

    public static void writeLE(Tag<?> tag, File file, boolean compressed) throws IOException {
        writeLE(new net.querz.nbt.io.NamedTag(null, tag), file, compressed);
    }

    public static void writeLE(Tag<?> tag, OutputStream out, boolean compressed) throws IOException {
        writeLE(new net.querz.nbt.io.NamedTag(null, tag), out, compressed);
    }

    public static void writeLE(Tag<?> tag, String file, boolean compressed) throws IOException {
        writeLE(new net.querz.nbt.io.NamedTag(null, tag), new File(file), compressed);
    }

    public static void writeLE(Tag<?> tag, File file) throws IOException {
        writeLE(new net.querz.nbt.io.NamedTag(null, tag), file, true);
    }

    public static void writeLE(Tag<?> tag, OutputStream out) throws IOException {
        writeLE(new net.querz.nbt.io.NamedTag(null, tag), out, true);
    }

    public static void writeLE(Tag<?> tag, String file) throws IOException {
        writeLE(new net.querz.nbt.io.NamedTag(null, tag), new File(file), true);
    }

    public static net.querz.nbt.io.NamedTag read(File file, boolean compressed) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return new net.querz.nbt.io.NBTDeserializer(compressed).fromStream(fis);
        }
    }

    public static net.querz.nbt.io.NamedTag read(InputStream in, boolean compressed) throws IOException {
        return new net.querz.nbt.io.NBTDeserializer(compressed).fromStream(in);
    }

    public static net.querz.nbt.io.NamedTag read(String file, boolean compressed) throws IOException {
        return read(new File(file), compressed);
    }

    public static net.querz.nbt.io.NamedTag read(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return new net.querz.nbt.io.NBTDeserializer(false).fromStream(detectDecompression(fis));
        }
    }

    public static net.querz.nbt.io.NamedTag read(InputStream in) throws IOException {
        return new net.querz.nbt.io.NBTDeserializer(false).fromStream(detectDecompression(in));
    }

    public static net.querz.nbt.io.NamedTag read(String file) throws IOException {
        return read(new File(file));
    }

    public static net.querz.nbt.io.NamedTag readLE(File file, boolean compressed) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return new net.querz.nbt.io.NBTDeserializer(compressed, true).fromStream(fis);
        }
    }

    public static net.querz.nbt.io.NamedTag readLE(InputStream in, boolean compressed) throws IOException {
        return new net.querz.nbt.io.NBTDeserializer(compressed, true).fromStream(in);
    }

    public static net.querz.nbt.io.NamedTag readLE(String file, boolean compressed) throws IOException {
        return readLE(new File(file), compressed);
    }

    public static net.querz.nbt.io.NamedTag readLE(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return new net.querz.nbt.io.NBTDeserializer(false, true).fromStream(detectDecompression(fis));
        }
    }

    public static net.querz.nbt.io.NamedTag readLE(InputStream in) throws IOException {
        return new NBTDeserializer(false, true).fromStream(detectDecompression(in));
    }

    public static NamedTag readLE(String file) throws IOException {
        return readLE(new File(file));
    }

    private static InputStream detectDecompression(InputStream is) throws IOException {
        PushbackInputStream pbis = new PushbackInputStream(is, 2);
        int signature = (pbis.read() & 0xFF) + (pbis.read() << 8);
        pbis.unread(signature >> 8);
        pbis.unread(signature & 0xFF);
        if (signature == GZIPInputStream.GZIP_MAGIC) {
            return new GZIPInputStream(pbis);
        }
        return pbis;
    }
}
