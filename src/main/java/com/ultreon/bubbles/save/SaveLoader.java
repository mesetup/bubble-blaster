package com.ultreon.bubbles.save;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.common.References;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Save Laoder
 * Save loader for saved game instances.
 *
 * @author Qboi (Qboi)
 */
public class SaveLoader {
    // Static
    private static final SaveLoader instance = new SaveLoader();

    // Non-static.
    private final File saveDir;
    private final HashMap<String, Supplier<SavedGame>> saves = new HashMap<>();
    private final BubbleBlaster game = BubbleBlaster.instance();

    /**
     * Get the {@link SaveLoader save laoder} instance.
     *
     * @return the requested instance.
     */
    public static SaveLoader instance() {
        return instance;
    }

    /**
     * Save loader constructor.
     */
    private SaveLoader() {
        saveDir = References.SAVES_DIR;
    }

    /**
     * Get the saves directory.
     *
     * @return The saves directory.
     */
    public File getSaveDir() {
        return saveDir;
    }

    /**
     * Refresh saves index.
     */
    public void refresh() {
        File[] dirs = saveDir.listFiles();
        saves.clear();

        for (File dir : Objects.requireNonNull(dirs)) {
            Supplier<SavedGame> saveSupplier = () -> SavedGame.fromFile(dir);
            this.saves.put(dir.getName(), saveSupplier);
        }
    }

    public SavedGame getSavedGame(String name) {
        return saves.get(name).get();
    }

    /**
     * Get saved games.
     *
     * @return a collection of suppliers of saved games.
     */
    public Collection<Supplier<SavedGame>> getSaves() {
        return Collections.unmodifiableCollection(saves.values());
    }
}
