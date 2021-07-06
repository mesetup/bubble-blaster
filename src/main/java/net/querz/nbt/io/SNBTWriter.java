package net.querz.nbt.io;

import net.querz.nbt.tag.CompoundTag;
import net.querz.io.MaxDepthIO;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * SNBTWriter creates an SNBT String.
 */
public final class SNBTWriter implements MaxDepthIO {

    private static final Pattern NON_QUOTE_PATTERN = Pattern.compile("[a-zA-Z_.+\\-]+");

    private final Writer writer;

    private SNBTWriter(Writer writer) {
        this.writer = writer;
    }

    public static void write(net.querz.nbt.tag.Tag<?> tag, Writer writer, int maxDepth) throws IOException {
        new SNBTWriter(writer).writeAnything(tag, maxDepth);
    }

    public static void write(net.querz.nbt.tag.Tag<?> tag, Writer writer) throws IOException {
        write(tag, writer, net.querz.nbt.tag.Tag.DEFAULT_MAX_DEPTH);
    }

    private void writeAnything(net.querz.nbt.tag.Tag<?> tag, int maxDepth) throws IOException {
        switch (tag.getID()) {
            case net.querz.nbt.tag.EndTag.ID:
                //do nothing
                break;
            case net.querz.nbt.tag.ByteTag.ID:
                writer.append(Byte.toString(((net.querz.nbt.tag.ByteTag) tag).asByte())).write('b');
                break;
            case net.querz.nbt.tag.ShortTag.ID:
                writer.append(Short.toString(((net.querz.nbt.tag.ShortTag) tag).asShort())).write('s');
                break;
            case net.querz.nbt.tag.IntTag.ID:
                writer.write(Integer.toString(((net.querz.nbt.tag.IntTag) tag).asInt()));
                break;
            case net.querz.nbt.tag.LongTag.ID:
                writer.append(Long.toString(((net.querz.nbt.tag.LongTag) tag).asLong())).write('l');
                break;
            case net.querz.nbt.tag.FloatTag.ID:
                writer.append(Float.toString(((net.querz.nbt.tag.FloatTag) tag).asFloat())).write('f');
                break;
            case net.querz.nbt.tag.DoubleTag.ID:
                writer.append(Double.toString(((net.querz.nbt.tag.DoubleTag) tag).asDouble())).write('d');
                break;
            case net.querz.nbt.tag.ByteArrayTag.ID:
                writeArray(((net.querz.nbt.tag.ByteArrayTag) tag).getValue(), ((net.querz.nbt.tag.ByteArrayTag) tag).length(), "B");
                break;
            case net.querz.nbt.tag.StringTag.ID:
                writer.write(escapeString(((net.querz.nbt.tag.StringTag) tag).getValue()));
                break;
            case net.querz.nbt.tag.ListTag.ID:
                writer.write('[');
                for (int i = 0; i < ((net.querz.nbt.tag.ListTag<?>) tag).size(); i++) {
                    writer.write(i == 0 ? "" : ",");
                    writeAnything(((net.querz.nbt.tag.ListTag<?>) tag).get(i), decrementMaxDepth(maxDepth));
                }
                writer.write(']');
                break;
            case CompoundTag.ID:
                writer.write('{');
                boolean first = true;
                for (Map.Entry<String, net.querz.nbt.tag.Tag<?>> entry : (CompoundTag) tag) {
                    writer.write(first ? "" : ",");
                    writer.append(escapeString(entry.getKey())).write(':');
                    writeAnything(entry.getValue(), decrementMaxDepth(maxDepth));
                    first = false;
                }
                writer.write('}');
                break;
            case net.querz.nbt.tag.IntArrayTag.ID:
                writeArray(((net.querz.nbt.tag.IntArrayTag) tag).getValue(), ((net.querz.nbt.tag.IntArrayTag) tag).length(), "I");
                break;
            case net.querz.nbt.tag.LongArrayTag.ID:
                writeArray(((net.querz.nbt.tag.LongArrayTag) tag).getValue(), ((net.querz.nbt.tag.LongArrayTag) tag).length(), "L");
                break;
            default:
                throw new IOException("unknown tag with id \"" + tag.getID() + "\"");
        }
    }

    private void writeArray(Object array, int length, String prefix) throws IOException {
        writer.append('[').append(prefix).write(';');
        for (int i = 0; i < length; i++) {
            writer.append(i == 0 ? "" : ",").write(Array.get(array, i).toString());
        }
        writer.write(']');
    }

    public static String escapeString(String s) {
        if (!NON_QUOTE_PATTERN.matcher(s).matches()) {
            StringBuilder sb = new StringBuilder();
            sb.append('"');
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '\\' || c == '"') {
                    sb.append('\\');
                }
                sb.append(c);
            }
            sb.append('"');
            return sb.toString();
        }
        return s;
    }
}
