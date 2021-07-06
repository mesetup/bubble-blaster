package net.querz.nbt.io;

import net.querz.nbt.tag.Tag;

public class NamedTag {

    private String name;
    private net.querz.nbt.tag.Tag<?> tag;

    public NamedTag(String name, net.querz.nbt.tag.Tag<?> tag) {
        this.name = name;
        this.tag = tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(net.querz.nbt.tag.Tag<?> tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public Tag<?> getTag() {
        return tag;
    }
}
