package com.qtech.bubbles.event.bus

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.addon.loader.AddonContainer
import com.qtech.bubbles.common.addon.AddonObject
import com.qtech.bubbles.common.addon.AddonInstance
import com.qtech.bubbles.event.Event
import com.qtech.bubbles.event.ICancellable
import java.lang.UnsupportedOperationException

class AddonEventBus : EventBus() {
    override fun <T : Event> post(event: T): Boolean {
        BubbleBlaster.instance.addonManager.containers.stream().map(AddonContainer::obj).forEach { addonInstanceObject: AddonObject<out AddonInstance>? ->
            BubbleBlaster.logger.info("Sending addon event to: " + addonInstanceObject!!.namespace)
            addonInstanceObject.eventBus.post(event)
        }
        return event is ICancellable && (event as ICancellable).isCancelled
    }

    override fun <T : Event> addListener(clazz: Class<T>, handler: (event: T) -> Unit) {
        throw UnsupportedOperationException("Not available.")
    }
}