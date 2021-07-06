package net.querz.nbt.io;

import net.querz.io.MaxDepthIO;
import net.querz.nbt.tag.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class SNBTParser implements MaxDepthIO {

    private static final Pattern
            FLOAT_LITERAL_PATTERN = Pattern.compile("^[-+]?(?:\\d+\\.?|\\d*\\.\\d+)(?:e[-+]?\\d+)?f$", Pattern.CASE_INSENSITIVE),
            DOUBLE_LITERAL_PATTERN = Pattern.compile("^[-+]?(?:\\d+\\.?|\\d*\\.\\d+)(?:e[-+]?\\d+)?d$", Pattern.CASE_INSENSITIVE),
            DOUBLE_LITERAL_NO_SUFFIX_PATTERN = Pattern.compile("^[-+]?(?:\\d+\\.|\\d*\\.\\d+)(?:e[-+]?\\d+)?$", Pattern.CASE_INSENSITIVE),
            BYTE_LITERAL_PATTERN = Pattern.compile("^[-+]?\\d+b$", Pattern.CASE_INSENSITIVE),
            SHORT_LITERAL_PATTERN = Pattern.compile("^[-+]?\\d+s$", Pattern.CASE_INSENSITIVE),
            INT_LITERAL_PATTERN = Pattern.compile("^[-+]?\\d+$", Pattern.CASE_INSENSITIVE),
            LONG_LITERAL_PATTERN = Pattern.compile("^[-+]?\\d+l$", Pattern.CASE_INSENSITIVE),
            NUMBER_PATTERN = Pattern.compile("^[-+]?\\d+$");

    private final net.querz.nbt.io.StringPointer ptr;

    public SNBTParser(String string) {
        this.ptr = new StringPointer(string);
    }

    public net.querz.nbt.tag.Tag<?> parse(int maxDepth, boolean lenient) throws net.querz.nbt.io.ParseException {
        net.querz.nbt.tag.Tag<?> tag = parseAnything(maxDepth);
        if (!lenient) {
            ptr.skipWhitespace();
            if (ptr.hasNext()) {
                throw ptr.parseException("invalid characters after end of snbt");
            }
        }
        return tag;
    }

    public net.querz.nbt.tag.Tag<?> parse(int maxDepth) throws net.querz.nbt.io.ParseException {
        return parse(maxDepth, false);
    }

    public net.querz.nbt.tag.Tag<?> parse() throws net.querz.nbt.io.ParseException {
        return parse(net.querz.nbt.tag.Tag.DEFAULT_MAX_DEPTH, false);
    }

    public int getReadChars() {
        return ptr.getIndex() + 1;
    }

    private net.querz.nbt.tag.Tag<?> parseAnything(int maxDepth) throws net.querz.nbt.io.ParseException {
        ptr.skipWhitespace();
        switch (ptr.currentChar()) {
            case '{':
                return parseCompoundTag(maxDepth);
            case '[':
                if (ptr.hasCharsLeft(2) && ptr.lookAhead(1) != '"' && ptr.lookAhead(2) == ';') {
                    return parseNumArray();
                }
                return parseListTag(maxDepth);
        }
        return parseStringOrLiteral();
    }

    private net.querz.nbt.tag.Tag<?> parseStringOrLiteral() throws net.querz.nbt.io.ParseException {
        ptr.skipWhitespace();
        if (ptr.currentChar() == '"') {
            return new net.querz.nbt.tag.StringTag(ptr.parseQuotedString());
        }
        String s = ptr.parseSimpleString();
        if (s.isEmpty()) {
            throw new net.querz.nbt.io.ParseException("expected non empty value");
        }
        if (FLOAT_LITERAL_PATTERN.matcher(s).matches()) {
            return new net.querz.nbt.tag.FloatTag(Float.parseFloat(s.substring(0, s.length() - 1)));
        } else if (BYTE_LITERAL_PATTERN.matcher(s).matches()) {
            try {
                return new net.querz.nbt.tag.ByteTag(Byte.parseByte(s.substring(0, s.length() - 1)));
            } catch (NumberFormatException ex) {
                throw ptr.parseException("byte not in range: \"" + s.substring(0, s.length() - 1) + "\"");
            }
        } else if (SHORT_LITERAL_PATTERN.matcher(s).matches()) {
            try {
                return new net.querz.nbt.tag.ShortTag(Short.parseShort(s.substring(0, s.length() - 1)));
            } catch (NumberFormatException ex) {
                throw ptr.parseException("short not in range: \"" + s.substring(0, s.length() - 1) + "\"");
            }
        } else if (LONG_LITERAL_PATTERN.matcher(s).matches()) {
            try {
                return new net.querz.nbt.tag.LongTag(Long.parseLong(s.substring(0, s.length() - 1)));
            } catch (NumberFormatException ex) {
                throw ptr.parseException("long not in range: \"" + s.substring(0, s.length() - 1) + "\"");
            }
        } else if (INT_LITERAL_PATTERN.matcher(s).matches()) {
            try {
                return new net.querz.nbt.tag.IntTag(Integer.parseInt(s));
            } catch (NumberFormatException ex) {
                throw ptr.parseException("int not in range: \"" + s.substring(0, s.length() - 1) + "\"");
            }
        } else if (DOUBLE_LITERAL_PATTERN.matcher(s).matches()) {
            return new net.querz.nbt.tag.DoubleTag(Double.parseDouble(s.substring(0, s.length() - 1)));
        } else if (DOUBLE_LITERAL_NO_SUFFIX_PATTERN.matcher(s).matches()) {
            return new net.querz.nbt.tag.DoubleTag(Double.parseDouble(s));
        } else if ("true".equalsIgnoreCase(s)) {
            return new net.querz.nbt.tag.ByteTag(true);
        } else if ("false".equalsIgnoreCase(s)) {
            return new net.querz.nbt.tag.ByteTag(false);
        }
        return new net.querz.nbt.tag.StringTag(s);
    }

    private net.querz.nbt.tag.CompoundTag parseCompoundTag(int maxDepth) throws net.querz.nbt.io.ParseException {
        ptr.expectChar('{');

        net.querz.nbt.tag.CompoundTag compoundTag = new CompoundTag();

        ptr.skipWhitespace();
        while (ptr.hasNext() && ptr.currentChar() != '}') {
            ptr.skipWhitespace();
            String key = ptr.currentChar() == '"' ? ptr.parseQuotedString() : ptr.parseSimpleString();
            if (key.isEmpty()) {
                throw new net.querz.nbt.io.ParseException("empty keys are not allowed");
            }
            ptr.expectChar(':');

            compoundTag.put(key, parseAnything(decrementMaxDepth(maxDepth)));

            if (!ptr.nextArrayElement()) {
                break;
            }
        }
        ptr.expectChar('}');
        return compoundTag;
    }

    private net.querz.nbt.tag.ListTag<?> parseListTag(int maxDepth) throws net.querz.nbt.io.ParseException {
        ptr.expectChar('[');
        ptr.skipWhitespace();
        net.querz.nbt.tag.ListTag<?> list = net.querz.nbt.tag.ListTag.createUnchecked(net.querz.nbt.tag.EndTag.class);
        while (ptr.currentChar() != ']') {
            net.querz.nbt.tag.Tag<?> element = parseAnything(decrementMaxDepth(maxDepth));
            try {
                list.addUnchecked(element);
            } catch (IllegalArgumentException ex) {
                throw ptr.parseException(ex.getMessage());
            }
            if (!ptr.nextArrayElement()) {
                break;
            }
        }
        ptr.expectChar(']');
        return list;
    }

    private net.querz.nbt.tag.ArrayTag<?> parseNumArray() throws net.querz.nbt.io.ParseException {
        ptr.expectChar('[');
        char arrayType = ptr.next();
        ptr.expectChar(';');
        ptr.skipWhitespace();
        switch (arrayType) {
            case 'B':
                return parseByteArrayTag();
            case 'I':
                return parseIntArrayTag();
            case 'L':
                return parseLongArrayTag();
        }
        throw new net.querz.nbt.io.ParseException("invalid array type '" + arrayType + "'");
    }

    private net.querz.nbt.tag.ByteArrayTag parseByteArrayTag() throws net.querz.nbt.io.ParseException {
        List<Byte> byteList = new ArrayList<>();
        while (ptr.currentChar() != ']') {
            String s = ptr.parseSimpleString();
            ptr.skipWhitespace();
            if (NUMBER_PATTERN.matcher(s).matches()) {
                try {
                    byteList.add(Byte.parseByte(s));
                } catch (NumberFormatException ex) {
                    throw ptr.parseException("byte not in range: \"" + s + "\"");
                }
            } else {
                throw ptr.parseException("invalid byte in ByteArrayTag: \"" + s + "\"");
            }
            if (!ptr.nextArrayElement()) {
                break;
            }
        }
        ptr.expectChar(']');
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        return new net.querz.nbt.tag.ByteArrayTag(bytes);
    }

    private net.querz.nbt.tag.IntArrayTag parseIntArrayTag() throws net.querz.nbt.io.ParseException {
        List<Integer> intList = new ArrayList<>();
        while (ptr.currentChar() != ']') {
            String s = ptr.parseSimpleString();
            ptr.skipWhitespace();
            if (NUMBER_PATTERN.matcher(s).matches()) {
                try {
                    intList.add(Integer.parseInt(s));
                } catch (NumberFormatException ex) {
                    throw ptr.parseException("int not in range: \"" + s + "\"");
                }
            } else {
                throw ptr.parseException("invalid int in IntArrayTag: \"" + s + "\"");
            }
            if (!ptr.nextArrayElement()) {
                break;
            }
        }
        ptr.expectChar(']');
        return new net.querz.nbt.tag.IntArrayTag(intList.stream().mapToInt(i -> i).toArray());
    }

    private net.querz.nbt.tag.LongArrayTag parseLongArrayTag() throws ParseException {
        List<Long> longList = new ArrayList<>();
        while (ptr.currentChar() != ']') {
            String s = ptr.parseSimpleString();
            ptr.skipWhitespace();
            if (NUMBER_PATTERN.matcher(s).matches()) {
                try {
                    longList.add(Long.parseLong(s));
                } catch (NumberFormatException ex) {
                    throw ptr.parseException("long not in range: \"" + s + "\"");
                }
            } else {
                throw ptr.parseException("invalid long in LongArrayTag: \"" + s + "\"");
            }
            if (!ptr.nextArrayElement()) {
                break;
            }
        }
        ptr.expectChar(']');
        return new net.querz.nbt.tag.LongArrayTag(longList.stream().mapToLong(l -> l).toArray());
    }
}
