package com.ultreon.bubbles.screen;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.commons.annotation.MethodsReturnNonnullByDefault;
import com.ultreon.commons.lang.InfoTransporter;
import com.ultreon.commons.crash.CrashLog;
import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.save.SavedGame;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.hydro.util.GraphicsUtils;
import com.ultreon.bubbles.environment.Environment;
import com.ultreon.bubbles.settings.GameSettings;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import com.ultreon.hydro.render.Renderer;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Deprecated
public class SaveLoadingScreen extends Screen {
    private final InfoTransporter infoTransporter = new InfoTransporter(this::setDescription);
    private final Supplier<AbstractGameType> gameType;
    private final SavedGame savedGame;
    private final boolean create;
    private String description = "";
    protected BubbleBlaster game = BubbleBlaster.getInstance();

    private void setDescription(String s) {
        this.description = s;
    }

    public SaveLoadingScreen(SavedGame savedGame) {
        this(false, savedGame, null);
    }

    public SaveLoadingScreen(SavedGame savedGame, @NotNull Supplier<AbstractGameType> gameType) {
        this(true, savedGame, gameType);
    }

    private SaveLoadingScreen(boolean create, SavedGame savedGame, @Nullable Supplier<AbstractGameType> gameType) {
        this.create = create;
        this.savedGame = savedGame;
        this.gameType = gameType;
    }

    @Override
    public void init() {
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
            CrashLog crashLog = new CrashLog("Save being loaded", t);
            crashLog.add("Save Directory", this.savedGame.getDirectory());
            throw crashLog.createCrash();
        }

        BubbleBlaster.getInstance().showScreen(null);
    }

    @Override
    public void render(Game game, Renderer gg) {
        gg.color(new Color(64, 64, 64));
        gg.rect(0, 0, BubbleBlaster.getInstance().getWidth(), BubbleBlaster.getInstance().getHeight());
        if (GameSettings.instance().isTextAntialiasEnabled())
            gg.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        gg.color(new Color(127, 127, 127));
        GraphicsUtils.drawCenteredString(gg, this.description, new Rectangle2D.Double(0, ((double) BubbleBlaster.getInstance().getHeight() / 2) + 40, BubbleBlaster.getInstance().getWidth(), 50d), new Font("Helvetica", Font.PLAIN, 20));
        if (GameSettings.instance().isTextAntialiasEnabled())
            gg.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    }

    @Override
    public void renderGUI(Game game, Renderer gg) {

    }
}
