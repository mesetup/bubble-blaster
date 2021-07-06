package qtech.bubbles.common.entity

import org.bson.BsonDocument
import org.bson.BsonDouble
import org.bson.BsonString

@Suppress("UNUSED_VARIABLE")
class Modifier(private val type: ModifierType, val value: Double) {
    fun write(document: BsonDocument): BsonDocument? {
        document["name"] = BsonString(type.name)
        document["value"] = BsonDouble(value)
        return null
    }

    companion object {
        val ATTACK = ModifierType("bubbleblaster.attack")
        val DEFENSE = ModifierType("bubbleblaster.defense")
        val SCORE = ModifierType("bubbleblaster.score")
        fun read(document: BsonDocument) {
            val type: ModifierType = ModifierType.Companion.types.get(document.getString("name").value)!!
            val value = document.getDouble("value").value
        }
    }
}