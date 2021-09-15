package com.ultreon.bubbles.screen;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.LoadedGame;
import com.ultreon.commons.lang.InfoTransporter;
import com.ultreon.commons.crash.CrashReport;
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

import java.awt.*;
import com.ultreon.hydro.render.Renderer;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Environment loading scene.
 * When showing this scene, a new thread will be created and in that thread the loading will be done.
 * The thread is located in the method {@link #init()}.
 *
 * Todo: update docstring.
 *
 * @author Qboi
 * @since 1.0.615
 */
public class EnvLoadScreen extends Screen {
    private final boolean create;
    private final SavedGame savedGame;
    private final Supplier<AbstractGameType> gameType;
    private final AtomicReference<LoadedGame> loadedGameReference;
    private final InfoTransporter infoTransporter = new InfoTransporter(this::setDescription);
    private String description = "";
    protected BubbleBlaster game = BubbleBlaster.getInstance();

    public EnvLoadScreen(SavedGame savedGame, AtomicReference<LoadedGame> loadedGameReference) {
        this(false, savedGame, null, loadedGameReference);
    }

    public EnvLoadScreen(SavedGame savedGame, @NotNull Supplier<AbstractGameType> gameType, AtomicReference<LoadedGame> loadedGameReference) {
        this(true, savedGame, gameType, loadedGameReference);
    }

    private EnvLoadScreen(boolean create, SavedGame savedGame, @Nullable Supplier<AbstractGameType> gameType, AtomicReference<LoadedGame> loadedGameReference) {
        this.create = create;
        this.savedGame = savedGame;
        this.gameType = gameType;
        this.loadedGameReference = loadedGameReference;
    }

    private void setDescription(String description) {
        this.description = description;
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

            LoadedGame loadedGame = new LoadedGame(savedGame, game.environment);
            loadedGame.run();

            loadedGameReference.set(loadedGame);
        } catch (Throwable t) {
            CrashReport crashReport = new CrashReport("Save being loaded", t);
            crashReport.add("Save Directory", this.savedGame.getDirectory());
            throw crashReport.getReportedException();
        }

        BubbleBlaster.getInstance().showScreen(null);
    }

    /**
     * Renders the environment loading scene.<br>
     * Shows the title in the blue accent color (#00b0ff), and the description in a 50% black color (#7f7f7f).
     *
     * @param game the QBubbles game.
     * @param gg   the graphics 2D processor.
     */
    @Override
    public void render(Game game, Renderer gg) {
        gg.color(new Color(64, 64, 64));
        gg.rect(0, 0, BubbleBlaster.getInstance().getWidth(), BubbleBlaster.getInstance().getHeight());
        if (GameSettings.instance().isTextAntialiasEnabled())
            gg.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        gg.color(new Color(0, 192, 255));
        GraphicsUtils.drawCenteredString(gg, "Loading Environment...", new Rectangle2D.Double(0, ((double) BubbleBlaster.getInstance().getHeight() / 2) - 24, BubbleBlaster.getInstance().getWidth(), 64d), new Font("Helvetica", Font.PLAIN, 48));
        gg.color(new Color(127, 127, 127));
        GraphicsUtils.drawCenteredString(gg, this.description, new Rectangle2D.Double(0, ((double) BubbleBlaster.getInstance().getHeight() / 2) + 40, BubbleBlaster.getInstance().getWidth(), 50d), new Font("Helvetica", Font.PLAIN, 20));
        if (GameSettings.instance().isTextAntialiasEnabled())
            gg.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}
