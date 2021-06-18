package com.qtech.bubbles.save

import com.qtech.bubbles.common.References
import com.qtech.bubbles.core.common.SavedGame
import com.qtech.bubbles.core.common.SavedGame.Companion.fromFile
import java.io.File
import java.util.*
import java.util.function.Supplier

/**
 * <h1>Save Laoder</h1>
 * Save loader for saved game instances.
 *
 * @author Quinten Jungblut (Qboi)
 */
class SaveLoader private constructor() {
    /**
     * Get the saves directory.
     *
     * @return The saves directory.
     */
    // Non-static.
    val saveDir: File = References.SAVES_DIR
    private val saves = HashMap<String, Supplier<SavedGame>>()

    /**
     * Refresh saves index.
     */
    fun refresh() {
        val dirs = saveDir.listFiles()
        saves.clear()
        for (dir in Objects.requireNonNull(dirs)) {
            val saveSupplier = Supplier { fromFile(dir) }
            saves[dir.name] = saveSupplier
        }
    }

    fun getSavedGame(name: String): SavedGame {
        return saves[name]!!.get()
    }

    /**
     * Get saved games.
     *
     * @return a collection of suppliers of saved games.
     */
    fun getSaves(): Collection<Supplier<SavedGame>> {
        return Collections.unmodifiableCollection(saves.values)
    }

    companion object {
        /**
         * Get the [save laoder][SaveLoader] instance.
         *
         * @return the requested instance.
         */
        // Static
        @JvmStatic
        val instance = SaveLoader()
    }

}