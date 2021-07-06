@file:Suppress("DEPRECATION")

package qtech.bubbles.common.screen

import qtech.bubbles.common.RegistryEntry
import qtech.bubbles.common.scene.Scene
import java.util.*
import java.util.function.Function

@Deprecated("")
class ScreenType<T : Screen?>     // EffectInstance class field.
    (  // Fields
    private val screenFactory: Function<Scene, T>
) : RegistryEntry() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ScreenType<*>
        return registryName == that.registryName
    }

    override fun hashCode(): Int {
        return Objects.hash(registryName)
    }

    fun getScreen(scene: Scene): T {
        return screenFactory.apply(scene)
    }
}