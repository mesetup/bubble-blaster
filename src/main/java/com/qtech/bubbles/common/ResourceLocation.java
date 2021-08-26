package com.qtech.bubbles.common;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.addon.loader.AddonContainer;
import com.qtech.bubbles.addon.loader.AddonManager;
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qtech.bubbles.common.addon.AddonObject;
import com.qtech.bubbles.common.addon.QBubblesAddon;
import com.qtech.bubbles.common.exceptions.SyntaxError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.regex.Pattern;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record ResourceLocation(String namespace, String path) {
    public ResourceLocation {
        if (!Pattern.matches("[a-z][a-z0-9_]*", namespace)) {
            throw new SyntaxError("Namespace contains illegal characters: " + namespace);
        }

        if (!Pattern.matches("[a-z][a-z0-9_/]*", path)) {
            throw new SyntaxError("Path contains illegal characters: " + path);
        }

    }

    public ResourceLocation(@NotNull String name) {
        this(verify(name, 0).split(":")[0], verify(name, 1).split(":")[1]);
    }

    private static String verify(String name, int id) {
        switch (id) {
            case 0 -> Objects.requireNonNull(name, "Expected non-null string, but got a null.");
            case 1 -> {
                String[] split = name.split(":");
                if (split.length != 2) {
                    throw new SyntaxError("Resource location syntax is invalid. Expected to find 1 colon but found " + (split.length - 1) + ".");
                }
            }
        }
        return name;
    }

    public static ResourceLocation fromString(String name) {
        return new ResourceLocation(name);
    }

    public static void testNamespace(String namespace) {
        if (!Pattern.matches("[a-z_]*", namespace)) {
            throw new SyntaxError("Namespace contains illegal characters: " + namespace);
        }
    }

    @Nullable
    public Class<? extends QBubblesAddon> addonClass() {
        QBubbles instance = QBubbles.getInstance();
        AddonManager addonManager = instance.getAddonManager();
        AddonContainer containerFromId = addonManager.getContainerFromId(namespace);
        Class<? extends QBubblesAddon> addonClass;
        if (containerFromId != null) {
            AddonObject<? extends QBubblesAddon> addon = containerFromId.getAddonObject();
            addonClass = addon.getAddonClass();
        } else {
            addonClass = null;
        }

        return addonClass;
    }

    public ResourceLocation withNamespace(String namespace) {
        return new ResourceLocation(namespace, path);
    }
}
