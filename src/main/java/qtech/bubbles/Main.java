package qtech.bubbles;

import com.qtech.preloader.PreClassLoader;
import lombok.SneakyThrows;

import javax.swing.*;

public class Main {
    private static PreClassLoader preClassLoader;

    public static PreClassLoader getPreClassLoader() {
        return preClassLoader;
    }

    @SneakyThrows
    public static void main(String[] args, PreClassLoader preClassLoader) {
        Main.preClassLoader = preClassLoader;
        SwingUtilities.invokeAndWait(() -> BubbleBlaster.main(args, preClassLoader));
    }
}
