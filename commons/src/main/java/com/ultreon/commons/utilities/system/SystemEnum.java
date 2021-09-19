package com.ultreon.commons.utilities.system;

public enum SystemEnum {
    WINDOWS("Windows"),
    UNIX("Unix"),
    LINUX("Linux"),
    OSX("OSX"),
    DARWIN("Darwin"),
    SOLARIS("Solaris"),
    ANDROID("Android"),
    IOS("iOS"),
    UNKNOWN("<<UNKNOWN>>"),
    OS_2("OS/2"),
    AIX("AIX"),
    DOS("DOS"),
    FREE_BSD("FreeBSD"),
    IRIX("Irix"),
    DIGITAL_UNIX("Digital Unix"),
    MPE_IX("MPE/iX"),
    NETWARE("Netware"),
    HP_UX("HP UX");

    private final String name;

    SystemEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
