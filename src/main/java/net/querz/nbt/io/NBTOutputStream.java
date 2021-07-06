package net.querz.nbt.io;

import net.querz.io.ExceptionTriConsumer;
import net.querz.io.MaxDepthIO;
import net.querz.nbt.tag.CompoundTag;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class NBTOutputStream extends DataOutputStream implements NBTOutput, MaxDepthIO {

    private static final Map<Byte, ExceptionTriConsumer<NBTOutputStream, net.querz.nbt.tag.Tag<?>, Integer, IOException>> writers = new HashMap<>();
    private static final Map<Class<?>, Byte> classIdMapping = new HashMap<>();

    static {
        put(net.querz.nbt.tag.EndTag.ID, (o, t, d) -> {
        }, net.querz.nbt.tag.EndTag.class);
        put(net.querz.nbt.tag.ByteTag.ID, (o, t, d) -> writeByte(o, t), net.querz.nbt.tag.ByteTag.class);
        put(net.querz.nbt.tag.ShortTag.ID, (o, t, d) -> writeShort(o, t), net.querz.nbt.tag.ShortTag.class);
        put(net.querz.nbt.tag.IntTag.ID, (o, t, d) -> writeInt(o, t), net.querz.nbt.tag.IntTag.class);
        put(net.querz.nbt.tag.LongTag.ID, (o, t, d) -> writeLong(o, t), net.querz.nbt.tag.LongTag.class);
        put(net.querz.nbt.tag.FloatTag.ID, (o, t, d) -> writeFloat(o, t), net.querz.nbt.tag.FloatTag.class);
        put(net.querz.nbt.tag.DoubleTag.ID, (o, t, d) -> writeDouble(o, t), net.querz.nbt.tag.DoubleTag.class);
        put(net.querz.nbt.tag.ByteArrayTag.ID, (o, t, d) -> writeByteArray(o, t), net.querz.nbt.tag.ByteArrayTag.class);
        put(net.querz.nbt.tag.StringTag.ID, (o, t, d) -> writeString(o, t), net.querz.nbt.tag.StringTag.class);
        put(net.querz.nbt.tag.ListTag.ID, NBTOutputStream::writeList, net.querz.nbt.tag.ListTag.class);
        put(net.querz.nbt.tag.CompoundTag.ID, NBTOutputStream::writeCompound, net.querz.nbt.tag.CompoundTag.class);
        put(net.querz.nbt.tag.IntArrayTag.ID, (o, t, d) -> writeIntArray(o, t), net.querz.nbt.tag.IntArrayTag.class);
        put(net.querz.nbt.tag.LongArrayTag.ID, (o, t, d) -> writeLongArray(o, t), net.querz.nbt.tag.LongArrayTag.class);
    }

    private static void put(byte id, ExceptionTriConsumer<NBTOutputStream, net.querz.nbt.tag.Tag<?>, Integer, IOException> f, Class<?> clazz) {
        writers.put(id, f);
        classIdMapping.put(clazz, id);
    }

    public NBTOutputStream(OutputStream out) {
        super(out);
    }

    public void writeTag(NamedTag tag, int maxDepth) throws IOException {
        writeByte(tag.getTag().getID());
        if (tag.getTag().getID() != 0) {
            writeUTF(tag.getName() == null ? "" : tag.getName());
        }
        writeRawTag(tag.getTag(), maxDepth);
    }

    public void writeTag(net.querz.nbt.tag.Tag<?> tag, int maxDepth) throws IOException {
        writeByte(tag.getID());
        if (tag.getID() != 0) {
            writeUTF("");
        }
        writeRawTag(tag, maxDepth);
    }

    public void writeRawTag(net.querz.nbt.tag.Tag<?> tag, int maxDepth) throws IOException {
        ExceptionTriConsumer<NBTOutputStream, net.querz.nbt.tag.Tag<?>, Integer, IOException> f;
        if ((f = writers.get(tag.getID())) == null) {
            throw new IOException("invalid tag \"" + tag.getID() + "\"");
        }
        f.accept(this, tag, maxDepth);
    }

    static byte idFromClass(Class<?> clazz) {
        Byte id = classIdMapping.get(clazz);
        if (id == null) {
            throw new IllegalArgumentException("unknown Tag class " + clazz.getName());
        }
        return id;
    }

    private static void writeByte(NBTOutputStream out, net.querz.nbt.tag.Tag<?> tag) throws IOException {
        out.writeByte(((net.querz.nbt.tag.ByteTag) tag).asByte());
    }

    private static void writeShort(NBTOutputStream out, net.querz.nbt.tag.Tag<?> tag) throws IOException {
        out.writeShort(((net.querz.nbt.tag.ShortTag) tag).asShort());
    }

    private static void writeInt(NBTOutputStream out, net.querz.nbt.tag.Tag<?> tag) throws IOException {
        out.writeInt(((net.querz.nbt.tag.IntTag) tag).asInt());
    }

    private static void writeLong(NBTOutputStream out, net.querz.nbt.tag.Tag<?> tag) throws IOException {
        out.writeLong(((net.querz.nbt.tag.LongTag) tag).asLong());
    }

    private static void writeFloat(NBTOutputStream out, net.querz.nbt.tag.Tag<?> tag) throws IOException {
        out.writeFloat(((net.querz.nbt.tag.FloatTag) tag).asFloat());
    }

    private static void writeDouble(NBTOutputStream out, net.querz.nbt.tag.Tag<?> tag) throws IOException {
        out.writeDouble(((net.querz.nbt.tag.DoubleTag) tag).asDouble());
    }

    private static void writeString(NBTOutputStream out, net.querz.nbt.tag.Tag<?> tag) throws IOException {
        out.writeUTF(((net.querz.nbt.tag.StringTag) tag).getValue());
    }

    private static void writeByteArray(NBTOutputStream out, net.querz.nbt.tag.Tag<?> tag) throws IOException {
        out.writeInt(((net.querz.nbt.tag.ByteArrayTag) tag).length());
        out.write(((net.querz.nbt.tag.ByteArrayTag) tag).getValue());
    }

    private static void writeIntArray(NBTOutputStream out, net.querz.nbt.tag.Tag<?> tag) throws IOException {
        out.writeInt(((net.querz.nbt.tag.IntArrayTag) tag).length());
        for (int i : ((net.querz.nbt.tag.IntArrayTag) tag).getValue()) {
            out.writeInt(i);
        }
    }

    private static void writeLongArray(NBTOutputStream out, net.querz.nbt.tag.Tag<?> tag) throws IOException {
        out.writeInt(((net.querz.nbt.tag.LongArrayTag) tag).length());
        for (long l : ((net.querz.nbt.tag.LongArrayTag) tag).getValue()) {
            out.writeLong(l);
        }
    }

    private static void writeList(NBTOutputStream out, net.querz.nbt.tag.Tag<?> tag, int maxDepth) throws IOException {
        out.writeByte(idFromClass(((net.querz.nbt.tag.ListTag<?>) tag).getTypeClass()));
        out.writeInt(((net.querz.nbt.tag.ListTag<?>) tag).size());
        for (net.querz.nbt.tag.Tag<?> t : ((net.querz.nbt.tag.ListTag<?>) tag)) {
            out.writeRawTag(t, out.decrementMaxDepth(maxDepth));
        }
    }

    private static void writeCompound(NBTOutputStream out, net.querz.nbt.tag.Tag<?> tag, int maxDepth) throws IOException {
        for (Map.Entry<String, net.querz.nbt.tag.Tag<?>> entry : (CompoundTag) tag) {
            if (entry.getValue().getID() == 0) {
                throw new IOException("end tag not allowed");
            }
            out.writeByte(entry.getValue().getID());
            out.writeUTF(entry.getKey());
            out.writeRawTag(entry.getValue(), out.decrementMaxDepth(maxDepth));
        }
        out.writeByte(0);
    }
}
