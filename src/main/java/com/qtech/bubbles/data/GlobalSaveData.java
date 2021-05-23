package com.qtech.bubbles.data;

import com.qtech.bubbles.common.References;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt64;
import org.bson.RawBsonDocument;
import org.bson.codecs.BsonDocumentCodec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("unused")
public class GlobalSaveData extends GameData {
    private static GlobalSaveData INSTANCE;
    private boolean loaded = false;

    public static GlobalSaveData instance() {
        return INSTANCE;
    }

    public GlobalSaveData() {
        INSTANCE = this;
    }

    public void dump() {
        try {
            File file = new File(References.QBUBBLES_DIR, "global.bson");
            if (file.exists()) {
                if (!file.delete()) {
                    throw new RuntimeException(new IOException("Failed to delete file."));
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            this.dump(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            FileInputStream fis = new FileInputStream(new File(References.QBUBBLES_DIR, "global.bson"));
            this.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loaded = true;
    }

    public double getHighScore() {
        return document.getDouble("highScore").getValue();
    }

    public long getHighScoreTime() {
        return document.getInt64("highScoreTime").getValue();
    }

    public void setHighScore(double highScore, long time) {
        if (getHighScore() < highScore) {
            document.put("highScore", new BsonDouble(highScore));
            document.put("highScoreTime", new BsonInt64(time));

            dump();
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isCreated() {
        File file = new File(References.QBUBBLES_DIR, "global.dat");
        return file.exists();
    }

    public void create() {
        if (!isCreated()) {
            BsonDocument document = new BsonDocument();
            document.put("highScore", new BsonDouble(0.0d));
            document.put("highScoreTime", new BsonInt64(0L));

            this.document = new RawBsonDocument(document, new BsonDocumentCodec());

            dump();
        }
    }
}
