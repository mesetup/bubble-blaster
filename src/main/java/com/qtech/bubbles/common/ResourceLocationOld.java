package com.qtech.bubbles.common;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.addon.loader.AddonContainer;
import com.qtech.bubbles.addon.loader.AddonManager;
import com.qtech.bubbles.common.addon.AddonObject;
import com.qtech.bubbles.common.addon.QBubblesAddon;
import com.qtech.bubbles.util.TextUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
@Deprecated
public class ResourceLocationOld {
    private final String path;
    @Nullable
    private final String namespace;
    @Nullable
    private final Class<? extends QBubblesAddon> addonClass;

    private static final String pattern = "([a-z_]*):([a-z_/]*)";

    public ResourceLocationOld(@NotNull String s) {
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(s);
        if (m.find()) {
            String namespace = m.group(1);
            String path = m.group(2);

            this.namespace = namespace;
            this.path = StringUtils.stripEnd(path, "/");
        } else {
            throw new IllegalArgumentException("Could not recognize resource location from string \"" + TextUtils.getRepresentationString(s) + "\"");
        }

        if (namespace != null) {
            QBubbles instance = QBubbles.getInstance();
            AddonManager addonManager = instance.getAddonManager();
            AddonContainer containerFromId = addonManager.getContainerFromId(namespace);
            if (containerFromId != null) {
                AddonObject<? extends QBubblesAddon> addon = containerFromId.getAddonObject();
                addonClass = addon.getAddonClass();
            } else {
                addonClass = null;
            }
        } else {
            addonClass = null;
        }
    }

    private static String getNamespace(String s) {
        String[] split = s.split(":", 2);
        return split[0];
    }

    private static String getPath(String s) {
        String[] split = s.split(":", 2);
        return split[1];
    }

    public ResourceLocationOld(@Nullable String namespace, String path) {
        path = StringUtils.stripEnd(path, "/");

        testNamespace(namespace);
        testPath(path);

        this.path = path;
        this.namespace = namespace;
//        this.url = namespace == null ? null : "assets/" + namespace + "/" + path;

        if (namespace != null) {
            QBubbles instance = QBubbles.getInstance();
            AddonManager addonManager = instance.getAddonManager();
            AddonContainer addonContainer = addonManager.getContainerFromId(namespace);
            if (addonContainer != null) {
                AddonObject<? extends QBubblesAddon> addon = addonContainer.getAddonObject();
                addonClass = addon.getAddonClass();
            } else {
                addonClass = null;
            }
        } else {
            addonClass = null;
        }
    }

    public static ResourceLocation fromString(String s) {
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(s);
        if (m.find()) {
            String namespace = m.group(1);
            String path = m.group(2);

            path = StringUtils.stripEnd(path, "/");

            return new ResourceLocation(namespace, path);
        } else {
            throw new IllegalArgumentException("Could not recognize resource location from regex (" + pattern + ")");
        }
    }

    public static void testNamespace(String val) {
        if (val == null) {
            return;
        }

        // String to be scanned to find the pattern.
        String pattern = "[a-z_]*";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(val);
        if (!m.find()) {
            throw new IllegalArgumentException("Namespace has an invalid format (" + pattern + ")");
        }
    }

    public static void testPath(String val) {
        // String to be scanned to find the pattern.
        String pattern = "[a-z_/]*";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(val);
        if (!m.find()) {
            throw new IllegalArgumentException("Path has an invalid format (" + pattern + ")");
        }
    }

    @Nullable
    public InputStream getResourceAsStream(DataType type, String objectPath, String ext) {
        if (type.getPath() == null) {
            return null;
        }

        if (addonClass == null) {
            return null;
        }

        return addonClass.getResourceAsStream(type.getPath() + "/" + namespace + "/" + objectPath + "/" + path + ext);
    }

    @Nullable
    public URL getResourceURL(DataType type, String objectPath, String ext) {
        if (type.getPath() == null) {
            return null;
        }

        if (addonClass == null) {
            return null;
        }

        return addonClass.getResource(type.getPath() + "/" + namespace + "/" + objectPath + "/" + path + ext);
    }

    @Nullable
    public Enumeration<URL> listDir(DataType type, String objectPath) throws IOException {
        if (type.getPath() == null) {
            return null;
        }

        if (addonClass == null) {
            return null;
        }

        return addonClass.getClassLoader().getResources(type.getPath() + "/" + namespace + "/" + objectPath + "/" + path);
    }

    public String getPath() {
        return path;
    }

    @Nullable
    public String getNamespace() {
        return namespace;
    }

    public ResourceLocation withNamespace(@NotNull String namespace) {
        if (this.namespace != null) {
            throw new IllegalArgumentException("Namespace already defined");
        }

        return new ResourceLocation(namespace, path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceLocationOld that = (ResourceLocationOld) o;
        return Objects.equals(getPath(), that.getPath()) &&
                Objects.equals(getNamespace(), that.getNamespace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath(), getNamespace());
    }

    @Override
    public String toString() {
        if (namespace == null) {
            return path;
        }

        return namespace + ":" + path;
    }

    public String toString(@NotNull DataType type) {
        if (namespace == null) {
            return type.getPath() + "@" + path;
        }

        return type.getPath() + "@" + namespace + ":" + path;
    }
}
