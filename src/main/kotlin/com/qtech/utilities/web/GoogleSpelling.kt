@file:Suppress("unused")

package com.qtech.utilities.web

import com.google.gson.JsonObject
import java.io.Serializable

class GoogleSpelling(jsonObject: JsonObject) : Serializable {
    val correctedSpelling: String = jsonObject["correctedSpelling"].asString

}