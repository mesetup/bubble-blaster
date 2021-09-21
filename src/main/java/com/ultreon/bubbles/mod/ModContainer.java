package com.ultreon.bubbles.mod;

import com.google.gson.JsonObject;
import com.ultreon.bubbles.common.mod.ModInstance;
import com.ultreon.bubbles.common.mod.ModObject;

import java.io.File;
import java.util.jar.JarFile;

/**
 * The container that wraps around mods in the system.
 * <p>
 * The philosophy is that individual mod implementation technologies should not
 * impact the actual loading and management of mod code. This interface provides
 * a mechanism by which we can wrap actual mod code so that the loader and other
 * facilities can treat mods at arms length.
 * </p>
 *
 * @author cpw
 */

public interface ModContainer {
    /**
     * The globally unique mod id for this mod
     */
    String getModId();

    ModInformation getModInfo();

    ModObject<? extends ModInstance> getModObject();

    ModInstance getModInstance();

    String getModFileId();

    /**
     * Get Json mod data.
     */
    JsonObject getModProperties();

    /**
     * The location on the file system which this mod came from
     */
    File getSource();

    JarFile getJarFile();

    Class<?> getModClass();
}