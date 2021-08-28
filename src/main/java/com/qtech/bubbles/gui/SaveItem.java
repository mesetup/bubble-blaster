package com.qtech.bubbles.gui;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.core.common.SavedGame;
import com.qtech.bubbles.gui.border.Border;
import org.bson.BsonDocument;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("unused")
public class SaveItem extends JPanel {
    private final String name;
    private final long saveTime;
    private boolean selected;

    public SaveItem(SavedGame savedGame) {
        super();
        BsonDocument document = savedGame.debugInfoData();
        this.name = document.getString("name").getValue();
        this.saveTime = document.getInt64("saveTime").getValue();
        this.setSize(new Dimension(800, 300));
    }

    public void select() {
        this.selected = true;
    }

    public void unselect() {
        this.selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(800, 300);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 300);
    }

    @Override
    public void paintComponent(Graphics g) {
//        int margin = 10;
//        Dimension dim = getSize();
//        super.paintComponent(g);
//        g.setColor(Color.BLUE);
//        g.fillRect(margin, margin, dim.width - margin * 2, dim.height - margin * 2);

        Dimension dim = getSize();

        super.paintComponent(g);
        Rectangle r = getBounds();

        Graphics2D gg = (Graphics2D) g.create();

        if (selected) {
            Border border = new Border(4, 0, 4, 0);
            border.setPaint(new LinearGradientPaint(0, 0, dim.width, 0, new float[]{0f, 1f}, new Color[]{new Color(0, 128, 255), new Color(0, 255, 128)}));
            border.paintBorder(this, gg, 0, 0, r.width, r.height);

            gg.setColor(new Color(128, 128, 128));
        } else {
            Border border = new Border(1, 0, 1, 0);
            border.setPaint(new LinearGradientPaint(0, 0, dim.width, 0, new float[]{0f, 1f}, new Color[]{new Color(0, 128, 255), new Color(0, 255, 128)}));
            border.paintBorder(this, gg, 0, 0, dim.width, dim.height);

            gg.setColor(new Color(112, 112, 112));
        }
        gg.fillRect(0, 0, dim.width, dim.height);

        Font oldFont = gg.getFont();

        gg.setColor(new Color(192, 192, 192));

        gg.setFont(new Font(BubbleBlaster.getInstance().getSansFontName(), Font.BOLD, 36));
        gg.drawString(name, 20, 20 + gg.getFontMetrics().getHeight() / 2);

        gg.setFont(new Font(BubbleBlaster.getInstance().getSansFontName(), Font.BOLD, 16));
        ZonedDateTime date = LocalDateTime.ofEpochSecond(saveTime / 1000, (int) ((saveTime % 1000) * 1000000), ZoneOffset.UTC).atZone(ZoneId.of("UTC"));
        @SuppressWarnings("SpellCheckingInspection")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM uuuu, HH:mm:ss (z)");
        String formattedSavedTime = date.format(formatter);
        gg.drawString(formattedSavedTime, 20, 80 + gg.getFontMetrics().getHeight() / 2);

        gg.setFont(oldFont);

        gg.dispose();
    }
}
