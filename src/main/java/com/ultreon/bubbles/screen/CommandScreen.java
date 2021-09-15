package com.ultreon.bubbles.screen;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.LoadedGame;
import com.ultreon.bubbles.command.CommandConstructor;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.hydro.util.GraphicsUtils;
import com.ultreon.hydro.event._common.SubscribeEvent;
import com.ultreon.hydro.event.input.KeyboardEvent;
import com.ultreon.hydro.event.type.KeyEventType;
import com.ultreon.bubbles.util.Util;
import com.ultreon.bubbles.util.helpers.MathHelper;
import org.apache.tools.ant.types.Commandline;

import java.awt.*;
import com.ultreon.hydro.render.Renderer;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandScreen extends Screen {
    private final Font defaultFont = new Font(Util.getGame().getPixelFontName(), Font.PLAIN, 32);
    private String currentText = "/";
    private int cursorIndex = 1;

    public CommandScreen() {
        super();
    }

    @Override
    public void init() {
        BubbleBlaster.getEventBus().register(this);
    }

    @Override
    public boolean onClose(Screen to) {
        BubbleBlaster.getEventBus().unregister(this);

        currentText = "/";
        cursorIndex = 1;

        return super.onClose(to);
    }

    @Override
    public void render(Game game, Renderer gg) {
        this.render((BubbleBlaster) game, gg);
    }

    @SubscribeEvent
    public void onKeyboard(KeyboardEvent evt) {
        LoadedGame loadedGame = BubbleBlaster.getInstance().getLoadedGame();

        if (loadedGame == null) {
            return;
        }

        if (evt.getType() == KeyEventType.PRESS || evt.getType() == KeyEventType.HOLD) {
            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (currentText.length() == 0) return;

                String leftText = currentText.substring(0, cursorIndex - 1);
                String rightText = currentText.substring(cursorIndex);

                currentText = leftText + rightText;

                cursorIndex = MathHelper.clamp(cursorIndex - 1, 0, currentText.length());
                return;
            }

            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_LEFT) {
                cursorIndex = MathHelper.clamp(cursorIndex - 1, 0, currentText.length());
                return;
            }

            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_RIGHT) {
                cursorIndex = MathHelper.clamp(cursorIndex + 1, 0, currentText.length());
                return;
            }

            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_ENTER) {
                if (currentText.charAt(0) != '/') {
                    Objects.requireNonNull(loadedGame.getGameType().getPlayer()).sendMessage("Not a command, start with a ‘/’ for a command.");
                    BubbleBlaster.getInstance().showScreen(null);
                    return;
                }

                List<String> parsed = Arrays.asList(Commandline.translateCommandline(currentText.substring(1)));
                String[] args = parsed.subList(1, parsed.size()).toArray(new String[]{});

                if (!CommandConstructor.execute(parsed.get(0), loadedGame.getGameType().getPlayer(), args)) {
                    Objects.requireNonNull(loadedGame.getGameType().getPlayer()).sendMessage("Command ‘" + parsed.get(0) + "’ is non-existent.");
                    BubbleBlaster.getInstance().showScreen(null);
                    return;
                }

                BubbleBlaster.getInstance().showScreen(null);
                return;
            }

            if (evt.getType() == KeyEventType.PRESS && evt.getParentEvent().getKeyCode() == KeyEvent.VK_ESCAPE) {
                BubbleBlaster.getInstance().showScreen(null);
                return;
            }

            char c = evt.getParentEvent().getKeyChar();

            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_DEAD_ACUTE) {
                c = '\'';
            }

            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_QUOTEDBL) {
                c = '"';
            }

            System.out.println(evt.getParentEvent().paramString());
            System.out.println(evt.getParentEvent().paramString());
            System.out.println(evt.getParentEvent().getKeyChar());
            System.out.println(KeyEvent.getExtendedKeyCodeForChar('"'));
            System.out.println(KeyEvent.getKeyText(evt.getParentEvent().getKeyCode()));
            System.out.println(KeyEvent.getKeyText(evt.getParentEvent().getExtendedKeyCode()));

            if ((short) c >= 32) {
//                currentText += c;
                String leftText = currentText.substring(0, cursorIndex);
                String rightText = currentText.substring(cursorIndex);

                currentText = leftText + c + rightText;

                cursorIndex++;
            }
        }
    }

    public void render(BubbleBlaster game, Renderer gg) {
        if (!BubbleBlaster.getInstance().isGameLoaded()) {
            return;
        }

        gg.color(new Color(0, 0, 0, 64));
        gg.rect(0, 0, BubbleBlaster.getInstance().getWidth(), BubbleBlaster.getInstance().getHeight());

        gg.color(new Color(0, 0, 0, 128));
        gg.rect(0, BubbleBlaster.getInstance().getHeight() - 32, BubbleBlaster.getInstance().getWidth(), 32);

        gg.color(new Color(255, 255, 255, 255));
        GraphicsUtils.drawLeftAnchoredString(gg, currentText, new Point2D.Double(2, BubbleBlaster.getInstance().getHeight() - 28), 28, defaultFont);

        FontMetrics fontMetrics = gg.getFontMetrics(defaultFont);


        int cursorX;
        gg.color(new Color(0, 144, 192, 255));
        if (cursorIndex >= currentText.length()) {
            if (currentText.length() != 0) {
                cursorX = fontMetrics.stringWidth(currentText.substring(0, cursorIndex)) + 2;
            } else {
                cursorX = 0;
            }

            gg.line(cursorX, BubbleBlaster.getInstance().getHeight() - 30, cursorX, BubbleBlaster.getInstance().getHeight() - 2);
            gg.line(cursorX + 1, BubbleBlaster.getInstance().getHeight() - 30, cursorX + 1, BubbleBlaster.getInstance().getHeight() - 2);
        } else {
            if (currentText.length() != 0) {
                cursorX = fontMetrics.stringWidth(currentText.substring(0, cursorIndex));
            } else {
                cursorX = 0;
            }

            int width = fontMetrics.charWidth(currentText.charAt(cursorIndex));
            gg.line(cursorX, BubbleBlaster.getInstance().getHeight() - 2, cursorX + width, BubbleBlaster.getInstance().getHeight() - 2);
            gg.line(cursorX, BubbleBlaster.getInstance().getHeight() - 1, cursorX + width, BubbleBlaster.getInstance().getHeight() - 1);
        }
    }
}
