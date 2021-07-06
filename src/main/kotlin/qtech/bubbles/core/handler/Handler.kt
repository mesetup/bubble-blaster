package qtech.bubbles.core.handler

import qtech.bubbles.entity.Entity
import qtech.bubbles.entity.player.PlayerEntity
import java.util.*

/**
 * @author Quinten Jungblut
 */
class Handler {
    val `object` = LinkedList<Entity>()

    /**
     * @author Quinten Jungblut
     */
    fun tick() {
//        for (int i = 0; i < object.size(); i++) {
//            GameObject tempObject = object.get(i);
//            tempObject.tick();
//        }
    }

    /**
     * @author Quinten Jungblut
     */
    fun render() {
//        for (int i = 0; i  < object.size(); i++) {
//            GameObject tempObject = object.get(i);
//
//            tempObject.render(g);
//        }
    }

    /**
     * @author Quinten Jungblut
     */
    fun addObject(`object`: Entity) {
        this.`object`.add(`object`)
    }

    fun removeObject(`object`: Entity) {
        this.`object`.remove(`object`)
    }

    fun clearEnemies() {
        for (i in `object`.indices) {
            val tempObject = `object`[i]
            if (tempObject is PlayerEntity) {
                `object`.clear()
                addObject(tempObject)
            }
        }
    }
}