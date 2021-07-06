package qtech.bubbles.common

interface IRegistryEntry {
    var registryName: ResourceLocation?

    fun updateRegistryName(namespace: String)
    val isTempRegistryName: Boolean
}