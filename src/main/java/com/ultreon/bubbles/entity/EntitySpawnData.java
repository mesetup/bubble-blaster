package com.ultreon.bubbles.entity;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.command.Command;
import com.ultreon.bubbles.environment.Environment;
import lombok.NonNull;
import org.bson.BsonDocument;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class EntitySpawnData {
    private SpawnReason reason;
    @Nullable
    private Point pos;
    @Nullable
    private BsonDocument state;
    @Nullable
    private Command command;
    private Environment environment;

    public enum SpawnReason {
        LOAD, NATURAL, COMMAND
    }

    private EntitySpawnData(SpawnReason reason, @Nullable BsonDocument state, @Nullable Point pos, @Nullable Command command, Environment environment) {
        this.pos = pos;
        this.state = state;
        this.reason = reason;
        this.command = command;
        this.environment = environment;
    }

    public static EntitySpawnData fromLoadSpawn(@NonNull BsonDocument state) {
        return new EntitySpawnData(SpawnReason.LOAD, state, null, null, BubbleBlaster.getInstance().environment);
    }

    public static EntitySpawnData fromNaturalSpawn(Point pos) {
        return new EntitySpawnData(SpawnReason.NATURAL, null, pos, null, BubbleBlaster.getInstance().environment);
    }

    public static EntitySpawnData fromNaturalSpawn(Point pos, Environment environment) {
        return new EntitySpawnData(SpawnReason.NATURAL, null, pos, null, environment);
    }

    public static EntitySpawnData fromCommand(Command command) {
        return new EntitySpawnData(SpawnReason.COMMAND, null, null, command, BubbleBlaster.getInstance().environment);
    }

    public static EntitySpawnData fromCommand(Command command, Environment environment) {
        return new EntitySpawnData(SpawnReason.COMMAND, null, null, command, environment);
    }

    public SpawnReason getReason() {
        return reason;
    }

    public void setReason(SpawnReason reason) {
        this.reason = reason;
    }

    @Nullable
    public BsonDocument getState() {
        return state;
    }

    public void setState(@Nullable BsonDocument state) {
        if (this.state != null) {
            this.state = state;
        } else {
            throw new NullPointerException("Command property was initialized with null.");
        }
    }

    @Nullable
    public Command getCommand() {
        return command;
    }

    public void setCommand(@Nullable Command command) {
        if (this.state != null) {
            this.command = command;
        } else {
            throw new NullPointerException("Command property was initialized with null.");
        }
    }

    @Nullable
    public Point getPos() {
        return pos;
    }

    public void setPos(@Nullable Point pos) {
        if (this.pos != null) {
            this.pos = pos;
        } else {
            throw new NullPointerException("Command property was initialized with null.");
        }
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
