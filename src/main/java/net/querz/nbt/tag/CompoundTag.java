package net.querz.nbt.tag;

import net.querz.io.MaxDepthIO;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.function.BiConsumer;

@SuppressWarnings("UnusedReturnValue")
public class CompoundTag extends Tag<Map<String, Tag<?>>>
        implements Iterable<Map.Entry<String, Tag<?>>>, Comparable<CompoundTag>, MaxDepthIO {

    public static final byte ID = 10;

    public CompoundTag() {
        super(createEmptyValue());
    }

    public CompoundTag(int initialCapacity) {
        super(new HashMap<>(initialCapacity));
    }

    @Override
    public byte getID() {
        return ID;
    }

    private static Map<String, Tag<?>> createEmptyValue() {
        return new HashMap<>(8);
    }

    public int size() {
        return getValue().size();
    }

    public Tag<?> remove(String key) {
        return getValue().remove(key);
    }

    public void clear() {
        getValue().clear();
    }

    public boolean containsKey(String key) {
        return getValue().containsKey(key);
    }

    public boolean containsValue(Tag<?> value) {
        return getValue().containsValue(value);
    }

    public Collection<Tag<?>> values() {
        return getValue().values();
    }

    public Set<String> keySet() {
        return getValue().keySet();
    }

    public Set<Map.Entry<String, Tag<?>>> entrySet() {
        return new NonNullEntrySet<>(getValue().entrySet());
    }

    @Override
    public Iterator<Map.Entry<String, Tag<?>>> iterator() {
        return entrySet().iterator();
    }

    public void forEach(BiConsumer<String, Tag<?>> action) {
        getValue().forEach(action);
    }

    public <C extends Tag<?>> C get(String key, Class<C> type) {
        Tag<?> t = getValue().get(key);
        if (t != null) {
            return type.cast(t);
        }
        return null;
    }

    public Tag<?> get(String key) {
        return getValue().get(key);
    }

    public NumberTag<?> getNumberTag(String key) {
        return (NumberTag<?>) getValue().get(key);
    }

    public Number getNumber(String key) {
        return getNumberTag(key).getValue();
    }

    public ByteTag getByteTag(String key) {
        return get(key, ByteTag.class);
    }

    public ShortTag getShortTag(String key) {
        return get(key, ShortTag.class);
    }

    public IntTag getIntTag(String key) {
        return get(key, IntTag.class);
    }

    public LongTag getLongTag(String key) {
        return get(key, LongTag.class);
    }

    public FloatTag getFloatTag(String key) {
        return get(key, FloatTag.class);
    }

    public DoubleTag getDoubleTag(String key) {
        return get(key, DoubleTag.class);
    }

    public StringTag getStringTag(String key) {
        return get(key, StringTag.class);
    }

    public ByteArrayTag getByteArrayTag(String key) {
        return get(key, ByteArrayTag.class);
    }

    public IntArrayTag getIntArrayTag(String key) {
        return get(key, IntArrayTag.class);
    }

    public LongArrayTag getLongArrayTag(String key) {
        return get(key, LongArrayTag.class);
    }

    public ListTag<?> getListTag(String key) {
        return get(key, ListTag.class);
    }

    @SuppressWarnings("unchecked")
    public <T extends Tag<?>> ListTag<T> getListTag(String key, Class<T> type) {
        return get(key, ListTag.class);
    }

    public CompoundTag getCompoundTag(String key) {
        return get(key, CompoundTag.class);
    }

    public boolean getBoolean(String key) {
        Tag<?> t = get(key);
        return t instanceof ByteTag && ((ByteTag) t).asByte() > 0;
    }

    public byte getByte(String key) {
        ByteTag t = getByteTag(key);
        return t == null ? ByteTag.ZERO_VALUE : t.asByte();
    }

    public short getShort(String key) {
        ShortTag t = getShortTag(key);
        return t == null ? ShortTag.ZERO_VALUE : t.asShort();
    }

    public int getInt(String key) {
        IntTag t = getIntTag(key);
        return t == null ? IntTag.ZERO_VALUE : t.asInt();
    }

    public long getLong(String key) {
        LongTag t = getLongTag(key);
        return t == null ? LongTag.ZERO_VALUE : t.asLong();
    }

    public float getFloat(String key) {
        FloatTag t = getFloatTag(key);
        return t == null ? FloatTag.ZERO_VALUE : t.asFloat();
    }

    public double getDouble(String key) {
        DoubleTag t = getDoubleTag(key);
        return t == null ? DoubleTag.ZERO_VALUE : t.asDouble();
    }

    public String getString(String key) {
        StringTag t = getStringTag(key);
        return t == null ? StringTag.ZERO_VALUE : t.getValue();
    }

    public byte[] getByteArray(String key) {
        ByteArrayTag t = getByteArrayTag(key);
        return t == null ? ByteArrayTag.ZERO_VALUE : t.getValue();
    }

    public int[] getIntArray(String key) {
        IntArrayTag t = getIntArrayTag(key);
        return t == null ? IntArrayTag.ZERO_VALUE : t.getValue();
    }

    public long[] getLongArray(String key) {
        LongArrayTag t = getLongArrayTag(key);
        return t == null ? LongArrayTag.ZERO_VALUE : t.getValue();
    }

    public UUID getUuid(String key) {
        CompoundTag t = getCompoundTag(key);
        if (t.containsKey("most") && t.containsKey("least")) {
            if (t.get("most") instanceof LongTag && t.get("least") instanceof LongTag) {
                long most = t.getLong("most");
                long least = t.getLong("least");
                return new UUID(most, least);
            }
        }

        return null;
    }

    public Point getIntPoint(String key) {
        CompoundTag t = getCompoundTag(key);
        if (t.containsKey("x") && t.containsKey("y")) {
            if (t.get("x") instanceof IntTag && t.get("y") instanceof IntTag) {
                int x = t.getInt("x");
                int y = t.getInt("y");
                return new Point(x, y);
            }
        }

        return null;
    }

    public Point2D.Float getFloatPoint(String key) {
        CompoundTag t = getCompoundTag(key);
        if (t.containsKey("x") && t.containsKey("y")) {
            if (t.get("x") instanceof FloatTag && t.get("y") instanceof FloatTag) {
                float x = t.getFloat("x");
                float y = t.getFloat("y");
                return new Point2D.Float(x, y);
            }
        }

        return null;
    }

    public Point2D.Double getDoublePoint(String key) {
        CompoundTag t = getCompoundTag(key);
        if (t.containsKey("x") && t.containsKey("y")) {
            if (t.get("x") instanceof DoubleTag && t.get("y") instanceof DoubleTag) {
                double x = t.getDouble("x");
                double y = t.getDouble("y");
                return new Point.Double(x, y);
            }
        }

        return null;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }

        Tag<?> t = get(key);
        if (t instanceof ByteTag) {
            return ((ByteTag) t).asByte() > 0;
        } else {
            return defaultValue;
        }
    }

    public byte getByte(String key, byte defaultValue) {
        ByteTag t = getByteTag(key);
        return t == null ? defaultValue : t.asByte();
    }

    public short getShort(String key, short defaultValue) {
        ShortTag t = getShortTag(key);
        return t == null ? defaultValue : t.asShort();
    }

    public int getInt(String key, int defaultValue) {
        IntTag t = getIntTag(key);
        return t == null ? defaultValue : t.asInt();
    }

    public long getLong(String key, long defaultValue) {
        LongTag t = getLongTag(key);
        return t == null ? defaultValue : t.asLong();
    }

    public float getFloat(String key, float defaultValue) {
        FloatTag t = getFloatTag(key);
        return t == null ? defaultValue : t.asFloat();
    }

    public double getDouble(String key, double defaultValue) {
        DoubleTag t = getDoubleTag(key);
        return t == null ? defaultValue : t.asDouble();
    }

    public String getString(String key, String defaultValue) {
        StringTag t = getStringTag(key);
        return t == null ? defaultValue : t.getValue();
    }

    public byte[] getByteArray(String key, byte[] defaultValue) {
        ByteArrayTag t = getByteArrayTag(key);
        return t == null ? defaultValue : t.getValue();
    }

    public int[] getIntArray(String key, int[] defaultValue) {
        IntArrayTag t = getIntArrayTag(key);
        return t == null ? defaultValue : t.getValue();
    }

    public long[] getLongArray(String key, long[] defaultValue) {
        LongArrayTag t = getLongArrayTag(key);
        return t == null ? defaultValue : t.getValue();
    }

    public UUID getUuid(String key, UUID defaultValue) {
        CompoundTag t = getCompoundTag(key);
        if (t.containsKey("most") && t.containsKey("least")) {
            if (t.get("most") instanceof LongTag && t.get("least") instanceof LongTag) {
                long most = t.getLong("most");
                long least = t.getLong("least");
                return new UUID(most, least);
            }
        }

        return defaultValue;
    }

    public Point getIntPoint(String key, Point defaultValue) {
        CompoundTag t = getCompoundTag(key);
        if (t.containsKey("x") && t.containsKey("y")) {
            if (t.get("x") instanceof IntTag && t.get("y") instanceof IntTag) {
                int x = t.getInt("x");
                int y = t.getInt("y");
                return new Point(x, y);
            }
        }

        return defaultValue;
    }

    public Point2D.Float getFloatPoint(String key, Point2D.Float defaultValue) {
        CompoundTag t = getCompoundTag(key);
        if (t.containsKey("x") && t.containsKey("y")) {
            if (t.get("x") instanceof FloatTag && t.get("y") instanceof FloatTag) {
                float x = t.getFloat("x");
                float y = t.getFloat("y");
                return new Point2D.Float(x, y);
            }
        }

        return defaultValue;
    }

    public Point2D.Double getDoublePoint(String key, Point2D.Double defaultValue) {
        CompoundTag t = getCompoundTag(key);
        if (t.containsKey("x") && t.containsKey("y")) {
            if (t.get("x") instanceof DoubleTag && t.get("y") instanceof DoubleTag) {
                double x = t.getDouble("x");
                double y = t.getDouble("y");
                return new Point.Double(x, y);
            }
        }

        return defaultValue;
    }

    public Tag<?> put(String key, Tag<?> tag) {
        return getValue().put(Objects.requireNonNull(key), Objects.requireNonNull(tag));
    }

    public Tag<?> putIfNotNull(String key, Tag<?> tag) {
        if (tag == null) {
            return this;
        }
        return put(key, tag);
    }

    public Tag<?> putBoolean(String key, boolean value) {
        return put(key, new ByteTag(value));
    }

    public Tag<?> putByte(String key, byte value) {
        return put(key, new ByteTag(value));
    }

    public Tag<?> putShort(String key, short value) {
        return put(key, new ShortTag(value));
    }

    public Tag<?> putInt(String key, int value) {
        return put(key, new IntTag(value));
    }

    public Tag<?> putLong(String key, long value) {
        return put(key, new LongTag(value));
    }

    public Tag<?> putFloat(String key, float value) {
        return put(key, new FloatTag(value));
    }

    public Tag<?> putDouble(String key, double value) {
        return put(key, new DoubleTag(value));
    }

    public Tag<?> putString(String key, String value) {
        return put(key, new StringTag(value));
    }

    public Tag<?> putByteArray(String key, byte[] value) {
        return put(key, new ByteArrayTag(value));
    }

    public Tag<?> putIntArray(String key, int[] value) {
        return put(key, new IntArrayTag(value));
    }

    public Tag<?> putLongArray(String key, long[] value) {
        return put(key, new LongArrayTag(value));
    }

    public Tag<?> putUuid(String key, UUID value) {
        CompoundTag tag = new CompoundTag();
        tag.putLong("most", value.getMostSignificantBits());
        tag.putLong("least", value.getLeastSignificantBits());

        return put(key, tag);
    }

    public Tag<?> putIntPoint(String key, Point value) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", value.x);
        tag.putInt("y", value.y);

        return put(key, tag);
    }

    public Tag<?> putFloatPoint(String key, Point2D.Float value) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("x", value.x);
        tag.putFloat("y", value.y);

        return put(key, tag);
    }

    public Tag<?> putDoublePoint(String key, Point2D.Double value) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("x", value.x);
        tag.putDouble("y", value.y);

        return put(key, tag);
    }

    @Override
    public String valueToString(int maxDepth) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Tag<?>> e : getValue().entrySet()) {
            sb.append(first ? "" : ",")
                    .append(escapeString(e.getKey(), false)).append(":")
                    .append(e.getValue().toString(decrementMaxDepth(maxDepth)));
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!super.equals(other) || size() != ((CompoundTag) other).size()) {
            return false;
        }
        for (Map.Entry<String, Tag<?>> e : getValue().entrySet()) {
            Tag<?> v;
            if ((v = ((CompoundTag) other).get(e.getKey())) == null || !e.getValue().equals(v)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(CompoundTag o) {
        return Integer.compare(size(), o.getValue().size());
    }

    @Override
    public CompoundTag clone() {
        // Choose initial capacity based on default load factor (0.75) so all entries fit in map without resizing
        CompoundTag copy = new CompoundTag((int) Math.ceil(getValue().size() / 0.75f));
        for (Map.Entry<String, Tag<?>> e : getValue().entrySet()) {
            copy.put(e.getKey(), e.getValue().clone());
        }
        return copy;
    }
}
