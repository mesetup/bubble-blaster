package com.qtech.bubbles.common.holders

import org.bson.BsonDocument

interface IDataHolder {
    fun write(document: BsonDocument?): BsonDocument?
    fun read(document: BsonDocument?)
}