package com.ultreon.commons.utilities.system;

import java.io.Serializable;

public class User implements Serializable {
    private final String name;
    private final String variant;
    private final String home;
    private final String dir;
    private final String countryName;
    private final String language;

    public User() {
        this(
                System.getProperty("user.name"), System.getProperty("user.variant"), // Name & variant
                System.getProperty("user.home"), System.getProperty("user.dir"), // Home & directory
                System.getProperty("user.country"), System.getProperty("user.language") // Country & language
        );
    }

    public User(String name) {
        this(name, null, null, null, null, null);
    }

    public User(String name, String variant, String home, String dir, String countryName, String language) {
        this.name = name;
        this.variant = variant;
        this.home = home;
        this.dir = dir;
        this.countryName = countryName;
        this.language = language;
    }

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

    public String getName() {
        return name;
    }

    public String getVariant() {
        return variant;
    }

    public String getHome() {
        return home;
    }

    public String getDir() {
        return dir;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getLanguage() {
        return language;
    }
}
