package com.ultreon.bubbles.screen;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.commons.crash.CrashLog;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.bubbles.render.gui.CrashButton;

import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.util.GraphicsUtils;

import java.awt.*;
import java.io.File;

public class CrashScreen extends Screen {
    private final CrashLog report;
    private final CrashButton crashButton;
    private final String reportName;

    public CrashScreen(CrashLog crashLog) {
        super();
        this.report = crashLog;
        this.reportName = crashLog.getDefaultFileName();

        BubbleBlaster bb = BubbleBlaster.getInstance();
        this.crashButton = this.add(new CrashButton(bb.getScaledWidth() / 2 - 64, 60, 128, 24));
        this.crashButton.setText("Open crash report \uD83D\uDCCE");
    }

    @Override
    public void init() {
        this.report.writeToFile(new File(reportName));
    }

    @Override
    public boolean onClose(Screen to) {
        return false;
    }

    @Override
    public void render(Game game, Renderer gg) {
        if (game instanceof BubbleBlaster bb) {
            gg.color(new Color(192, 0, 0));
            gg.rectLine(0, 0, game.getWidth(), bb.getScaledHeight());

            GraphicsUtils.drawCenteredString(gg, "The game crashed!", new Rectangle(20, 20, bb.getScaledWidth() - 40, 30), new Font(bb.getSansFontName(), Font.BOLD, 24));

            crashButton.setX(bb.getScaledWidth() / 2 - crashButton.getBounds().width / 2);
        }
    }

    @Override
    public void renderGUI(Game game, Renderer gg) {

    }
}
