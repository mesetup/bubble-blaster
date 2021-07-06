@file:Suppress("DEPRECATION")

package qtech.bubbles.registry

import qtech.bubbles.common.maps.SequencedHashMap
import qtech.bubbles.common.text.translation.LanguageMap
import java.util.*
import java.util.AbstractMap.SimpleEntry

open class LocaleManager protected constructor() : AbstractRegistry<Locale, LanguageMap?>() {
    /**
     * Returns the registered instance from the given [Locale]
     *
     * @param obj the namespaced key.
     * @return an registered instance of the type [LanguageMap].
     * @throws ClassCastException if the type is invalid.
     */
    override fun get(obj: Locale): LanguageMap? {
        return Companion.registry[obj]
    }

    /**
     * Register an object.
     *
     * @param key the register item key.
     * @param val the register item value.
     */
    @Deprecated("", ReplaceWith("register(locale)"))
    override fun register(key: Locale, `val`: LanguageMap?) {
        Companion.registry[key] = `val`
    }

    fun register(locale: String?) {
        val locale1 = Locale.forLanguageTag(locale)
        val languageMap = LanguageMap(locale1)
        register(locale1, languageMap)
    }

    override fun values(): ArrayList<LanguageMap?> {
        return ArrayList(Companion.registry.values)
    }

    override fun keys(): Set<Locale> {
        return Companion.registry.keys
    }

    @Throws(IllegalAccessException::class)
    override fun entries(): Set<Map.Entry<Locale, LanguageMap?>> {
        // I do this because IDE won's accept dynamic values ans keys.
        val values = ArrayList(values())
        val keys = ArrayList(keys())
        if (keys.size != values.size) throw IllegalAccessException("Keys and values have different lengths.")
        val entrySet: MutableSet<Map.Entry<Locale, LanguageMap?>> = HashSet()
        for (i in keys.indices) {
            entrySet.add(SimpleEntry(keys[i], values[i]))
        }
        return entrySet
    }

    companion object {
        //    protected final static HashBasedTable<Object, Object, Object> registries = HashBasedTable.create();
        protected val registry = SequencedHashMap<Locale, LanguageMap>()
        @JvmStatic
        val manager: LocaleManager
            get() = LocaleManager()
    }
}