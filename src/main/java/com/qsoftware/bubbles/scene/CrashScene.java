package com.qsoftware.bubbles.scene;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.crash.CrashReport;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.core.utils.categories.GraphicsUtils;
import com.qsoftware.bubbles.gui.CrashButton;

import java.awt.*;
import java.io.IOException;

public class CrashScene extends Scene {
    private final CrashReport report;
    private final CrashButton crashButton;

    public CrashScene(CrashReport crashReport) {
        super();
        this.report = crashReport;

        this.crashButton = this.addWidget(new CrashButton(game.getWidth() / 2 - 64, 60, 128, 24));
        this.crashButton.setText("Open crash report \uD83D\uDCCE");
    }

    @Override
    public void showScene() {
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
    public boolean hideScene(Scene to) {
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
