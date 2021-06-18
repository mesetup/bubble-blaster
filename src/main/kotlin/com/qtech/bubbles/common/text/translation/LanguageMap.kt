package com.qtech.bubbles.common.text.translation

import com.google.common.base.Splitter
import com.google.common.collect.Iterables
import com.google.common.collect.Maps
import com.qtech.bubbles.settings.GameSettings
import org.apache.commons.io.IOUtils
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.regex.Pattern

class LanguageMap(locale: Locale) {
    private val languageList: MutableMap<String?, String> = Maps.newHashMap()
    var lastUpdateTimeInMilliseconds: Long = 0
        private set

    fun injectInst(inputStream: InputStream) {
        inject(this, inputStream)
    }

    @Synchronized
    fun translateKey(key: String): String {
        return tryTranslateKey(key)
    }

    @Synchronized
    fun translateKeyFormat(key: String, vararg format: Any?): String {
        val s = tryTranslateKey(key)
        return try {
            String.format(s, *format)
        } catch (var5: IllegalFormatException) {
            "Format error: $s"
        }
    }

    private fun tryTranslateKey(key: String): String {
        val s = languageList[key]
        return s ?: key
    }

    @Synchronized
    fun isKeyTranslated(key: String?): Boolean {
        return languageList.containsKey(key)
    }

    companion object {
        private val NUMERIC_VARIABLE_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]")
        private val EQUAL_SIGN_SPLITTER = Splitter.on('=').limit(2)
        val instance = LanguageMap(GameSettings.instance().languageLocale)
        fun inject(inputStream: InputStream) {
            inject(instance, inputStream)
        }

        private fun inject(inst: LanguageMap, inputStream: InputStream?) {
            val map = parseLangFile(inputStream)
            inst.languageList.putAll(map)
            inst.lastUpdateTimeInMilliseconds = System.currentTimeMillis()
        }

        fun parseLangFile(inputStream: InputStream?): Map<String?, String> {
            val table: MutableMap<String?, String> = Maps.newHashMap()
            try {
                if (inputStream == null) return table
                val lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8.displayName())
                for (s in lines) {
                    if (s.isNotEmpty() && s[0] != '#') {
                        val string = Iterables.toArray(EQUAL_SIGN_SPLITTER.split(s), String::class.java)
                        if (string != null && string.size == 2) {
                            val s1 = string[0]
                            val s2 = NUMERIC_VARIABLE_PATTERN.matcher(string[1]).replaceAll("%$1s")
                            table[s1] = s2
                        }
                    }
                }
            } catch (ignored: Exception) {
            }
            return table
        }

        @Deprecated("", ReplaceWith("inputStream"))
        private fun loadLanguage(table: Map<String, String>, inputStream: InputStream?): InputStream? {
            return inputStream
        }

        //    @SideOnly(Side.CLIENT)
        @Synchronized
        fun replaceWith(p_135063_0_: Map<String?, String>?) {
            instance.languageList.clear()
            instance.languageList.putAll(p_135063_0_!!)
            instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis()
        }
    }

    init {
        val langCode = locale.toString().lowercase()
        val inputStream = LanguageMap::class.java.getResourceAsStream("/assets/minecraft/lang/$langCode.lang")
        try {
            inject(this, inputStream)
        } finally {
            IOUtils.closeQuietly(inputStream) // Forge: close stream after use (MC-153470)
        }
    }
}