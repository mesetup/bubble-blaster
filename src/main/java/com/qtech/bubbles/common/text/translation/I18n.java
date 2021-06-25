package com.qtech.bubbles.common.text.translation;

import java.util.Locale;

public class I18n {
    private static final LanguageMap localizedName = LanguageMap.getInstance();
    private static final LanguageMap fallbackTranslator = new LanguageMap(new Locale("en", "us"));

    public static String translateToLocal(String key) {
        if (canTranslate(key)) {
            return localizedName.translateKey(key);
        } else {
            return translateToFallback(key);
        }
    }

    public static String translateToLocalFormatted(String key, Object... format) {
        if (canTranslate(key)) {
            return localizedName.translateKeyFormat(key, format);
        } else {
            return translateToFallbackFormatted(key, format);
        }
    }

    public static String translateToFallback(String key) {
        return fallbackTranslator.translateKey(key);
    }

    public static String translateToFallbackFormatted(String key, Object... format) {
        return fallbackTranslator.translateKeyFormat(key, format);
    }

    public static boolean canTranslate(String key) {
        return localizedName.isKeyTranslated(key);
    }

    public static long getLastTranslationUpdateTimeInMilliseconds() {
        return localizedName.getLastUpdateTimeInMilliseconds();
    }
}