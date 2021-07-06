@file:Suppress("unused")

package qtech.bubbles.settings

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import qtech.bubbles.BubbleBlaster
import qtech.bubbles.common.References
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.util.*

open class GameSettings {
    protected val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private lateinit var parsed: JsonObject
    @Synchronized
    fun reload() {
        val gson = Gson()
        val settingsFile = References.SETTINGS_FILE
        var json: String? = "{}"
        BubbleBlaster.logger.info("Reloading settings file...")
        if (!settingsFile.exists()) {
            try {
                BubbleBlaster.logger.debug("Write settings file from resource...")
                Files.copy(Objects.requireNonNull(javaClass.getResourceAsStream("/settings.json")), settingsFile.toPath())
            } catch (e: IOException) {
                BubbleBlaster.logger.error("Cannot write settings file from resource: $settingsFile")
                e.printStackTrace()
            }
        }
        try {
            BubbleBlaster.logger.debug("Reading settings file...")
            json = Files.readString(settingsFile.toPath())
        } catch (e: IOException) {
            BubbleBlaster.logger.error("Cannot read settings file: " + settingsFile.toPath())
            e.printStackTrace()
        }
        var changed = false
        parsed = try {
            gson.fromJson(json, JsonObject::class.java)
        } catch (e: Exception) {
            BubbleBlaster.logger.fatal("Cannot read Game-settings file. Traceback follows:")
            e.printStackTrace()
            BubbleBlaster.instance.shutdown()
            return
        }
        if (!parsed.has("lang")) {
            parsed.add("lang", JsonPrimitive("en_us"))
            changed = true
        }
        if (!parsed.has("max-bubbles")) {
//            parsed.add("max-bubbles", new JsonPrimitive(Runtime.getRuntime().availableProcessors() * 62));
            parsed.add("max-bubbles", JsonPrimitive(500))
            changed = true
        }
        if (!parsed.has("graphics")) {
//            parsed.add("max-bubbles", new JsonPrimitive(Runtime.getRuntime().availableProcessors() * 62));
            val graphics = JsonObject()
            graphics.add("enable-antialias", JsonPrimitive(true))
            graphics.add("enable-text-antialias", JsonPrimitive(true))
            parsed.add("graphics", graphics)
            changed = true
        } else {
            val graphics = parsed.getAsJsonObject("graphics")
            if (!graphics.has("enable-antialias")) {
                graphics.add("enable-antialias", JsonPrimitive(true))
                changed = true
            }
            if (!graphics.has("enable-text-antialias")) {
                graphics.add("enable-text-antialias", JsonPrimitive(true))
                changed = true
            }
            parsed.add("graphics", graphics)
        }
        if (changed) {
            save()
        }
        BubbleBlaster.logger.info("Settings file reloaded!")
    }

    @Synchronized
    fun save() {
        val settingsFile = References.SETTINGS_FILE
        BubbleBlaster.logger.info("Saving settings file...")
        val json = gson.toJson(parsed, JsonObject::class.java)
        try {
            BubbleBlaster.logger.info("Writing settings file...")
            Files.writeString(settingsFile.toPath(), json, StandardCharsets.UTF_8, StandardOpenOption.CREATE)
        } catch (e: IOException) {
            BubbleBlaster.logger.error("Cannot write settings file to: $settingsFile")
            e.printStackTrace()
        }
        BubbleBlaster.logger.info("Settings file saved!")
    }

    var maxBubbles: Int
        get() = parsed.getAsJsonPrimitive("max-bubbles").asInt
        set(value) {
            parsed.add("max-bubbles", JsonPrimitive(value))
        }
    var language: String?
        get() = parsed.getAsJsonPrimitive("lang").asString
        set(lang) {
            parsed.add("lang", JsonPrimitive(Locale.forLanguageTag(lang).language.lowercase()))
        }
    val languageLocale: Locale
        get() = Locale.forLanguageTag(language)

    fun setLanguage(locale: Locale) {
        parsed.add("lang", JsonPrimitive(locale.language.lowercase()))
    }

    var isAntialiasEnabled: Boolean
        get() = parsed.getAsJsonObject("graphics").getAsJsonPrimitive("enable-antialias").asBoolean
        set(b) {
            val graphics = parsed.getAsJsonObject("graphics")
            graphics.add("enable-antialias", JsonPrimitive(b))
            parsed.add("graphics", graphics)
        }
    var isTextAntialiasEnabled: Boolean
        get() = parsed.getAsJsonObject("graphics").getAsJsonPrimitive("enable-text-antialias").asBoolean
        set(b) {
            val graphics = parsed.getAsJsonObject("graphics")
            graphics.add("enabled-text-antialias", JsonPrimitive(b))
            parsed.add("graphics", graphics)
        }

    companion object {
        private val INSTANCE = GameSettings()
        @JvmStatic
        fun instance(): GameSettings {
            return INSTANCE
        }
    }

    init {
        reload()
    }
}