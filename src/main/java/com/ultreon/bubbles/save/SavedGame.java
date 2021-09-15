package com.ultreon.bubbles.save;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.RawBsonDocument;
import org.bson.codecs.BsonDocumentCodec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

@SuppressWarnings({"unused", "FieldCanBeLocal", "SameParameterValue"})
public class SavedGame {
    private final String path;
    protected final File gameInfoFile;
    private final File directory;

    protected SavedGame(String path) {
        this(new File(path));
    }

    protected SavedGame(File directory) {
        this.path = directory.getPath();
        this.directory = directory;
        this.gameInfoFile = new File(path, "info.bson");
    }

    public static SavedGame fromFile(File file) {
        return new SavedGame(file);
    }

    public static SavedGame fromPath(Path file) {
        return new SavedGame(file.toFile());
    }

    public static SavedGame fromStringPath(String file) {
        return new SavedGame(file);
    }

    public BsonDocument loadInfoData() throws IOException {
        FileInputStream fis = new FileInputStream(gameInfoFile);
        return new RawBsonDocument(fis.readAllBytes());
    }

    public BsonDocument debugInfoData() {
        RawBsonDocument nbt = new RawBsonDocument(new BsonDocument(), new BsonDocumentCodec());
        nbt.put("name", new BsonString("Test Name - " + new Random().nextInt()));
        nbt.put("saveTime", new BsonInt64(System.currentTimeMillis()));

        return nbt;
    }

    public BsonDocument loadData(String name) throws IOException {
        return this.loadData(new File(path, name + ".bson"));
    }

    private BsonDocument loadData(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        return new RawBsonDocument(fis.readAllBytes());
    }

    public void dumpData(String name, BsonDocument data) throws IOException {
        this.dumpData(new File(path, name + ".bson"), data);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private void dumpData(File file, BsonDocument data) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        RawBsonDocument document = new RawBsonDocument(data, new BsonDocumentCodec());
        fos.write(document.getByteBuffer().array());
    }

    public File getDirectory() {
        return directory;
    }

    public boolean hasMainState() {
        return hasDataFile("game");
    }

    private boolean hasDataFile(String name) {
        return hasDataFile(new File(path, name + ".bson"));
    }

    private boolean hasDataFile(File file) {
        return file.exists() && file.getAbsolutePath().startsWith(directory.getAbsolutePath());
    }

    public void createFolders(String relPath) throws IOException {
        if (!Path.of(directory.getPath(), relPath).toFile().mkdirs()) {
            throw new IOException("Failed to create directories " + Path.of(directory.getPath(), relPath).toFile().getAbsolutePath());
        }
    }

}
