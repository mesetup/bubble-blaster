package qtech.bubbles.event

import qtech.bubbles.mods.loader.ModLoader

/**
 * <h1>Mod Post-initialization Event</h1>
 * This event is for post-initialize addons.
 *
 * @since 1.0.0
 */
class BBAddonSetupEvent(val loader: ModLoader) : Event()