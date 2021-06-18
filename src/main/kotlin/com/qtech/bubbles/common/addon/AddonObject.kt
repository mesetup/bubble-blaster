package com.qtech.bubbles.common.addon

import com.qtech.bubbles.addon.loader.AddonContainer
import com.qtech.bubbles.addon.loader.AddonManager
import com.qtech.bubbles.common.interfaces.NamespaceHolder
import com.qtech.bubbles.event.bus.LocalAddonEventBus

class AddonObject<T : AddonInstance>(
    override var namespace: String,
    val container: AddonContainer,
    val annotation: Addon,
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
            val addonInstance1: AddonInstance? = AddonManager.instance.getInstance(namespace)
            return addonInstance1!! as T
        }
}