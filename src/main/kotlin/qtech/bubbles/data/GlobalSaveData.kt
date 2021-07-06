package qtech.bubbles.data

import qtech.bubbles.common.References
import org.bson.BsonDocument
import org.bson.BsonDouble
import org.bson.BsonInt64
import org.bson.RawBsonDocument
import org.bson.codecs.BsonDocumentCodec
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class GlobalSaveData : GameData() {
    var isLoaded = false
        private set

    fun dump() {
        try {
            val file = File(References.QBUBBLES_DIR, "global.bson")
            if (file.exists()) {
                if (!file.delete()) {
                    throw RuntimeException(IOException("Failed to delete file."))
                }
            }
            val fos = FileOutputStream(file)
            this.dump(fos)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun load() {
        try {
            val fis = FileInputStream(File(References.QBUBBLES_DIR, "global.bson"))
            this.load(fis)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        isLoaded = true
    }

    val highScore: Double
        get() = document.getDouble("highScore").value
    val highScoreTime: Long
        get() = document.getInt64("highScoreTime").value

    fun setHighScore(highScore: Double, time: Long) {
        if (highScore < highScore) {
            document["highScore"] = BsonDouble(highScore)
            document["highScoreTime"] = BsonInt64(time)
            dump()
        }
    }

    val isCreated: Boolean
        get() {
            val file = File(References.QBUBBLES_DIR, "global.dat")
            return file.exists()
        }

    fun create() {
        if (!isCreated) {
            val document = BsonDocument()
            document["highScore"] = BsonDouble(0.0)
            document["highScoreTime"] = BsonInt64(0L)
            this.document = RawBsonDocument(document, BsonDocumentCodec())
            dump()
        }
    }

    companion object {
        private lateinit var INSTANCE: GlobalSaveData
        @JvmStatic
        fun instance(): GlobalSaveData {
            return INSTANCE
        }
    }

    init {
        INSTANCE = this
    }
}