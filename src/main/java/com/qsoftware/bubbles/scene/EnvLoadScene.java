package com.qsoftware.bubbles.scene;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.InfoTransporter;
import com.qsoftware.bubbles.common.crash.CrashReport;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.core.common.SavedGame;
import com.qsoftware.bubbles.core.utils.categories.GraphicsUtils;
import com.qsoftware.bubbles.environment.Environment;
import com.qsoftware.bubbles.settings.GameSettings;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Environment loading scene.
 * When showing this scene, a new thread will be created and in that thread the loading will be done.
 * The thread is located in the method {@link #loadEnv()}.
 *
 * @author Quinten Jungblut
 * @since 1.0.615
 */
public class EnvLoadScene extends Scene {
    private final boolean create;
    private final SavedGame savedGame;
    private final Supplier<AbstractGameType> gameType;
    private final InfoTransporter infoTransporter = new InfoTransporter(this::setDescription);
    private String description = "";

    public EnvLoadScene(SavedGame savedGame) {
        this(false, savedGame, null);
    }

    public EnvLoadScene(SavedGame savedGame, @NotNull Supplier<AbstractGameType> gameType) {
        this(true, savedGame, gameType);
    }

    private EnvLoadScene(boolean create, SavedGame savedGame, @Nullable Supplier<AbstractGameType> gameType) {
        this.create = create;
        this.savedGame = savedGame;
        this.gameType = gameType;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void showScene() {
        Thread thread = new Thread(this::loadEnv, "EnvironmentLoader");
        thread.start();
    }

    private void loadEnv() {
        AbstractGameType gameType;
        try {
            File directory = this.savedGame.getDirectory();
            if (this.create && directory.exists()) {
                FileUtils.deleteDirectory(directory);
            }

            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw new IOException("Creating save folder failed.");
                }
            }

            if (create) {
                this.setDescription("Creating save data...");
                gameType = Objects.requireNonNull(this.gameType).get();
                gameType.dumpDefaultState(this.savedGame, this.infoTransporter);
                gameType.createSaveData(this.savedGame, this.infoTransporter);

                this.setDescription("Loading data...");
            } else {
                this.setDescription("Loading data...");
                gameType = AbstractGameType.loadState(this.savedGame, this.infoTransporter);
            }

            Environment environment = this.game.environment = new Environment(gameType);

            if (create) {
                gameType.init(environment, this.infoTransporter);
                gameType.dumpSaveData(this.savedGame);
            } else {
                gameType.load(environment, this.infoTransporter);
            }
        } catch (Throwable t) {
            CrashReport crashReport = new CrashReport("Save being loaded", t);
            crashReport.add("Save Directory", this.savedGame.getDirectory());
            throw crashReport.getReportedException();
        }

        this.game.displayScene(new GameScene(this.savedGame, this.game.environment));
    }

    /**
     * Renders the environment loading scene.<br>
     * Shows the title in the blue accent color (#00b0ff), and the description in a 50% black color (#7f7f7f).
     *
     * @param game the QBubbles game.
     * @param gg   the graphics 2D processor.
     */
    @Override
    public void render(QBubbles game, Graphics2D gg) {
        gg.setColor(new Color(64, 64, 64));
        gg.fillRect(0, 0, QBubbles.getInstance().getWidth(), QBubbles.getInstance().getHeight());
        if (GameSettings.instance().isTextAntialiasEnabled())
            gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        gg.setColor(new Color(0, 192, 255));
        GraphicsUtils.drawCenteredString(gg, "Loading Environment...", new Rectangle2D.Double(0, ((double) QBubbles.getInstance().getHeight() / 2) - 24, QBubbles.getInstance().getWidth(), 64d), new Font("Helvetica", Font.PLAIN, 48));
        gg.setColor(new Color(127, 127, 127));
        GraphicsUtils.drawCenteredString(gg, this.description, new Rectangle2D.Double(0, ((double) QBubbles.getInstance().getHeight() / 2) + 40, QBubbles.getInstance().getWidth(), 50d), new Font("Helvetica", Font.PLAIN, 20));
        if (GameSettings.instance().isTextAntialiasEnabled())
            gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}
