package com.qtech.bubbles.core.common

import net.querz.nbt.io.NBTUtil
import net.querz.nbt.tag.CompoundTag
import org.bson.BsonDocument
import org.bson.BsonInt64
import org.bson.BsonString
import org.bson.RawBsonDocument
import org.bson.codecs.BsonDocumentCodec
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

open class SavedGame protected constructor(val directory: File) {
    private val path: String = directory.path
    protected val gameInfoFile: File = File(path, "info.bson")

    protected constructor(path: String) : this(File(path)) {}

    @Throws(IOException::class)
    fun loadInfoData(): BsonDocument {
        val fis = FileInputStream(gameInfoFile)
        return RawBsonDocument(fis.readAllBytes())
    }

    fun debugInfoData(): BsonDocument {
        val nbt = RawBsonDocument(BsonDocument(), BsonDocumentCodec())
        nbt["name"] = BsonString("Test Name - " + Random().nextInt())
        nbt["saveTime"] = BsonInt64(System.currentTimeMillis())
        return nbt
    }

    @Throws(IOException::class)
    fun loadData(name: String): CompoundTag {
        return this.loadData(File(path, "$name.dat"))
    }

    @Throws(IOException::class)
    private fun loadData(file: File): CompoundTag {
        return NBTUtil.read(file, true).tag as CompoundTag
    }

    @Throws(IOException::class)
    fun dumpData(name: String, data: CompoundTag) {
        this.dumpData(File(path, "$name.dat"), data)
    }

    @Throws(IOException::class)
    private fun dumpData(file: File, data: CompoundTag) {
        NBTUtil.write(data, file, true)
    }

    fun hasMainState(): Boolean {
        return hasDataFile("game")
    }

    @Suppress("SameParameterValue")
    private fun hasDataFile(name: String): Boolean {
        return hasDataFile(File(path, "$name.dat"))
    }

    private fun hasDataFile(file: File): Boolean {
        return file.exists() && file.absolutePath.startsWith(directory.absolutePath)
    }

    @Throws(IOException::class)
    fun createFolders(relPath: String?) {
        if (!Paths.get(directory.path, relPath).toFile().mkdirs()) {
            throw IOException("Failed to create directories " + Paths.get(directory.path, relPath).toFile().absolutePath)
        }
    }

    companion object {
        @JvmStatic
        fun fromFile(file: File): SavedGame {
            return SavedGame(file)
        }

        fun fromPath(file: Path): SavedGame {
            return SavedGame(file.toFile())
        }

        fun fromStringPath(file: String): SavedGame {
            return SavedGame(file)
        }
    }

}