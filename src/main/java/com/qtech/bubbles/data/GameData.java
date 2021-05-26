package com.qtech.bubbles.data;

import org.bson.BsonDocument;
import org.bson.RawBsonDocument;
import org.bson.codecs.BsonDocumentCodec;

import java.io.*;

public class GameData {
    protected RawBsonDocument document = new RawBsonDocument(new BsonDocument(), new BsonDocumentCodec());

    protected void dump(OutputStream stream) throws IOException {
        stream.write(document.getByteBuffer().array());
    }

    protected void dump(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        dump(fos);
    }

    protected void load(InputStream stream) throws IOException {
        RawBsonDocument nbt = new RawBsonDocument(stream.readAllBytes());

        this.document.clear();

        for (String key : nbt.keySet()) {
            this.document.put(key, nbt.get(key));
        }
    }

    protected void load(File file) throws IOException {
        load(new FileInputStream(file));
    }
}
