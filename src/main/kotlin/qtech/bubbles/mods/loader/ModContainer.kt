package qtech.bubbles.mods.loader

import com.google.gson.JsonObject
import qtech.bubbles.mods.Scanner
import qtech.bubbles.common.mods.ModObject
import qtech.bubbles.common.mods.ModInstance
import java.io.File
import java.util.jar.JarFile
import kotlin.reflect.KClass

abstract class ModContainer {
    abstract val namespace: String
    abstract val json: JsonObject?
    abstract val source: File
    abstract val jarFile: JarFile?
    abstract val info: ModInfo
    abstract val clazz: Class<*>?
    abstract val obj: ModObject<out ModInstance>
    abstract val instance: ModInstance?
    abstract val scanResult: Scanner.ScanResult
    abstract val kotlinClass: KClass<*>
}