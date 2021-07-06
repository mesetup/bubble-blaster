@file:Suppress("unused")

package qtech.utilities.web

import com.google.gson.JsonObject
import java.io.Serializable

class GoogleSearchInformation(searchInformation: JsonObject) : Serializable {
    val searchTime: Double = searchInformation["searchTime"].asDouble
    val totalResults: Long = searchInformation["totalResults"].asString.toLong()

}