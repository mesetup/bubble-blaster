package qtech.bubbles.common.text.translation

import java.util.*

object I18n {
    private val localizedName: LanguageMap = LanguageMap.Companion.instance
    private val fallbackTranslator = LanguageMap(Locale("en", "us"))
    @JvmStatic
    fun translateToLocal(key: String): String? {
        return if (canTranslate(key)) {
            localizedName.translateKey(key)
        } else {
            translateToFallback(key)
        }
    }

    @JvmStatic
    fun translateToLocalFormatted(key: String, vararg format: Any?): String? {
        return if (canTranslate(key)) {
            localizedName.translateKeyFormat(key, *format)
        } else {
            translateToFallbackFormatted(key, *format)
        }
    }

    fun translateToFallback(key: String): String {
        return fallbackTranslator.translateKey(key)
    }

    fun translateToFallbackFormatted(key: String, vararg format: Any?): String {
        return fallbackTranslator.translateKeyFormat(key, *format)
    }

    fun canTranslate(key: String?): Boolean {
        return localizedName.isKeyTranslated(key)
    }

    val lastTranslationUpdateTimeInMilliseconds: Long
        get() = localizedName.lastUpdateTimeInMilliseconds
}