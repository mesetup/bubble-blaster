package com.ultreon.commons.utilities.python;

import com.ultreon.commons.utilities.system.OS;
import com.ultreon.commons.utilities.system.SystemEnum;

public class Platform {
    private Platform() {

    }

    public static SystemEnum getSystem() {
        String sunDesktop = System.getProperty("os.name");
        if (sunDesktop.toLowerCase().startsWith("windows")) {
            return SystemEnum.WINDOWS;
        } else if (sunDesktop.equalsIgnoreCase("solaris")) {
            return SystemEnum.SOLARIS;
        } else if (sunDesktop.equalsIgnoreCase("android")) {
            return SystemEnum.ANDROID;
        } else if (sunDesktop.equalsIgnoreCase("ios")) {
            return SystemEnum.IOS;
        } else if (sunDesktop.equalsIgnoreCase("linux")) {
            return SystemEnum.LINUX;
        } else if (sunDesktop.equalsIgnoreCase("darwin")) {
            return SystemEnum.DARWIN;
        } else if (sunDesktop.equalsIgnoreCase("osx")) {
            return SystemEnum.OSX;
        } else if (sunDesktop.equalsIgnoreCase("mac os")) {
            return SystemEnum.OSX;
        } else if (sunDesktop.equalsIgnoreCase("mac os x")) {
            return SystemEnum.OSX;
        } else if (sunDesktop.equalsIgnoreCase("os/2")) {
            return SystemEnum.OS_2;
        } else if (sunDesktop.equalsIgnoreCase("aix")) {
            return SystemEnum.AIX;
        } else if (sunDesktop.equalsIgnoreCase("freebsd")) {
            return SystemEnum.FREE_BSD;
        } else if (sunDesktop.equalsIgnoreCase("irix")) {
            return SystemEnum.IRIX;
        } else if (sunDesktop.equalsIgnoreCase("digital unix")) {
            return SystemEnum.DIGITAL_UNIX;
        } else if (sunDesktop.equalsIgnoreCase("mpe/ix")) {
            return SystemEnum.MPE_IX;
        } else if (sunDesktop.equalsIgnoreCase("hp ux")) {
            return SystemEnum.HP_UX;
        } else if (sunDesktop.toLowerCase().startsWith("netware")) {
            return SystemEnum.NETWARE;
        } else if (sunDesktop.equalsIgnoreCase("dos")) {
            return SystemEnum.DOS;
        } else if (sunDesktop.equalsIgnoreCase("unix")) {
            return SystemEnum.UNIX;
        }
        return SystemEnum.UNKNOWN;
    }

    public static OS getOS() {
        return new OS();
    }
}
