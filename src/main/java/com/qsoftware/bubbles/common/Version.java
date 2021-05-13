package com.qsoftware.bubbles.common;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class Version implements Serializable {
    private final int major;
    private final int minor;
    private final int build;
    private final VersionType type;
    private final int release;

    public Version(int major, int minor, int build, @NotNull VersionType type, int release) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.type = type;
        this.release = release;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + build + "-" + type.getName() + release;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return major == version.major &&
                minor == version.minor &&
                release == version.release &&
                type == version.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, type, release);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getBuild() {
        return build;
    }

    public VersionType getType() {
        return type;
    }

    public int getRelease() {
        return release;
    }
}
