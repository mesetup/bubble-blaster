package qtech.bubbles.common.mods

import qtech.bubbles.mods.loader.ModContainer
import qtech.bubbles.mods.loader.ModManager
import qtech.bubbles.common.interfaces.NamespaceHolder
import qtech.bubbles.event.bus.LocalAddonEventBus

class ModObject<T : ModInstance>(
    override var modId: String,
    val container: ModContainer,
    val annotation: Modification,
    val addonClass: Class<T>
) : NamespaceHolder {
    var instance: T? = null
        set(value) {
            field = value
        }
    var eventBus: LocalAddonEventBus<T> = LocalAddonEventBus(this)
        private set

    @Suppress("UNCHECKED_CAST")
    val addon: T
        get() {
            val modInstance1: ModInstance? = ModManager.instance.getInstance(modId)
            return modInstance1!! as T
        }
}