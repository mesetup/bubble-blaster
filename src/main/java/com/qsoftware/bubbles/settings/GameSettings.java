package com.qsoftware.bubbles.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.References;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Locale;

public class GameSettings {
    private static final GameSettings INSTANCE = new GameSettings();

    protected Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject parsed;

    public GameSettings() {
        reload();
    }

    public synchronized void reload() {
        Gson gson = new Gson();
        File settingsFile = References.SETTINGS_FILE;

        String json = "{}";

        QBubbles.getLogger().info("Reloading settings file...");

        if (!settingsFile.exists()) {
            try {
                QBubbles.getLogger().debug("Write settings file from resource...");
                Files.copy(getClass().getResourceAsStream("/settings.json"), settingsFile.toPath());
            } catch (IOException e) {
                QBubbles.getLogger().error("Cannot write settings file from resource: " + settingsFile);
                e.printStackTrace();
            }
        }

        try {
            QBubbles.getLogger().debug("Reading settings file...");
            json = Files.readString(settingsFile.toPath());
        } catch (IOException e) {
            QBubbles.getLogger().error("Cannot read settings file: " + settingsFile.toPath());
            e.printStackTrace();
        }

        boolean changed = false;
        try {
            parsed = gson.fromJson(json, JsonObject.class);
        } catch (Exception e) {
            QBubbles.getLogger().fatal("Cannot read Game-settings file. Traceback follows:");
            e.printStackTrace();
            QBubbles.getInstance().shutdown();
            return;
        }
        if (!parsed.has("lang")) {
            parsed.add("lang", new JsonPrimitive("en_us"));
            changed = true;
        }
        if (!parsed.has("max-bubbles")) {
//            parsed.add("max-bubbles", new JsonPrimitive(Runtime.getRuntime().availableProcessors() * 62));
            parsed.add("max-bubbles", new JsonPrimitive(500));
            changed = true;
        }
        if (!parsed.has("graphics")) {
//            parsed.add("max-bubbles", new JsonPrimitive(Runtime.getRuntime().availableProcessors() * 62));
            JsonObject graphics = new JsonObject();
            graphics.add("enable-antialias", new JsonPrimitive(true));
            graphics.add("enable-text-antialias", new JsonPrimitive(true));

            parsed.add("graphics", graphics);
            changed = true;
        } else {
            JsonObject graphics = parsed.getAsJsonObject("graphics");

            if (!graphics.has("enable-antialias")) {
                graphics.add("enable-antialias", new JsonPrimitive(true));
                changed = true;
            }
            if (!graphics.has("enable-text-antialias")) {
                graphics.add("enable-text-antialias", new JsonPrimitive(true));
                changed = true;
            }

            parsed.add("graphics", graphics);
        }

        if (changed) {
            save();
        }

        QBubbles.getLogger().info("Settings file reloaded!");
    }

    public synchronized void save() {
        File settingsFile = References.SETTINGS_FILE;

        QBubbles.getLogger().info("Saving settings file...");

        String json = gson.toJson(parsed, JsonObject.class);
        try {
            QBubbles.getLogger().info("Writing settings file...");
            Files.writeString(settingsFile.toPath(), json, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        } catch (IOException e) {
            QBubbles.getLogger().error("Cannot write settings file to: " + settingsFile);
            e.printStackTrace();
        }

        QBubbles.getLogger().info("Settings file saved!");
    }

    public static GameSettings instance() {
        return INSTANCE;
    }

    public int getMaxBubbles() {
        return parsed.getAsJsonPrimitive("max-bubbles").getAsInt();
    }

    public void setMaxBubbles(int value) {
        parsed.add("max-bubbles", new JsonPrimitive(value));
    }

    public String getLanguage() {
        return parsed.getAsJsonPrimitive("lang").getAsString();
    }

    public Locale getLanguageLocale() {
        return Locale.forLanguageTag(getLanguage());
    }

    public void setLanguage(String lang) {
        parsed.add("lang", new JsonPrimitive(Locale.forLanguageTag(lang).getLanguage().toLowerCase()));
    }

    public void setLanguage(Locale locale) {
        parsed.add("lang", new JsonPrimitive(locale.getLanguage().toLowerCase()));
    }

    public boolean isAntialiasEnabled() {
        return parsed.getAsJsonObject("graphics").getAsJsonPrimitive("enable-antialias").getAsBoolean();
    }

    public boolean isTextAntialiasEnabled() {
        return parsed.getAsJsonObject("graphics").getAsJsonPrimitive("enable-text-antialias").getAsBoolean();
    }

    public void setAntialiasEnabled(boolean b) {
        JsonObject graphics = parsed.getAsJsonObject("graphics");
        graphics.add("enable-antialias", new JsonPrimitive(b));
        parsed.add("graphics", graphics);
    }

    public void setTextAntialiasEnabled(boolean b) {
        JsonObject graphics = parsed.getAsJsonObject("graphics");
        graphics.add("enabled-text-antialias", new JsonPrimitive(b));
        parsed.add("graphics", graphics);
    }
}
