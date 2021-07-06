package qtech.bubbles.data

import qtech.utilities.python.builtins.ValueError

/**
 * @author Quinten Jungblut
 */
@Suppress("unused")
class SaveData {
    val saveData = HashMap<List<String>, Any>()
    fun getString(vararg path: String): String? {
        val stringArray: ArrayList<String> = ArrayList(listOf(*path))
        if (!saveData.containsKey(listOf(*path))) {
            return null
        }
        val rawData = saveData[stringArray]
        return if (rawData is String) {
            rawData
        } else {
            throw ValueError("Data is not instance of String")
        }
    }

    fun getInt(vararg path: String): Int? {
        val stringArray = ArrayList(listOf(*path))
        if (!saveData.containsKey(listOf(*path))) {
            return null
        }
        val rawData = saveData[stringArray]
        return if (rawData is Int) {
            rawData
        } else {
            throw ValueError("Data is not instance of Integer")
        }
    }

    fun getFloat(vararg path: String): Float? {
        val stringArray = ArrayList(listOf(*path))
        if (!saveData.containsKey(listOf(*path))) {
            return null
        }
        val rawData = saveData[stringArray]
        return if (rawData is Float) {
            rawData
        } else {
            throw ValueError("Data is not instance of Float")
        }
    }

    fun getDouble(vararg path: String): Double? {
        val stringArray = ArrayList(listOf(*path))
        if (!saveData.containsKey(listOf(*path))) {
            return null
        }
        val rawData = saveData[stringArray]
        return if (rawData is Double) {
            rawData
        } else {
            throw ValueError("Data is not instance of Double")
        }
    }

    fun getBoolean(vararg path: String): Boolean? {
        val stringArray = ArrayList(listOf(*path))
        if (!saveData.containsKey(listOf(*path))) {
            return null
        }
        val rawData = saveData[stringArray]
        return if (rawData is Boolean) {
            rawData
        } else {
            throw ValueError("Data is not instance of Boolean")
        }
    }

    fun getObject(vararg path: String): Any? {
        val stringArray = ArrayList(listOf(*path))
        if (!saveData.containsKey(listOf(*path))) {
            return null
        }
        val rawData = saveData[stringArray]
        return rawData ?: throw ValueError("Data is not instance of Object")
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getArrayList(vararg path: String): ArrayList<out T>? {
        val stringArray = ArrayList(listOf(*path))
        if (!saveData.containsKey(listOf(*path))) {
            return null
        }
        val rawData = saveData[stringArray]
        return if (rawData is ArrayList<*>?) {
            rawData as ArrayList<out T>?
        } else {
            throw ValueError("Data is not instance of ArrayList")
        }
    }
}