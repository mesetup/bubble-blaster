package com.qtech.bubbles.event

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.addon.loader.AddonContainer
import com.qtech.bubbles.common.addon.AddonObject
import com.qtech.bubbles.common.addon.AddonInstance
import com.qtech.bubbles.event.bus.AddonEventBus
import com.qtech.bubbles.event.bus.BBEventBus
import com.qtech.bubbles.event.bus.LocalAddonEventBus

object Bus {
    @kotlin.jvm.JvmStatic
    val addonEventBus = AddonEventBus()
    fun getLocalAddonEventBus(addonId: String): LocalAddonEventBus<*> {
        val container: AddonContainer? = BubbleBlaster.instance.addonManager.getContainer(addonId)
        val addonInstanceObject: AddonObject<out AddonInstance> = container!!.obj
        return addonInstanceObject.eventBus
    }

    fun getLocalAddonEventBus(addonInstance: AddonInstance): LocalAddonEventBus<out AddonInstance> {
        val addonObject = addonInstance.`object`
        return addonObject.eventBus
    }

    fun <T : AddonInstance> getLocalAddonEventBus(addonObject: AddonObject<T>): LocalAddonEventBus<T> {
        return addonObject.eventBus
    }

    @kotlin.jvm.JvmStatic
    val qBubblesEventBus: BBEventBus
        get() = BubbleBlaster.eventBus
}