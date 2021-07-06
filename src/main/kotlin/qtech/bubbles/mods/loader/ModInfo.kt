package qtech.bubbles.mods.loader

import com.google.gson.JsonObject
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URL

@Suppress("SameParameterValue", "unused")
class ModInfo(container: ModContainer) {
    private val json: JsonObject? = container.json
    private fun getString(key: String): String {
        return json!!.getAsJsonPrimitive(key).asString
    }

    private fun getString(key: String, def: String?): String? {
        return try {
            this.getString(key)
        } catch (t: Throwable) {
            def
        }
    }

    private fun getInt(key: String): Int {
        return json!!.getAsJsonPrimitive(key).asInt
    }

    private fun getInt(key: String, def: Int): Int {
        return try {
            this.getInt(key)
        } catch (t: Throwable) {
            def
        }
    }

    private fun getLong(key: String): Long {
        return json!!.getAsJsonPrimitive(key).asLong
    }

    private fun getLong(key: String, def: Long): Long {
        return try {
            this.getLong(key)
        } catch (t: Throwable) {
            def
        }
    }

    private fun getFloat(key: String): Float {
        return json!!.getAsJsonPrimitive(key).asFloat
    }

    private fun getFloat(key: String, def: Float): Float {
        return try {
            this.getFloat(key)
        } catch (t: Throwable) {
            def
        }
    }

    private fun getDouble(key: String): Double {
        return json!!.getAsJsonPrimitive(key).asDouble
    }

    private fun getDouble(key: String, def: Double): Double {
        return try {
            this.getDouble(key)
        } catch (t: Throwable) {
            def
        }
    }

    private fun getByte(key: String): Byte {
        return json!!.getAsJsonPrimitive(key).asByte
    }

    private fun getByte(key: String, def: Byte): Byte {
        return try {
            this.getByte(key)
        } catch (t: Throwable) {
            def
        }
    }

    private fun getChar(key: String): Char {
        return json!!.getAsJsonPrimitive(key).asCharacter
    }

    private fun getChar(key: String, def: Char): Char {
        return try {
            this.getChar(key)
        } catch (t: Throwable) {
            def
        }
    }

    private fun getBigInteger(key: String): BigInteger {
        return json!!.getAsJsonPrimitive(key).asBigInteger
    }

    private fun getBigInteger(key: String, def: BigInteger): BigInteger {
        return try {
            this.getBigInteger(key)
        } catch (t: Throwable) {
            def
        }
    }

    private fun getBigDecimal(key: String): BigDecimal {
        return json!!.getAsJsonPrimitive(key).asBigDecimal
    }

    private fun getBigDecimal(key: String, def: BigDecimal): BigDecimal {
        return try {
            this.getBigDecimal(key)
        } catch (t: Throwable) {
            def
        }
    }

    private fun getShort(key: String): Short {
        return json!!.getAsJsonPrimitive(key).asShort
    }

    private fun getShort(key: String, def: Short): Short {
        return try {
            this.getShort(key)
        } catch (t: Throwable) {
            def
        }
    }

    private fun getNumber(key: String): Number {
        return json!!.getAsJsonPrimitive(key).asNumber
    }

    private fun getNumber(key: String, def: Number): Number {
        return try {
            this.getNumber(key)
        } catch (t: Throwable) {
            def
        }
    }

    private fun getBoolean(key: String): Boolean {
        return json!!.getAsJsonPrimitive(key).asBoolean
    }

    private fun getBoolean(key: String, def: Boolean): Boolean {
        return try {
            this.getBoolean(key)
        } catch (t: Throwable) {
            def
        }
    }

    private fun getURL(key: String): URL? {
        return try {
            URL(getString(key))
        } catch (t: Throwable) {
            null
        }
    }

    private fun getURL(key: String, def: URL): URL {
        return try {
            URL(getString(key, null))
        } catch (t: Throwable) {
            def
        }
    }

    val addonId: String
        get() = getString("addonId")
    val version: String
        get() = getString("version")
    val name: String
        get() = getString("name")
    val buildNumber: Int
        get() = getInt("build", -1)
    val homepage: URL?
        get() = getURL("homepage")
    val updateUrl: URL?
        get() = getURL("updateUrl")
    val description: String
        get() = getString("description")

}