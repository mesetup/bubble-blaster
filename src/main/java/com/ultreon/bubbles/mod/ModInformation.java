package com.ultreon.bubbles.mod;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;

@SuppressWarnings({"unused", "SameParameterValue"})
public class ModInformation {
    private final JsonObject json;

    public ModInformation(ModContainer container) {
        this.json = container.getModProperties();
    }

    protected JsonObject getJson() {
        return json;
    }

    private String getString(String key) {
        return json.getAsJsonPrimitive(key).getAsString();
    }

    private String getString(String key, String def) {
        try {
            return this.getString(key);
        } catch (Throwable t) {
            return def;
        }
    }

    private int getInt(String key) {
        return json.getAsJsonPrimitive(key).getAsInt();
    }

    private int getInt(String key, int def) {
        try {
            return this.getInt(key);
        } catch (Throwable t) {
            return def;
        }
    }

    private long getLong(String key) {
        return json.getAsJsonPrimitive(key).getAsLong();
    }

    private long getLong(String key, long def) {
        try {
            return this.getLong(key);
        } catch (Throwable t) {
            return def;
        }
    }

    private float getFloat(String key) {
        return json.getAsJsonPrimitive(key).getAsFloat();
    }

    private float getFloat(String key, float def) {
        try {
            return this.getFloat(key);
        } catch (Throwable t) {
            return def;
        }
    }

    private double getDouble(String key) {
        return json.getAsJsonPrimitive(key).getAsDouble();
    }

    private double getDouble(String key, double def) {
        try {
            return this.getDouble(key);
        } catch (Throwable t) {
            return def;
        }
    }

    private byte getByte(String key) {
        return json.getAsJsonPrimitive(key).getAsByte();
    }

    private byte getByte(String key, byte def) {
        try {
            return this.getByte(key);
        } catch (Throwable t) {
            return def;
        }
    }

    private char getChar(String key) {
        return json.getAsJsonPrimitive(key).getAsCharacter();
    }

    private char getChar(String key, char def) {
        try {
            return this.getChar(key);
        } catch (Throwable t) {
            return def;
        }
    }

    private BigInteger getBigInteger(String key) {
        return json.getAsJsonPrimitive(key).getAsBigInteger();
    }

    private BigInteger getBigInteger(String key, BigInteger def) {
        try {
            return this.getBigInteger(key);
        } catch (Throwable t) {
            return def;
        }
    }

    private BigDecimal getBigDecimal(String key) {
        return json.getAsJsonPrimitive(key).getAsBigDecimal();
    }

    private BigDecimal getBigDecimal(String key, BigDecimal def) {
        try {
            return this.getBigDecimal(key);
        } catch (Throwable t) {
            return def;
        }
    }

    private short getShort(String key) {
        return json.getAsJsonPrimitive(key).getAsShort();
    }

    private short getShort(String key, short def) {
        try {
            return this.getShort(key);
        } catch (Throwable t) {
            return def;
        }
    }

    private Number getNumber(String key) {
        return json.getAsJsonPrimitive(key).getAsNumber();
    }

    private Number getNumber(String key, Number def) {
        try {
            return this.getNumber(key);
        } catch (Throwable t) {
            return def;
        }
    }

    private boolean getBoolean(String key) {
        return json.getAsJsonPrimitive(key).getAsBoolean();
    }

    private boolean getBoolean(String key, boolean def) {
        try {
            return this.getBoolean(key);
        } catch (Throwable t) {
            return def;
        }
    }

    private URL getURL(String key) {
        try {
            return new URL(getString("updateUrl"));
        } catch (Throwable t) {
            return null;
        }
    }

    private URL getURL(String key, URL def) {
        try {
            return new URL(getString("updateUrl", null));
        } catch (Throwable t) {
            return def;
        }
    }

    public String getModId() {
        return getString("modId");
    }

    public String getVersion() {
        return getString("version");
    }

    public String getName() {
        return getString("name");
    }

    public int getBuildNumber() {
        return getInt("build", -1);
    }

    @Nullable
    public URL getHomepage() {
        return getURL("homepage");
    }

    @Nullable
    public URL getUpdateUrl() {
        return getURL("updateUrl");
    }

    public String getDescription() {
        return getString("description");
    }
}
