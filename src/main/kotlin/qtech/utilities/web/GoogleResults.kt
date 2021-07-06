@file:Suppress("unused")

package qtech.utilities.web

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.Serializable
import java.net.URL

class GoogleResults internal constructor(val googleSearch: GoogleSearch) : ArrayList<GoogleResult?>(), Serializable {
    var gson: Gson? = null
    var requestUrl: URL? = null
    var isValid = false
        private set
    var exception: Throwable? = null
        private set
    private var jsonObject: JsonObject? = null
    fun setInvalid(exception: Throwable?) {
        this.exception = exception
        isValid = false
    }

    fun setJsonObject(`object`: JsonObject?) {
        jsonObject = `object`
    }

    val query: String?
        get() = googleSearch.query
    private val queries: JsonObject
        get() = jsonObject!!.getAsJsonObject("queries")
    private val request: JsonObject
        get() = queries.getAsJsonArray("request")[0].asJsonObject
    val requestTitle: String
        get() = request.getAsJsonPrimitive("title").asString
    val totalResults: Long
        get() = request.getAsJsonPrimitive("totalResults").asString.toLong()
    val resultCount: Long
        get() = request.getAsJsonPrimitive("count").asString.toLong()
    val startIndex: Long
        get() = request.getAsJsonPrimitive("count").asLong
    val inputEncoding: String
        get() = request.getAsJsonPrimitive("inputEncoding").asString
    val outputEncoding: String
        get() = request.getAsJsonPrimitive("outputEncoding").asString
    val isSafeSearch: Boolean
        get() {
            val text = request.getAsJsonPrimitive("outputEncoding").asString
            return if (text == "on") true else if (text == "off") false else false
        }
    val searchInformation: GoogleSearchInformation
        get() = GoogleSearchInformation(jsonObject!!.getAsJsonObject("searchInformation"))
    val correctedSpelling: GoogleSpelling
        get() = GoogleSpelling(jsonObject!!.getAsJsonObject("spelling"))

    fun nextPage(): GoogleSearch? {
        if (!queries.has("nextPage")) {
            return null
        }
        val json = queries.getAsJsonArray("nextPage")[0].asJsonObject
        val count = googleSearch.count
        val startIndex = json.getAsJsonPrimitive("startIndex").asLong
        return GoogleSearch(query, count, startIndex, isSafeSearch)
    }

    fun previousPage(): GoogleSearch? {
        if (!queries.has("previousPage")) {
            return null
        }
        val json = queries.getAsJsonArray("previousPage")[0].asJsonObject
        val count = googleSearch.count
        val startIndex = json.getAsJsonPrimitive("startIndex").asLong
        return GoogleSearch(query, count, startIndex, isSafeSearch)
    }

    fun hasNextPage(): Boolean {
        return queries.has("nextPage")
    }

    fun hasPreviousPage(): Boolean {
        return queries.has("previousPage")
    }
}