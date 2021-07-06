package qtech.utilities.web

import com.google.gson.JsonObject
import java.io.Serializable
import java.net.MalformedURLException
import java.net.URL

class GoogleResult(private val `object`: JsonObject) : Serializable {
    val title: String
        get() = `object`.getAsJsonPrimitive("title").asString
    val htmlTitle: String
        get() = `object`.getAsJsonPrimitive("htmlTitle").asString
    val snippet: String
        get() = `object`.getAsJsonPrimitive("snippet").asString
    val htmlSnippet: String
        get() = `object`.getAsJsonPrimitive("htmlSnippet").asString
    val link: URL?
        get() = try {
            URL(`object`.getAsJsonPrimitive("link").asString)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            null
        }
}