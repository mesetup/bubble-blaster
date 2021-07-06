package qtech.hydro

import qtech.bubbles.common.RegistryEntry
import qtech.bubbles.common.ResourceLocation
import org.apache.logging.log4j.LogManager
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO

class TextureCollection : RegistryEntry() {
    private val textures = HashMap<ResourceLocation, Image>()
    operator fun set(location: ResourceLocation, texture: ITexture) {
        if (textures.containsKey(location)) {
            LOGGER.warn("Texture override: $location")
        }
        val bufferedImage = BufferedImage(texture.width(), texture.height(), BufferedImage.TYPE_INT_ARGB)
        val graphics = GraphicsProcessor(bufferedImage.graphics)
        texture.render(graphics)
        graphics.dispose()
        textures[location] = bufferedImage
    }

    operator fun get(location: ResourceLocation): Image? {
        require(textures.containsKey(location)) { "No texture with resource location: $location" }
        return textures[location]
    }

    fun dump(path: Path) {
        for ((key, value) in textures) {
            val location = path.resolve(key.toString().replace(":", "-") + ".png")
            val parent = location.parent
            LOGGER.info("Dumping: $registryName :: $key")
            try {
                Files.createDirectories(parent)
                ImageIO.write(toBufferedImage(value), "png", location.toFile())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private val LOGGER = LogManager.getLogger("QB:TextureCollection")

        /**
         * Converts a given Image into a BufferedImage
         *
         * @param img The Image to be converted
         * @return The converted BufferedImage
         */
        private fun toBufferedImage(img: Image): BufferedImage {
            if (img is BufferedImage) {
                return img
            }

            // Create a buffered image with transparency
            val image = BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB)

            // Draw the image on to the buffered image
            val gp = GraphicsProcessor(image.createGraphics())
            gp.img(img, 0, 0)
            gp.dispose()

            // Return the buffered image
            return image
        }
    }
}