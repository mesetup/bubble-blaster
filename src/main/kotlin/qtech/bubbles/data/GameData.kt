package qtech.bubbles.data

import org.bson.BsonDocument
import org.bson.RawBsonDocument
import org.bson.codecs.BsonDocumentCodec
import java.io.*

open class GameData {
    protected var document = RawBsonDocument(BsonDocument(), BsonDocumentCodec())
    @Throws(IOException::class)
    protected fun dump(stream: OutputStream) {
        stream.write(document.byteBuffer.array())
    }

    @Throws(IOException::class)
    protected fun dump(file: File) {
        val fos = FileOutputStream(file)
        dump(fos)
    }

    @Throws(IOException::class)
    protected fun load(stream: InputStream) {
        val nbt = RawBsonDocument(stream.readAllBytes())
        document.clear()
        for (key in nbt.keys) {
            document[key] = nbt[key]
        }
    }

    @Throws(IOException::class)
    protected fun load(file: File) {
        load(FileInputStream(file))
    }
}