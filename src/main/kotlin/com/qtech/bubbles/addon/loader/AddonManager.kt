@file:Suppress("unused")

package com.qtech.bubbles.addon.loader

import com.qtech.bubbles.common.addon.AddonObject
import com.qtech.bubbles.common.addon.AddonInstance
import com.qtech.bubbles.common.maps.SequencedHashMap

class AddonManager private constructor() {
    private val namespace2container: SequencedHashMap<String, AddonContainer> = SequencedHashMap()
    private val namespace2instance: SequencedHashMap<String, AddonInstance> = SequencedHashMap()
    private val namespace2object: SequencedHashMap<String, AddonObject<*>> = SequencedHashMap()
    private val namespace2info: SequencedHashMap<String, AddonInfo> = SequencedHashMap()
    private val container2instance: SequencedHashMap<AddonContainer, AddonInstance> = SequencedHashMap()
    private val container2object: SequencedHashMap<AddonContainer, AddonObject<*>> = SequencedHashMap()
    private val container2info: SequencedHashMap<AddonContainer, AddonInfo> = SequencedHashMap()
    private val container2namespace: SequencedHashMap<AddonContainer, String> = SequencedHashMap()
    private val instance2object: SequencedHashMap<AddonInstance, AddonObject<*>> = SequencedHashMap()
    private val instance2info: SequencedHashMap<AddonInstance, AddonInfo> = SequencedHashMap()
    private val instance2namespace: SequencedHashMap<AddonInstance, String> = SequencedHashMap()
    private val instance2container: SequencedHashMap<AddonInstance, AddonContainer> = SequencedHashMap()
    private val object2info: SequencedHashMap<AddonObject<*>, AddonInfo> = SequencedHashMap()
    private val object2namespace: SequencedHashMap<AddonObject<*>, String> = SequencedHashMap()
    private val object2container: SequencedHashMap<AddonObject<*>, AddonContainer> = SequencedHashMap()
    private val object2instance: SequencedHashMap<AddonObject<*>, AddonInstance> = SequencedHashMap()
    private val info2namespace: SequencedHashMap<AddonInfo, String> = SequencedHashMap()
    private val info2container: SequencedHashMap<AddonInfo, AddonContainer> = SequencedHashMap()
    private val info2instance: SequencedHashMap<AddonInfo, AddonInstance> = SequencedHashMap()
    private val info2object: SequencedHashMap<AddonInfo, AddonObject<*>> = SequencedHashMap()

    fun register(namespace: String, container: AddonContainer) {
        namespace2container[namespace] = container
        namespace2object[namespace] = container.obj
        namespace2info[namespace] = container.info

        container2object[container] = container.obj
        container2info[container] = container.info
        container2namespace[container] = namespace

        object2container[container.obj] = container
        object2info[container.obj] = container.info
        object2namespace[container.obj] = namespace

        info2container[container.info] = container
        info2object[container.info] = container.obj
        info2namespace[container.info] = namespace
    }

    fun register(namespace: String, instance: AddonInstance) {
        instance2container[instance] = instance.`object`.container
        instance2object[instance] = instance.`object`
        instance2info[instance] = instance.`object`.container.info
        instance2namespace[instance] = namespace

        container2instance[instance.`object`.container] = instance
        object2instance[instance.`object`] = instance
        info2instance[instance.`object`.container.info] = instance
        namespace2instance[namespace] = instance
    }

    @Deprecated("Replaced", ReplaceWith("getContainer(namespace)"))
    fun getContainerFromId(namespace: String): AddonContainer? {
        return getContainer(namespace)
    }

    val namespaces: MutableSet<String>
        get() {
            return namespace2container.keys
        }

    val containers: MutableSet<AddonContainer>
        get() {
            return container2namespace.keys
        }

    val objects: MutableSet<AddonObject<*>>
        get() {
            return object2container.keys
        }

    val infos: MutableSet<AddonInfo>
        get() {
            return info2container.keys
        }

    val instances: MutableSet<AddonInstance>
        get() {
            return instance2container.keys
        }

    fun getNamespace(namespace: String): String {
        return namespace
    }

    fun getNamespace(container: AddonContainer): String? {
        return container2namespace[container]
    }

    fun getNamespace(obj: AddonObject<*>): String? {
        return object2namespace[obj]
    }

    fun getNamespace(info: AddonInfo): String? {
        return info2namespace[info]
    }

    fun getNamespace(instance: AddonInstance): String? {
        return instance2namespace[instance]
    }

    fun getContainer(namespace: String): AddonContainer? {
        return namespace2container[namespace]
    }

    fun getContainer(container: AddonContainer): AddonContainer {
        return container
    }

    fun getContainer(obj: AddonObject<*>): AddonContainer? {
        return object2container[obj]
    }

    fun getContainer(info: AddonInfo): AddonContainer? {
        return info2container[info]
    }

    fun getContainer(instance: AddonInstance): AddonContainer? {
        return instance2container[instance]
    }

    fun getObject(namespace: String): AddonObject<*>? {
        return if (!namespace2object.containsKey(namespace)) {
            null
        } else namespace2object[namespace]
    }

    fun getObject(container: AddonContainer): AddonObject<*>? {
        return container2object[container]
    }

    fun getObject(obj: AddonObject<*>): AddonObject<*> {
        return obj
    }

    fun getObject(info: AddonInfo): AddonObject<*>? {
        return info2object[info]
    }

    fun getObject(instance: AddonInstance): AddonObject<*>? {
        return instance2object[instance]
    }

    fun getInfo(namespace: String): AddonInfo? {
        return namespace2info[namespace]
    }

    fun getInfo(container: AddonContainer): AddonInfo? {
        return container2info[container]
    }

    fun getInfo(obj: AddonObject<*>): AddonInfo? {
        return object2info[obj]
    }

    fun getInfo(info: AddonInfo): AddonInfo {
        return info
    }

    fun getInfo(instance: AddonInstance): AddonInfo? {
        return instance2info[instance]
    }

    fun getInstance(namespace: String): AddonInstance? {
        return namespace2instance[namespace]
    }

    fun getInstance(container: AddonContainer): AddonInstance? {
        return container2instance[container]
    }

    fun getInstance(obj: AddonObject<*>): AddonInstance? {
        return object2instance[obj]
    }

    fun getInstance(info: AddonInfo): AddonInstance? {
        return info2instance[info]
    }

    fun getInstance(instance: AddonInstance): AddonInstance {
        return instance
    }

    @Deprecated("", ReplaceWith("register(addonObject.namespace, addonObject.container)"))
    fun registerAddonObject(addonObject: AddonObject<*>) {
        register(addonObject.namespace, addonObject.container)
    }

    @Deprecated("", ReplaceWith("register(addon.namespace, addon.`object`.container)"))
    fun registerAddon(addonInstance: AddonInstance) {
        register(addonInstance.namespace, addonInstance.`object`.container)
    }

    companion object {
        val instance = AddonManager()
        @Deprecated("Replaced", ReplaceWith("instance.register(container.namespace, container)", "com.qtech.bubbles.addon.loader.AddonManager.Companion.instance"))
        fun registerContainer(container: AddonContainer) {
            instance.register(container.namespace, container)
        }
    }

}