package com.qtech.bubbles.common.text.translation;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.qtech.bubbles.settings.GameSettings;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class LanguageMap {
    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern NUMERIC_VARIABLE_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    private static final Splitter EQUAL_SIGN_SPLITTER = Splitter.on('=').limit(2);
    private static final LanguageMap instance = new LanguageMap(GameSettings.instance().getLanguageLocale());
    private final Map<String, String> languageList = Maps.newHashMap();
    private long lastUpdateTimeInMilliseconds;

    public LanguageMap(Locale locale) {
        String langCode = locale.toString().toLowerCase();
        InputStream inputstream = LanguageMap.class.getResourceAsStream("/assets/minecraft/lang/" + langCode + ".lang");
        try {
            inject(this, inputstream);
        } finally {
            IOUtils.closeQuietly(inputstream); // Forge: close stream after use (MC-153470)
        }
    }

    public void injectInst(InputStream inputStream) {
        inject(this, inputStream);
    }

    public static void inject(InputStream inputstream) {
        inject(instance, inputstream);
    }

    private static void inject(LanguageMap inst, InputStream inputstream) {
        Map<String, String> map = parseLangFile(inputstream);
        inst.languageList.putAll(map);
        inst.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }

    public static Map<String, String> parseLangFile(InputStream inputstream) {
        Map<String, String> table = Maps.newHashMap();
        try {
            if (inputstream == null) return table;

            List<String> lines = IOUtils.readLines(inputstream, StandardCharsets.UTF_8.displayName());
            for (String s : lines) {
                if (!s.isEmpty() && s.charAt(0) != '#') {
                    String[] astring = Iterables.toArray(EQUAL_SIGN_SPLITTER.split(s), String.class);

                    if (astring != null && astring.length == 2) {
                        String s1 = astring[0];
                        String s2 = NUMERIC_VARIABLE_PATTERN.matcher(astring[1]).replaceAll("%$1s");
                        table.put(s1, s2);
                    }
                }
            }

        } catch (Exception ignored) {

        }
        return table;
    }

    @SuppressWarnings("unused")
    @Deprecated
    private static InputStream loadLanguage(Map<String, String> table, InputStream inputstream) {
        return inputstream;
    }

    static LanguageMap getInstance() {
        return instance;
    }

//    @SideOnly(Side.CLIENT)

    public static synchronized void replaceWith(Map<String, String> p_135063_0_) {
        instance.languageList.clear();
        instance.languageList.putAll(p_135063_0_);
        instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }

    public synchronized String translateKey(String key) {
        return this.tryTranslateKey(key);
    }

    public synchronized String translateKeyFormat(String key, Object... format) {
        String s = this.tryTranslateKey(key);

        try {
            return String.format(s, format);
        } catch (IllegalFormatException var5) {
            return "Format error: " + s;
        }
    }

    private String tryTranslateKey(String key) {
        String s = this.languageList.get(key);
        return s == null ? key : s;
    }

    public synchronized boolean isKeyTranslated(String key) {
        return this.languageList.containsKey(key);
    }

    public long getLastUpdateTimeInMilliseconds() {
        return this.lastUpdateTimeInMilliseconds;
    }
}