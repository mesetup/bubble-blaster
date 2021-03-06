package com.ultreon.test.bubbles;

import java.awt.*;
import com.ultreon.hydro.render.Renderer;
import java.awt.image.ImageProducer;

public class ImageCanvas extends Canvas {
    Image image;

    public ImageCanvas(String name) {
        MediaTracker media = new MediaTracker(this);
        image = Toolkit.getDefaultToolkit().getImage(name);
        media.addImage(image, 0);
        try {
            media.waitForID(0);
        } catch (Exception e) {
        }
    }

    public ImageCanvas(ImageProducer imageProducer) {
        image = createImage(imageProducer);
    }

    public void paint(Renderer g) {
        g.image(image, 0, 0);
    }

    public static void main(String[] argv) {
        if (argv.length < 1) {
            System.out.println
                    ("usage: ImageCanvas.class [image file name]");
            System.exit(0);
        }
        Frame frame = new Frame(argv[0]);
        frame.setLayout(new BorderLayout());
        frame.add("Center", new ImageCanvas(argv[0]));
        frame.resize(400, 400);
        frame.show();
    }
}