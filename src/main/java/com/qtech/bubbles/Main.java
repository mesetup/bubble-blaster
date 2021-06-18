package com.qtech.bubbles;

import com.qtech.preloader.PreClassLoader;
import lombok.SneakyThrows;

import javax.swing.*;

public class Main {

    @SneakyThrows
    public static void main(String[] args, PreClassLoader launchClassLoader) {

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
        SwingUtilities.invokeAndWait(() -> BubbleBlaster.main(args, launchClassLoader));
    }
}
