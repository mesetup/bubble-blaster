package com.qsoftware.bubbles.scene;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.annotation.MethodsReturnNonnullByDefault;
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

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SaveLoadingScene extends Scene {
    private final InfoTransporter infoTransporter = new InfoTransporter(this::setDescription);
    private final Supplier<AbstractGameType> gameType;
    private final SavedGame savedGame;
    private final boolean create;
    private String description = "";

    private void setDescription(String s) {
        this.description = s;
    }

    public SaveLoadingScene(SavedGame savedGame) {
        this(false, savedGame, null);
    }

    public SaveLoadingScene(SavedGame savedGame, @NotNull Supplier<AbstractGameType> gameType) {
        this(true, savedGame, gameType);
    }

    private SaveLoadingScene(boolean create, SavedGame savedGame, @Nullable Supplier<AbstractGameType> gameType) {
        this.create = create;
        this.savedGame = savedGame;
        this.gameType = gameType;
    }

    @Override
    public void showScene() {
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

    @Override
    public void render(QBubbles game, Graphics2D gg) {
        gg.setColor(new Color(64, 64, 64));
        gg.fillRect(0, 0, QBubbles.getInstance().getWidth(), QBubbles.getInstance().getHeight());
        if (GameSettings.instance().isTextAntialiasEnabled())
            gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        gg.setColor(new Color(127, 127, 127));
        GraphicsUtils.drawCenteredString(gg, this.description, new Rectangle2D.Double(0, ((double) QBubbles.getInstance().getHeight() / 2) + 40, QBubbles.getInstance().getWidth(), 50d), new Font("Helvetica", Font.PLAIN, 20));
        if (GameSettings.instance().isTextAntialiasEnabled())
            gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    }

    @Override
    public void renderGUI(QBubbles game, Graphics2D gg) {

    }
}
