package com.qtech.bubbles;

import com.qtech.preloader.PreClassLoader;
import com.qtech.preloader.PreGameLoader;
import lombok.SneakyThrows;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The actual main class.
 * Btw, not the main startup-class that's from the Manifest: {@linkplain PreGameLoader}
 *
 * @see PreGameLoader
 */
public class Main {
    public static PreClassLoader mainClassLoader;

    // Main (static) method. Game starts from here.
    @SneakyThrows
    @SuppressWarnings("unused")
    public static void main(String[] args, PreClassLoader launchClassLoader) {
        SwingUtilities.invokeAndWait(() -> {
            mainClassLoader = launchClassLoader;

            try {
                Class<?> gameClass = mainClassLoader.loadClass(BubbleBlaster.class.getName());
                Method launchMethod = gameClass.getMethod("main", String[].class, PreClassLoader.class);
                launchMethod.invoke(null, args, mainClassLoader);

            } catch (ClassNotFoundException exception) {
                System.err.println("Cannot load Game class: " + BubbleBlaster.class);
                exception.printStackTrace();
                System.exit(1);
            } catch (NoSuchMethodException e) {
                System.err.println("Cannot find launch method.");
                System.err.println("Cannot load Game class: " + BubbleBlaster.class);
                e.printStackTrace();
                System.exit(1);
            } catch (IllegalAccessException e) {
                System.err.println("Cannot invoke launch method.");
                System.err.println("Cannot load Game class: " + BubbleBlaster.class);
                e.printStackTrace();
                System.exit(1);
            } catch (InvocationTargetException e) {
                System.err.println("Cannot invoke launch method.");
                System.err.println("Cannot load Game class: " + BubbleBlaster.class);
                e.getCause().printStackTrace();
                System.exit(1);
            }
        });
    }
}
