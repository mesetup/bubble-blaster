package com.qtech.bubbles

import com.qtech.preloader.PreClassLoader
import lombok.SneakyThrows
import java.lang.reflect.InvocationTargetException
import javax.swing.SwingUtilities

/**
 * The actual main class.
 * Btw, not the main startup-class that's from the Manifest: [PreGameLoader]
 *
 * @see PreGameLoader
 */
object MainOld {
    var mainClassLoader: PreClassLoader? = null

    // Main (static) method. Game starts from here.
    @SneakyThrows
    fun main(args: Array<String?>?, launchClassLoader: PreClassLoader?) {
//        URL[] urls;
//
//        try {
//            urls = new URL[]{Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().toURL()};
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            System.exit(1);
//            return;
//        }
//
//        System.out.println(Arrays.toString(urls));
        SwingUtilities.invokeAndWait {
            mainClassLoader = launchClassLoader
            try {
                val gameClass = mainClassLoader!!.loadClass(BubbleBlaster::class.java.name)
                val launchMethod = gameClass.getMethod("main", Array<String>::class.java, PreClassLoader::class.java)
                launchMethod.invoke(null, args, mainClassLoader)
            } catch (exception: ClassNotFoundException) {
                System.err.println("Cannot load Game class: " + BubbleBlaster::class.java)
                exception.printStackTrace()
                System.exit(1)
            } catch (e: NoSuchMethodException) {
                System.err.println("Cannot find launch method.")
                System.err.println("Cannot load Game class: " + BubbleBlaster::class.java)
                e.printStackTrace()
                System.exit(1)
            } catch (e: IllegalAccessException) {
                System.err.println("Cannot invoke launch method.")
                System.err.println("Cannot load Game class: " + BubbleBlaster::class.java)
                e.printStackTrace()
                System.exit(1)
            } catch (e: InvocationTargetException) {
                System.err.println("Cannot invoke launch method.")
                System.err.println("Cannot load Game class: " + BubbleBlaster::class.java)
                e.cause!!.printStackTrace()
                System.exit(1)
            }
        }
    }
}