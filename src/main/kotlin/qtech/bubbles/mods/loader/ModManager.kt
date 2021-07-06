@file:Suppress("unused")

package qtech.bubbles.mods.loader

import qtech.bubbles.common.mods.ModObject
import qtech.bubbles.common.mods.ModInstance
import qtech.bubbles.common.maps.SequencedHashMap

class ModManager private constructor() {
    private val namespace2container: SequencedHashMap<String, ModContainer> = SequencedHashMap()
    private val namespace2instance: SequencedHashMap<String, ModInstance> = SequencedHashMap()
    private val namespace2object: SequencedHashMap<String, ModObject<*>> = SequencedHashMap()
    private val namespace2info: SequencedHashMap<String, ModInfo> = SequencedHashMap()
    private val container2instance: SequencedHashMap<ModContainer, ModInstance> = SequencedHashMap()
    private val container2object: SequencedHashMap<ModContainer, ModObject<*>> = SequencedHashMap()
    private val container2info: SequencedHashMap<ModContainer, ModInfo> = SequencedHashMap()
    private val container2namespace: SequencedHashMap<ModContainer, String> = SequencedHashMap()
    private val instance2object: SequencedHashMap<ModInstance, ModObject<*>> = SequencedHashMap()
    private val instance2info: SequencedHashMap<ModInstance, ModInfo> = SequencedHashMap()
    private val instance2namespace: SequencedHashMap<ModInstance, String> = SequencedHashMap()
    private val instance2container: SequencedHashMap<ModInstance, ModContainer> = SequencedHashMap()
    private val object2info: SequencedHashMap<ModObject<*>, ModInfo> = SequencedHashMap()
    private val object2namespace: SequencedHashMap<ModObject<*>, String> = SequencedHashMap()
    private val object2container: SequencedHashMap<ModObject<*>, ModContainer> = SequencedHashMap()
    private val object2instance: SequencedHashMap<ModObject<*>, ModInstance> = SequencedHashMap()
    private val info2namespace: SequencedHashMap<ModInfo, String> = SequencedHashMap()
    private val info2container: SequencedHashMap<ModInfo, ModContainer> = SequencedHashMap()
    private val info2instance: SequencedHashMap<ModInfo, ModInstance> = SequencedHashMap()
    private val info2object: SequencedHashMap<ModInfo, ModObject<*>> = SequencedHashMap()

    fun register(namespace: String, container: ModContainer) {
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

    fun register(namespace: String, instance: ModInstance) {
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
    fun getContainerFromId(namespace: String): ModContainer? {
        return getContainer(namespace)
    }

    val namespaces: MutableSet<String>
        get() {
            return namespace2container.keys
        }

    val containers: MutableSet<ModContainer>
        get() {
            return container2namespace.keys
        }

    val objects: MutableSet<ModObject<*>>
        get() {
            return object2container.keys
        }

    val infos: MutableSet<ModInfo>
        get() {
            return info2container.keys
        }

    val instances: MutableSet<ModInstance>
        get() {
            return instance2container.keys
        }

    fun getNamespace(namespace: String): String {
        return namespace
    }

    fun getNamespace(container: ModContainer): String? {
        return container2namespace[container]
    }

    fun getNamespace(obj: ModObject<*>): String? {
        return object2namespace[obj]
    }

    fun getNamespace(info: ModInfo): String? {
        return info2namespace[info]
    }

    fun getNamespace(instance: ModInstance): String? {
        return instance2namespace[instance]
    }

    fun getContainer(namespace: String): ModContainer? {
        return namespace2container[namespace]
    }

    fun getContainer(container: ModContainer): ModContainer {
        return container
    }

    fun getContainer(obj: ModObject<*>): ModContainer? {
        return object2container[obj]
    }

    fun getContainer(info: ModInfo): ModContainer? {
        return info2container[info]
    }

    fun getContainer(instance: ModInstance): ModContainer? {
        return instance2container[instance]
    }

    fun getObject(namespace: String): ModObject<*>? {
        return if (!namespace2object.containsKey(namespace)) {
            null
        } else namespace2object[namespace]
    }

    fun getObject(container: ModContainer): ModObject<*>? {
        return container2object[container]
    }

    fun getObject(obj: ModObject<*>): ModObject<*> {
        return obj
    }

    fun getObject(info: ModInfo): ModObject<*>? {
        return info2object[info]
    }

    fun getObject(instance: ModInstance): ModObject<*>? {
        return instance2object[instance]
    }

    fun getInfo(namespace: String): ModInfo? {
        return namespace2info[namespace]
    }

    fun getInfo(container: ModContainer): ModInfo? {
        return container2info[container]
    }

    fun getInfo(obj: ModObject<*>): ModInfo? {
        return object2info[obj]
    }

    fun getInfo(info: ModInfo): ModInfo {
        return info
    }

    fun getInfo(instance: ModInstance): ModInfo? {
        return instance2info[instance]
    }

    fun getInstance(namespace: String): ModInstance? {
        return namespace2instance[namespace]
    }

    fun getInstance(container: ModContainer): ModInstance? {
        return container2instance[container]
    }

    fun getInstance(obj: ModObject<*>): ModInstance? {
        return object2instance[obj]
    }

    fun getInstance(info: ModInfo): ModInstance? {
        return info2instance[info]
    }

    fun getInstance(instance: ModInstance): ModInstance {
        return instance
    }

    @Deprecated("", ReplaceWith("register(addonObject.namespace, addonObject.container)"))
    fun registerAddonObject(modObject: ModObject<*>) {
        register(modObject.modId, modObject.container)
    }

    @Deprecated("", ReplaceWith("register(addon.namespace, addon.`object`.container)"))
    fun registerAddon(modInstance: ModInstance) {
        register(modInstance.namespace, modInstance.`object`.container)
    }

    companion object {
        val instance = ModManager()
        @Deprecated("Replaced", ReplaceWith("instance.register(container.namespace, container)", "qtech.bubbles.addon.loader.ModManager.Companion.instance"))
        fun registerContainer(container: ModContainer) {
            instance.register(container.namespace, container)
        }
    }

}