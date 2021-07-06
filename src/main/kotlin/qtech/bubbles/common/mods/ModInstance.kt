package qtech.bubbles.common.mods

import qtech.bubbles.event.bus.LocalAddonEventBus
import org.apache.logging.log4j.Logger

abstract class ModInstance(val logger: Logger, val namespace: String, val `object`: ModObject<out ModInstance>) {

    // * See comment on cast
    open val eventBus: LocalAddonEventBus<out ModInstance>
        get() = `object`.eventBus
}