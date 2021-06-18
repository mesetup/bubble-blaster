package com.qtech.utilities.system

enum class SystemEnum(val text: String) {
    WINDOWS("Windows"), UNIX("Unix"), LINUX("Linux"), OSX("OSX"), DARWIN("Darwin"), SOLARIS("Solaris"), ANDROID("Android"), IOS("iOS"), UNKNOWN("<<UNKNOWN>>"), OS_2("OS/2"), AIX("AIX"), DOS("DOS"), FREE_BSD("FreeBSD"), IRIX("Irix"), DIGITAL_UNIX(
        "Digital Unix"),
    MPE_IX("MPE/iX"), NETWARE("Netware"), HP_UX("HP UX");

    override fun toString(): String {
        return text
    }
}