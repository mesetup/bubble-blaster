package com.qtech.bubbles.common.addon

import com.qtech.bubbles.event.bus.LocalAddonEventBus
import org.apache.logging.log4j.Logger

abstract class AddonInstance(val logger: Logger, val namespace: String, val `object`: AddonObject<out AddonInstance>) {

    // * See comment on cast
    open val eventBus: LocalAddonEventBus<out AddonInstance>
        get() = `object`.eventBus
}