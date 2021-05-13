package com.qsoftware.bubbles.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.metal.MetalScrollBarUI;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

@SuppressWarnings({"unused", "RedundantSuppression"})
@Deprecated(since = "1.0 Alpha 1")
public class MyScrollbarUI extends MetalScrollBarUI {
    private Image imageThumb, imageTrack;

    MyScrollbarUI() {
        try {
//            imageThumb = ImageIO.read(new File(new ClassPathResource("data/employees.dat", this.getClass().getClassLoader())));
            imageTrack = ImageIO.read(new File("track.png"));
        } catch (IOException ignored) {

        }
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        g.translate(thumbBounds.x, thumbBounds.y);
        g.setColor(Color.red);
        g.drawRect(0, 0, thumbBounds.width - 2, thumbBounds.height - 1);

        AffineTransform transform = AffineTransform.getScaleInstance((double) thumbBounds.width / imageThumb.getWidth(null), (double) thumbBounds.height / imageThumb.getHeight(null));
        ((Graphics2D) g).drawImage(imageThumb, transform, null);

        g.translate(-thumbBounds.x, -thumbBounds.y);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.translate(trackBounds.x, trackBounds.y);
        ((Graphics2D) g).drawImage(imageTrack, AffineTransform.getScaleInstance(1, (double) trackBounds.height / imageTrack.getHeight(null)), null);
        g.translate(-trackBounds.x, -trackBounds.y);
    }
}
