package qtech.utilities.python

import qtech.utilities.system.OS
import qtech.utilities.system.SystemEnum

object Platform {
    val system: SystemEnum
        get() {
            val sunDesktop = System.getProperty("os.name")
            when {
                sunDesktop.toLowerCase().startsWith("windows") -> {
                    return SystemEnum.WINDOWS
                }
                sunDesktop.equals("solaris", ignoreCase = true) -> {
                    return SystemEnum.SOLARIS
                }
                sunDesktop.equals("android", ignoreCase = true) -> {
                    return SystemEnum.ANDROID
                }
                sunDesktop.equals("ios", ignoreCase = true) -> {
                    return SystemEnum.IOS
                }
                sunDesktop.equals("linux", ignoreCase = true) -> {
                    return SystemEnum.LINUX
                }
                sunDesktop.equals("darwin", ignoreCase = true) -> {
                    return SystemEnum.DARWIN
                }
                sunDesktop.equals("osx", ignoreCase = true) -> {
                    return SystemEnum.OSX
                }
                sunDesktop.equals("mac os", ignoreCase = true) -> {
                    return SystemEnum.OSX
                }
                sunDesktop.equals("mac os x", ignoreCase = true) -> {
                    return SystemEnum.OSX
                }
                sunDesktop.equals("os/2", ignoreCase = true) -> {
                    return SystemEnum.OS_2
                }
                sunDesktop.equals("aix", ignoreCase = true) -> {
                    return SystemEnum.AIX
                }
                sunDesktop.equals("freebsd", ignoreCase = true) -> {
                    return SystemEnum.FREE_BSD
                }
                sunDesktop.equals("irix", ignoreCase = true) -> {
                    return SystemEnum.IRIX
                }
                sunDesktop.equals("digital unix", ignoreCase = true) -> {
                    return SystemEnum.DIGITAL_UNIX
                }
                sunDesktop.equals("mpe/ix", ignoreCase = true) -> {
                    return SystemEnum.MPE_IX
                }
                sunDesktop.equals("hp ux", ignoreCase = true) -> {
                    return SystemEnum.HP_UX
                }
                sunDesktop.toLowerCase().startsWith("netware") -> {
                    return SystemEnum.NETWARE
                }
                sunDesktop.equals("dos", ignoreCase = true) -> {
                    return SystemEnum.DOS
                }
                sunDesktop.equals("unix", ignoreCase = true) -> {
                    return SystemEnum.UNIX
                }
                else -> return SystemEnum.UNKNOWN
            }
        }
    val oS: OS
        get() = OS()
}
