package net.querz.nbt.io;

import net.querz.io.ExceptionBiFunction;
import net.querz.io.MaxDepthIO;
import net.querz.nbt.tag.CompoundTag;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NBTInputStream extends DataInputStream implements NBTInput, MaxDepthIO {

    private static final Map<Byte, ExceptionBiFunction<NBTInputStream, Integer, ? extends net.querz.nbt.tag.Tag<?>, IOException>> readers = new HashMap<>();
    private static final Map<Byte, Class<?>> idClassMapping = new HashMap<>();

    static {
        put(net.querz.nbt.tag.EndTag.ID, (i, d) -> net.querz.nbt.tag.EndTag.INSTANCE, net.querz.nbt.tag.EndTag.class);
        put(net.querz.nbt.tag.ByteTag.ID, (i, d) -> readByte(i), net.querz.nbt.tag.ByteTag.class);
        put(net.querz.nbt.tag.ShortTag.ID, (i, d) -> readShort(i), net.querz.nbt.tag.ShortTag.class);
        put(net.querz.nbt.tag.IntTag.ID, (i, d) -> readInt(i), net.querz.nbt.tag.IntTag.class);
        put(net.querz.nbt.tag.LongTag.ID, (i, d) -> readLong(i), net.querz.nbt.tag.LongTag.class);
        put(net.querz.nbt.tag.FloatTag.ID, (i, d) -> readFloat(i), net.querz.nbt.tag.FloatTag.class);
        put(net.querz.nbt.tag.DoubleTag.ID, (i, d) -> readDouble(i), net.querz.nbt.tag.DoubleTag.class);
        put(net.querz.nbt.tag.ByteArrayTag.ID, (i, d) -> readByteArray(i), net.querz.nbt.tag.ByteArrayTag.class);
        put(net.querz.nbt.tag.StringTag.ID, (i, d) -> readString(i), net.querz.nbt.tag.StringTag.class);
        put(net.querz.nbt.tag.ListTag.ID, NBTInputStream::readListTag, net.querz.nbt.tag.ListTag.class);
        put(net.querz.nbt.tag.CompoundTag.ID, NBTInputStream::readCompound, net.querz.nbt.tag.CompoundTag.class);
        put(net.querz.nbt.tag.IntArrayTag.ID, (i, d) -> readIntArray(i), net.querz.nbt.tag.IntArrayTag.class);
        put(net.querz.nbt.tag.LongArrayTag.ID, (i, d) -> readLongArray(i), net.querz.nbt.tag.LongArrayTag.class);
    }

    private static void put(byte id, ExceptionBiFunction<NBTInputStream, Integer, ? extends net.querz.nbt.tag.Tag<?>, IOException> reader, Class<?> clazz) {
        readers.put(id, reader);
        idClassMapping.put(id, clazz);
    }

    public NBTInputStream(InputStream in) {
        super(in);
    }

    public net.querz.nbt.io.NamedTag readTag(int maxDepth) throws IOException {
        byte id = readByte();
        return new NamedTag(readUTF(), readTag(id, maxDepth));
    }

    public net.querz.nbt.tag.Tag<?> readRawTag(int maxDepth) throws IOException {
        byte id = readByte();
        return readTag(id, maxDepth);
    }

    private net.querz.nbt.tag.Tag<?> readTag(byte type, int maxDepth) throws IOException {
        ExceptionBiFunction<NBTInputStream, Integer, ? extends net.querz.nbt.tag.Tag<?>, IOException> f;
        if ((f = readers.get(type)) == null) {
            throw new IOException("invalid tag id \"" + type + "\"");
        }
        return f.accept(this, maxDepth);
    }

    private static net.querz.nbt.tag.ByteTag readByte(NBTInputStream in) throws IOException {
        return new net.querz.nbt.tag.ByteTag(in.readByte());
    }

    private static net.querz.nbt.tag.ShortTag readShort(NBTInputStream in) throws IOException {
        return new net.querz.nbt.tag.ShortTag(in.readShort());
    }

    private static net.querz.nbt.tag.IntTag readInt(NBTInputStream in) throws IOException {
        return new net.querz.nbt.tag.IntTag(in.readInt());
    }

    private static net.querz.nbt.tag.LongTag readLong(NBTInputStream in) throws IOException {
        return new net.querz.nbt.tag.LongTag(in.readLong());
    }

    private static net.querz.nbt.tag.FloatTag readFloat(NBTInputStream in) throws IOException {
        return new net.querz.nbt.tag.FloatTag(in.readFloat());
    }

    private static net.querz.nbt.tag.DoubleTag readDouble(NBTInputStream in) throws IOException {
        return new net.querz.nbt.tag.DoubleTag(in.readDouble());
    }

    private static net.querz.nbt.tag.StringTag readString(NBTInputStream in) throws IOException {
        return new net.querz.nbt.tag.StringTag(in.readUTF());
    }

    private static net.querz.nbt.tag.ByteArrayTag readByteArray(NBTInputStream in) throws IOException {
        net.querz.nbt.tag.ByteArrayTag bat = new net.querz.nbt.tag.ByteArrayTag(new byte[in.readInt()]);
        in.readFully(bat.getValue());
        return bat;
    }

    private static net.querz.nbt.tag.IntArrayTag readIntArray(NBTInputStream in) throws IOException {
        int l = in.readInt();
        int[] data = new int[l];
        net.querz.nbt.tag.IntArrayTag iat = new net.querz.nbt.tag.IntArrayTag(data);
        for (int i = 0; i < l; i++) {
            data[i] = in.readInt();
        }
        return iat;
    }

    private static net.querz.nbt.tag.LongArrayTag readLongArray(NBTInputStream in) throws IOException {
        int l = in.readInt();
        long[] data = new long[l];
        net.querz.nbt.tag.LongArrayTag iat = new net.querz.nbt.tag.LongArrayTag(data);
        for (int i = 0; i < l; i++) {
            data[i] = in.readLong();
        }
        return iat;
    }

    private static net.querz.nbt.tag.ListTag<?> readListTag(NBTInputStream in, int maxDepth) throws IOException {
        byte listType = in.readByte();
        net.querz.nbt.tag.ListTag<?> list = net.querz.nbt.tag.ListTag.createUnchecked(idClassMapping.get(listType));
        int length = in.readInt();
        if (length < 0) {
            length = 0;
        }
        for (int i = 0; i < length; i++) {
            list.addUnchecked(in.readTag(listType, in.decrementMaxDepth(maxDepth)));
        }
        return list;
    }

    private static net.querz.nbt.tag.CompoundTag readCompound(NBTInputStream in, int maxDepth) throws IOException {
        net.querz.nbt.tag.CompoundTag comp = new CompoundTag();
        for (int id = in.readByte() & 0xFF; id != 0; id = in.readByte() & 0xFF) {
            String key = in.readUTF();
            net.querz.nbt.tag.Tag<?> element = in.readTag((byte) id, in.decrementMaxDepth(maxDepth));
            comp.put(key, element);
        }
        return comp;
    }
}
