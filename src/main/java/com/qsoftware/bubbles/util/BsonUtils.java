package com.qsoftware.bubbles.util;

import org.bson.BsonDocument;

public class BsonUtils {
    private BsonUtils() {
        throw ExceptionUtils.utilityClass();
    }

    public static BsonDocument generateTagDocument(BsonDocument document, BsonDocument tag) {
        document.put("Tag", tag);
        return document;
    }

    public static BsonDocument getTagDocument(BsonDocument document) {
        return document.getDocument("Tag");
    }
}
