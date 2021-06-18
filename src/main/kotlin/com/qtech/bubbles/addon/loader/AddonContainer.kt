package com.qtech.bubbles.addon.loader

import com.google.gson.JsonObject
import com.qtech.bubbles.common.addon.AddonObject
import com.qtech.bubbles.common.addon.AddonInstance
import java.io.File
import java.util.jar.JarFile

abstract class AddonContainer {
    abstract val namespace: String
    abstract val json: JsonObject?
    abstract val source: File
    abstract val jarFile: JarFile?
    abstract val info: AddonInfo
    abstract val clazz: Class<*>?
    abstract val obj: AddonObject<out AddonInstance>
    abstract val instance: AddonInstance?
}