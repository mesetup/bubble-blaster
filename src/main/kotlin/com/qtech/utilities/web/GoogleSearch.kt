package com.qtech.utilities.web

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonObject
import com.qtech.utilities.core.exceptions.IllegalJsonElementInArray
import com.qtech.utilities.core.exceptions.UnsafeOperationException
import java.io.IOException
import java.io.InputStreamReader
import java.io.Serializable
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.Consumer

class GoogleSearch @JvmOverloads constructor(query: String?, count: Long = 10, startIndex: Long = 1, var isSafeSearch: Boolean = false) : Iterable<GoogleResult?>, Serializable {
    var count: Long
    var startIndex: Long
    var query: String?
    private val cache = HashMap<URL, GoogleResults>()
    val results: GoogleResults?
        get() {
            var results = GoogleResults(this)
            var url: URL
            url = try {
                URL("https://www.googleapis.com/customsearch/v1?key=" + API_KEY + "&num=" + count + "&start=" + startIndex + "&safe=" + (if (isSafeSearch) "on" else "off") + "&cx=" + SEARCH_ENGINE_ID + "&q=" + URLEncoder.encode(query,
                    StandardCharsets.UTF_8))
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                results.setInvalid(e)
                return results
            }
            return if (!cache.containsKey(url)) {
                val reader: InputStreamReader
                try {
                    reader = InputStreamReader(url.openStream(), StandardCharsets.UTF_8)
                    println(String(url.openStream().readAllBytes()))
                } catch (e: IOException) {
                    e.printStackTrace()
                    results.setInvalid(e)
                    return results
                }
                val `object`: JsonObject
                `object` = try {
                    Gson().fromJson(reader, JsonObject::class.java)
                } catch (e: JsonIOException) {
                    results.setInvalid(e)
                    return results
                }
                results.setJsonObject(`object`)
                var i = 0
                for (item in `object`.getAsJsonArray("items")) {
                    if (item is JsonObject) {
                        results.add(GoogleResult(item))
                    } else {
                        results.setInvalid(IllegalJsonElementInArray("ItemType at index $i is not a JsonObject.", i, item))
                        return results
                    }
                    i++
                }
                cache[url] = results
                results
            } else {
                try {
                    url = URL("https://www.googleapis.com/customsearch/v1?key=" + API_KEY + "&num=" + count + "&start=" + startIndex + "&safe=" + (if (isSafeSearch) "on" else "off") + "&cx=" + SEARCH_ENGINE_ID + "&q=" + URLEncoder.encode(
                        query,
                        StandardCharsets.UTF_8))
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                    results = GoogleResults(this)
                    results.setInvalid(e)
                    return results
                }
                cache[url]
            }
        }

    fun clearCache() {
        cache.clear()
    }

    /**
     * Warning: Do not use in for-loop, can cause extremely large performance issues,
     *
     * @return the google result iterator.
     */
    override fun iterator(): MutableIterator<GoogleResult?> {
        val googleSearch = GoogleSearch(query, 1, 1, isSafeSearch)
        return object : MutableIterator<GoogleResult?> {
            var currentResults = googleSearch.results
            override fun hasNext(): Boolean {
                return currentResults!!.hasNextPage()
            }

            override fun next(): GoogleResult {
                val result = currentResults!![0]
                currentResults = Objects.requireNonNull(currentResults!!.nextPage())!!.results
                return result!!
            }

            override fun remove() {
                throw UnsupportedOperationException("Modify not supported.")
            }
        }
    }

    override fun forEach(action: Consumer<in GoogleResult?>?) {
        throw UnsafeOperationException()
    }

    companion object {
        var API_KEY: String? = null
        var SEARCH_ENGINE_ID: String? = null
    }

    init {
        requireNotNull(API_KEY) { "API_KEY is not set. Set the API_KEY static field to search." }
        requireNotNull(SEARCH_ENGINE_ID) { "SEARCH_ENGINE_ID is not set. Set the SEARCH_ENGINE_ID static field to search." }
        this.query = query
        this.count = count
        this.startIndex = startIndex
    }
}