package com.qtech.bubbles.event

import com.qtech.bubbles.addon.loader.AddonLoader

/**
 * <h1>Addon Post-initialization Event</h1>
 * This event is for post-initialize addons.
 *
 * @since 1.0.0
 */
class BBAddonSetupEvent(val loader: AddonLoader) : Event()