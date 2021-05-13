package com.qsoftware.bubbles.util.python;

import com.qsoftware.bubbles.util.system.OS;
import com.qsoftware.bubbles.util.system.SystemEnum;

public class Platform {
    public static SystemEnum getSystem() {
        String sunDesktop = System.getProperty("sun.desktop");
        if (sunDesktop.equalsIgnoreCase("windows")) {
            return SystemEnum.WINDOWS;
        } else if (sunDesktop.equalsIgnoreCase("solaris")) {
            return SystemEnum.SOLARIS;
        } else if (sunDesktop.equalsIgnoreCase("android")) {
            return SystemEnum.ANDROID;
        } else if (sunDesktop.equalsIgnoreCase("ios")) {
            return SystemEnum.IOS;
        } else if (sunDesktop.equalsIgnoreCase("linux")) {
            return SystemEnum.LINUX;
        } else if (sunDesktop.equalsIgnoreCase("gtk")) {
            return SystemEnum.LINUX;
        } else if (sunDesktop.equalsIgnoreCase("kde")) {
            return SystemEnum.LINUX;
        } else if (sunDesktop.equalsIgnoreCase("unity")) {
            return SystemEnum.LINUX;
        } else if (sunDesktop.equalsIgnoreCase("lxde")) {
            return SystemEnum.LINUX;
        } else if (sunDesktop.equalsIgnoreCase("lxqt")) {
            return SystemEnum.LINUX;
        } else if (sunDesktop.equalsIgnoreCase("deepin")) {
            return SystemEnum.LINUX;
        } else if (sunDesktop.equalsIgnoreCase("xserver")) {
            return SystemEnum.LINUX;
        } else if (sunDesktop.equalsIgnoreCase("xdesktop")) {
            return SystemEnum.LINUX;
        } else if (sunDesktop.equalsIgnoreCase("xsession")) {
            return SystemEnum.LINUX;
        } else if (sunDesktop.equalsIgnoreCase("darwin")) {
            return SystemEnum.DARWIN;
        } else if (sunDesktop.equalsIgnoreCase("osx")) {
            return SystemEnum.OSX;
        } else if (sunDesktop.equalsIgnoreCase("unix")) {
            return SystemEnum.UNIX;
        }
        return SystemEnum.UNKNOWN;
    }

    public static OS getOS() {
        return new OS();
    }
}
