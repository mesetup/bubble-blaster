package com.qtech.bubbles.datapack

import com.google.gson.Gson

abstract class BaseData(val properties: DataProperties) {

    //        resource.openStream();
    val gson: Gson?
        get() =//        resource.openStream();
            null
}