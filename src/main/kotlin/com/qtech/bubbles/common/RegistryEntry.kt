package com.qtech.bubbles.common

import java.util.*

abstract class RegistryEntry : IRegistryEntry {
    final override var registryName: ResourceLocation? = null

    fun setRegistryName(namespace: String?, name: String) {
        registryName = ResourceLocation(namespace, name)
    }

    override val isTempRegistryName: Boolean
        get() = registryName?.namespace == null

    override fun updateRegistryName(namespace: String) {
        if (registryName?.namespace == null) {
            registryName = registryName!!.withNamespace(namespace)
        }
    }

    fun setRegistryName(name: String) {
        registryName = ResourceLocation(null, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as RegistryEntry
        return registryName == that.registryName
    }

    override fun hashCode(): Int {
        return Objects.hash(registryName)
    }
}