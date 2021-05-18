package com.qtech.bubbles.scene;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.common.crash.CrashReport;
import com.qtech.bubbles.common.screen.Screen;
import com.qtech.bubbles.core.utils.categories.GraphicsUtils;
import com.qtech.bubbles.gui.CrashButton;

import java.awt.*;
import java.io.IOException;

public class CrashScreen extends Screen {
    private final CrashReport report;
    private final CrashButton crashButton;

    public CrashScreen(CrashReport crashReport) {
        super();
        this.report = crashReport;

        this.crashButton = this.addWidget(new CrashButton(game.getWidth() / 2 - 64, 60, 128, 24));
        this.crashButton.setText("Open crash report \uD83D\uDCCE");
    }

    @Override
    public void init() {
        try {
            this.report.writeToFile();
        } catch (IOException e) {
            try {
                this.report.writeToFile();
            } catch (IOException ioException) {
                try {
                    this.report.writeToFile();
                } catch (IOException exception) {
                    QBubbles.getLogger().fatal("Couldn't create crash report!");
                }
            }
        }
    }

    @Override
    public boolean onClose(Screen to) {
        return false;
    }

    @Override
    public void render(QBubbles game, Graphics2D gg) {
        gg.setColor(new Color(192, 0, 0));
        gg.drawRect(0, 0, game.getWidth(), game.getHeight());

        GraphicsUtils.drawCenteredString(gg, "The game crashed!", new Rectangle(20, 20, game.getWidth() - 40, 30), new Font(game.getSansFontName(), Font.BOLD, 24));

        crashButton.setX(game.getWidth() / 2 - crashButton.getBounds().width / 2);
    }

    @Override
    public void renderGUI(QBubbles game, Graphics2D gg) {

    }
}
