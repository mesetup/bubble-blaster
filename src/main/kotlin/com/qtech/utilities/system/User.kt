@file:Suppress("unused")

package com.qtech.utilities.system

import java.io.Serializable

class User constructor(
//    public static String getCountryName() {
    //        return System.getProperty("user.country");
    //    }
    //    public static String getName() {
    //        return System.getProperty("user.name");
    //    }
    //    public static String getVariant() {
    //        return System.getProperty("user.variant");
    //    }
    //    public static String getHome() {
    //        return System.getProperty("user.home");
    //    }
    //    public static String getDir() {
    //        return System.getProperty("user.dir");
    //    }
    //    public static String getLanguage() {
    //        return System.getProperty("user.language");
    //    }
    val name: String? =
        System.getProperty("user.name"),
    val variant: String? = System.getProperty("user.variant"),
    val home: String? =  // Name & variant
        System.getProperty("user.home"),
    val dir: String? = System.getProperty("user.dir"),
    val countryName: String? =  // Home & directory
        System.getProperty("user.country"),
    val language: String? = System.getProperty("user.language") // Country & language
) : Serializable {
    constructor(name: String?) : this(name, null, null, null, null, null) {}
}