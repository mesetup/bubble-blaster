package com.ultreon.bubbles.util.system;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OS {
    private final String name;
    private final String version;

    public OS() {
        this(System.getProperty("os.name"), System.getProperty("os.version"));
    }

    @Deprecated
    public OS(String name) {
        this(name, System.getProperty("os.version"));
    }

    public OS(String name, String version) {
        this.name = name;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OS os = (OS) o;
        return name.equals(os.name) &&
                version.equals(os.version);
    }

    public boolean equalsIgnoreVersion(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OS os = (OS) o;
        return name.equals(os.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version);
    }

    @NotNull
    public String getVersion() {
        return version;
    }

    @NotNull
    public String getName() {
        return name;
    }
}
