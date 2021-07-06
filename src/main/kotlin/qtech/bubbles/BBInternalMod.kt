package qtech.bubbles

import qtech.bubbles.bubble.AbstractBubble
import qtech.bubbles.common.ResourceLocation
import qtech.bubbles.common.mods.Modification
import qtech.bubbles.common.mods.ModObject
import qtech.bubbles.common.mods.ModInstance
import qtech.bubbles.environment.EnvironmentRenderer
import qtech.bubbles.event.Bus.getLocalAddonEventBus
import qtech.bubbles.event.Bus.qBubblesEventBus
import qtech.bubbles.event.SubscribeEvent
import qtech.bubbles.event.TextureRenderEvent
import qtech.bubbles.event.bus.LocalAddonEventBus
import qtech.hydro.GraphicsProcessor
import qtech.hydro.ITexture
import qtech.bubbles.init.*
import qtech.bubbles.init.TextureCollections.register
import qtech.bubbles.registry.Registers
import qtech.bubbles.screen.LoadScreen.Companion.get
import org.apache.logging.log4j.Logger
import qtech.bubbles.entity.BubbleEntity
import java.awt.RenderingHints

@Modification(namespace = "bubbleblaster")
class BBInternalMod(logger: Logger?, addonId: String?, modObject: ModObject<BBInternalMod>) : ModInstance(logger!!, addonId!!, modObject) {
    override val eventBus: LocalAddonEventBus<out ModInstance> = getLocalAddonEventBus(this)

    @SubscribeEvent
    fun onTextureRender(textureRenderEvent: TextureRenderEvent) {
        val textureCollection = textureRenderEvent.textureCollection
        if (textureCollection === TextureCollections.BUBBLE_TEXTURES.get()) {
            val bubbles: Collection<AbstractBubble?> = Registers.BUBBLES.values()
            val loadScreen = get() ?: throw IllegalStateException("Load scene is not available.")

            // Loop bubble types.
            for (bubble in bubbles) {
                // Log
                loadScreen.logInfo("Loading bubble textures: " + bubble!!.registryName)

                val MAX_GIANT_VALUE = (bubble.maxRadius * 4 + 80 + 4) + bubble.colors.size * BubbleEntity.RADIUS_MOD

                logger.debug("Bubble \u2018${bubble.registryName}\u2019 Max NORM  Radius: ${bubble.maxRadius}")
                logger.debug("Bubble \u2018${bubble.registryName}\u2019 Max GIANT Radius: ${bubble.maxRadius * 4}")
                logger.debug("Bubble \u2018${bubble.registryName}\u2019 Max GIANT Size  : ${MAX_GIANT_VALUE}")

                // Loop sizes 0 to max * 4.
                for (i in 0..(MAX_GIANT_VALUE + 1)) {

                    // Create resource location.
                    val resourceLocation = ResourceLocation(bubble.registryName!!.namespace, bubble.registryName!!.path + "/" + i)

                    // Create texture.
                    textureCollection[resourceLocation] = object : ITexture {
                        /**
                         * Renders the bubble.
                         *
                         * @param gg the graphics processor
                         */
                        override fun render(gg: GraphicsProcessor) {
                            gg.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
                            gg.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                            EnvironmentRenderer.drawBubble(gg, 0, 0, i, *bubble.colors)
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