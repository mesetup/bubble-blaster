package qtech.bubbles.event

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.mods.loader.ModContainer
import qtech.bubbles.common.mods.ModObject
import qtech.bubbles.common.mods.ModInstance
import qtech.bubbles.event.bus.AddonEventBus
import qtech.bubbles.event.bus.BBEventBus
import qtech.bubbles.event.bus.LocalAddonEventBus

object Bus {
    @kotlin.jvm.JvmStatic
    val addonEventBus = AddonEventBus()
    fun getLocalAddonEventBus(addonId: String): LocalAddonEventBus<*> {
        val container: ModContainer? = BubbleBlaster.instance.modManager.getContainer(addonId)
        val modInstanceObject: ModObject<out ModInstance> = container!!.obj
        return modInstanceObject.eventBus
    }

    fun getLocalAddonEventBus(modInstance: ModInstance): LocalAddonEventBus<out ModInstance> {
        val addonObject = modInstance.`object`
        return addonObject.eventBus
    }

    fun <T : ModInstance> getLocalAddonEventBus(modObject: ModObject<T>): LocalAddonEventBus<T> {
        return modObject.eventBus
    }

    @kotlin.jvm.JvmStatic
    val qBubblesEventBus: BBEventBus
        get() = BubbleBlaster.eventBus
}