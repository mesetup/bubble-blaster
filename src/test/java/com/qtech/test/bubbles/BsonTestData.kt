package com.qtech.test.bubbles

import org.bson.*
import org.bson.codecs.BsonDocumentCodec
import org.bson.types.Decimal128
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigDecimal

object BsonTestData {
    @JvmStatic
    fun main(args: Array<String>) {
        val document = BsonDocument()
        document["String"] = BsonString("Test String")
        document["Integer"] = BsonInt32(123456789)
        document["Long"] = BsonInt64(123456789)
        document["Double"] = BsonDouble(01234.56789)
        document["BigDecimal"] = BsonDecimal128(Decimal128(BigDecimal("01234.56789")))
        document["Boolean"] = BsonBoolean(true)
        val array = BsonArray()
        array.add(BsonInt32(75763242))
        array.add(BsonInt64(756298648239654823L))
        array.add(BsonBoolean(false))
        array.add(BsonString("TEST STRING IN A ARRAY"))
        array.add(BsonString("Test String In a Array"))
        val array1 = BsonArray()
        array1.add(BsonString("Array in a Array in a Document."))
        array.add(array1)
        val document1 = BsonDocument()
        document1["Lol"] = BsonString("Is a Lol Document")
        document1["Int32"] = BsonInt32(4224)
        array.add(document1)
        document["Boolean"] = array
        val document2 = BsonDocument()
        document2["Docx"] = BsonString("Document in a Document, may be need MS Office Word?")
        println(document.toJson())
        val doc = RawBsonDocument(document, BsonDocumentCodec())
        try {
            val fos = FileOutputStream("test.bson")
            fos.write(doc.byteBuffer.array())
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            val fis = FileInputStream("test.bson")
            val document3 = RawBsonDocument(fis.readAllBytes())
            println(document3.toJson())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}