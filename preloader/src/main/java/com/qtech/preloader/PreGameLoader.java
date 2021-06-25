package com.qtech.preloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

/**
 * Pre game loader, currently only for QBubbles.
 *
 * @since 1.0.0
 */
@SuppressWarnings({"deprecation"})
public class PreGameLoader {
    public static final Logger LOGGER = LogManager.getLogger("PreLoader");
    private static final String QBUBBLES_LOADER = "com.qtech.preloader.QBubblesLoader";

    public static void main(String[] args) {
        new PreGameLoader().loadGame(args);
    }

    public static PreClassLoader classLoader;

    private PreGameLoader() {
        URL[] urls = new URL[0];
        try {
            urls = new URL[]{PreGameLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().toURL()};
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }

        classLoader = new PreClassLoader(urls);
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    private void loadGame(String[] args) {
        try {
            QBubblesLoader loader;

            String loaderLocation = QBUBBLES_LOADER;
            LOGGER.info(String.format("Using loader from location: %s", loaderLocation));

            // Add the internal package and get the loader class from the loader location..
            classLoader.addInternalPackage(loaderLocation.substring(0, loaderLocation.lastIndexOf('.')));
            loader = (QBubblesLoader) Class.forName(loaderLocation, true, classLoader).newInstance();

            // Set arguments of loader.
            LOGGER.info(String.format("Current loader class %s", loader.getClass().getName()));

            final String loadTarget = Objects.requireNonNull(loader).getLoadingTarget();
            final Class<?> clazz = Class.forName(loadTarget, false, classLoader);
            final Method mainMethod = clazz.getMethod("main", String[].class, PreClassLoader.class);

            LOGGER.info(String.format("Loading Bubble Blaster {%s}", loadTarget));
            mainMethod.invoke(null, args, classLoader);
        } catch (Exception e) {
            LOGGER.fatal("Problem occurred when trying to load a game.", e);
            System.exit(1);
        }
    }
}
