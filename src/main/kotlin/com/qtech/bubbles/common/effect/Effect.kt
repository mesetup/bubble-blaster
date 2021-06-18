package com.qtech.bubbles.common.effect

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.AttributeMap
import com.qtech.bubbles.common.RegistryEntry
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.event.FilterEvent
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.util.helpers.SvgHelper
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*

abstract class Effect : RegistryEntry() {
    companion object {
        // Empty Image.
        protected var image: BufferedImage? = null

        init {
            image = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
            val gg = GraphicsProcessor(image!!.graphics)
            gg.background(Color(0, 0, 0, 0))
            gg.clear(0, 0, 32, 32)
        }
    }

    private val cache = HashMap<String, Any?>()
    val iconResource: URL?
        get() = javaClass.getResource("assets/" + registryName!!.namespace + "/vectors/effects/" + registryName!!.path + ".svg")

    @get:Synchronized
    val iconResourceAsStream: InputStream?
        get() {
            if (cache.containsKey("icon-stream")) {
                return cache["icon-stream"] as InputStream?
            }
            val inputStream = Effect::class.java.classLoader.getResourceAsStream("assets/" + registryName!!.namespace + "/vectors/effects/" + registryName!!.path + ".svg")
            if (inputStream == null && iconResource == null) {
                BubbleBlaster.logger.warn("Cannot find effect-icon: " + "/assets/" + registryName!!.namespace + "/vectors/effects/" + registryName!!.path + ".svg")
            }
            return cache.put("icon-stream", inputStream) as InputStream?
        }

    @Throws(IOException::class)
    fun getIcon(w: Int, h: Int, color: Color?): Image? {
//        if (cache.containsKey("icon-img")) {
//            return (Image) cache.get("icon-img");
//        }
//
//        Image img;
//        InputStream inputStream = getIconResourceAsStream();
//        if (inputStream != null) {
//            SvgHelper svgHelper = new SvgHelper(getIconResource());
//            img = svgHelper.getColoredImage(w, h, color);
//        } else {
//            Game.getLogger().warn("Cannot find effect-icon: " + getIconResource().toString());
//            img = image;
//        }
//
//        cache.put("icon-img", img);
//
//        return img;
        val inputStream = iconResourceAsStream
        if (inputStream != null && iconResource != null) {
            val svgHelper = SvgHelper(iconResource!!)
            return svgHelper.getColoredImage(w, h, color)
        }
        return image
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as Effect
        return registryName == that.registryName
    }

    override fun hashCode(): Int {
        return Objects.hash(registryName)
    }

    fun tick(entity: Entity, effectInstance: EffectInstance) {
        if (canExecute(entity, effectInstance)) {
            execute(entity, effectInstance)
        }
    }

    protected abstract fun canExecute(entity: Entity, effectInstance: EffectInstance): Boolean
    open fun execute(entity: Entity, effectInstance: EffectInstance) {}
    open fun onFilter(effectInstance: EffectInstance, evt: FilterEvent) {}
    open fun onStart(effectInstance: EffectInstance, entity: Entity) {}
    open fun onStop(entity: Entity?) {}
    open val attributeModifiers: AttributeMap?
        get() = AttributeMap()

    protected open fun updateStrength() {}
}