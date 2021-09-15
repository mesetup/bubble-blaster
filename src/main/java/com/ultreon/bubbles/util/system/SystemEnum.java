package com.ultreon.bubbles.util.system;

public enum SystemEnum {
    WINDOWS("Windows"), UNIX("Unix"), LINUX("Linux"), OSX("MacOS-X"), DARWIN("Darwin"), SOLARIS("Solaris"), ANDROID("Android"), IOS("iOS"), UNKNOWN("<<UNKNOWN>>");
    protected final String name;

    SystemEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
