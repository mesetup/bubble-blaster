package com.qtech.bubbles.addon.loader;

import com.google.gson.JsonObject;
import com.qtech.bubbles.common.mod.ModInstance;
import com.qtech.bubbles.common.mod.ModObject;

import java.io.File;
import java.util.jar.JarFile;

//import net.minecraftforge.fml.common.versioning.ArtifactVersion;
//import net.minecraftforge.fml.common.versioning.VersionRange;

/**
 * The container that wraps around addons in the system.
 * <p>
 * The philosophy is that individual addon implementation technologies should not
 * impact the actual loading and management of addon code. This interface provides
 * a mechanism by which we can wrap actual addon code so that the loader and other
 * facilities can treat addons at arms length.
 * </p>
 *
 * @author cpw
 */

public interface AddonContainer {
    /**
     * The globally unique addonid for this addon
     */
    String getAddonId();

    /**
     * Get Json addon data.
     */
    JsonObject getJson();

    /**
     * The location on the file system which this addon came from
     */
    File getSource();

    JarFile getJarFile();

    AddonInfo getAddonInfo();

    Class<?> getAddonClass();

    ModObject<? extends ModInstance> getAddonObject();

    ModInstance getJavaAddon();
}