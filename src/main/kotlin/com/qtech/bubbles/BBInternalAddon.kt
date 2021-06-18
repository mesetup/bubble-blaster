package com.qtech.bubbles

import com.qtech.bubbles.bubble.AbstractBubble
import com.qtech.bubbles.common.ResourceLocation
import com.qtech.bubbles.common.addon.Addon
import com.qtech.bubbles.common.addon.AddonObject
import com.qtech.bubbles.common.addon.AddonInstance
import com.qtech.bubbles.environment.EnvironmentRenderer
import com.qtech.bubbles.event.Bus.getLocalAddonEventBus
import com.qtech.bubbles.event.Bus.qBubblesEventBus
import com.qtech.bubbles.event.SubscribeEvent
import com.qtech.bubbles.event.TextureRenderEvent
import com.qtech.bubbles.event.bus.LocalAddonEventBus
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.graphics.ITexture
import com.qtech.bubbles.init.*
import com.qtech.bubbles.init.TextureCollections.register
import com.qtech.bubbles.registry.Registers
import com.qtech.bubbles.screen.LoadScreen.Companion.get
import org.apache.logging.log4j.Logger
import java.awt.RenderingHints

@Addon(namespace = "bubbleblaster")
class BBInternalAddon(logger: Logger?, addonId: String?, addonObject: AddonObject<BBInternalAddon>) : AddonInstance(logger!!, addonId!!, addonObject) {
    override val eventBus: LocalAddonEventBus<out AddonInstance>
    @SubscribeEvent
    fun onTextureRender(textureRenderEvent: TextureRenderEvent) {
        val textureCollection = textureRenderEvent.textureCollection
        if (textureCollection === TextureCollections.BUBBLE_TEXTURES.get()) {
            val bubbles: Collection<AbstractBubble?>? = Registers.BUBBLES.values()
            val loadScreen = get() ?: throw IllegalStateException("Load scene is not available.")
            for (bubble in bubbles!!) {
                loadScreen.logInfo("Loading bubble textures: " + bubble!!.registryName)
                for (i in 0..(bubble.maxRadius + 1) * 4) {
                    val resourceLocation = ResourceLocation(bubble.registryName!!.namespace, bubble.registryName!!.path + "/" + i)
                    textureCollection[resourceLocation] = object : ITexture {
                        override fun render(gg: GraphicsProcessor?) {
                            gg!!.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
                            gg.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                            EnvironmentRenderer.drawBubble(gg, 0, 0, i, *bubble.colors!!)
                        }

                        override fun width(): Int {
                            return i + bubble.colors!!.size * 2
                        }

                        override fun height(): Int {
                            return i + bubble.colors!!.size * 2
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val ADDON_ID = "bubbleblaster"
    }

    init {
        eventBus = getLocalAddonEventBus(this)
        qBubblesEventBus.register(this)
        Bubbles.BUBBLES.register(eventBus)
        AmmoTypes.AMMO_TYPES.register(eventBus)
        Entities.ENTITIES.register(eventBus)
        Effects.EFFECTS.register(eventBus)
        Abilities.ABILITY_TYPES.register(eventBus)
        GameEvents.GAME_EVENTS.register(eventBus)
        GameTypes.GAME_TYPES.register(eventBus)
        register(eventBus)
    }
}