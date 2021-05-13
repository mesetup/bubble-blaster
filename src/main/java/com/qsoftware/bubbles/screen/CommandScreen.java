package com.qsoftware.bubbles.screen;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.GraphicsProcessor;
import com.qsoftware.bubbles.common.command.CommandConstructor;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.common.screen.Screen;
import com.qsoftware.bubbles.core.utils.Utils;
import com.qsoftware.bubbles.core.utils.categories.GraphicsUtils;
import com.qsoftware.bubbles.event.KeyboardEvent;
import com.qsoftware.bubbles.event.PauseTickEvent;
import com.qsoftware.bubbles.event.SubscribeEvent;
import com.qsoftware.bubbles.event.bus.EventBus;
import com.qsoftware.bubbles.event.type.KeyEventType;
import com.qsoftware.bubbles.init.ScreenInit;
import com.qsoftware.bubbles.scene.GameScene;
import com.qsoftware.bubbles.util.Util;
import com.qsoftware.bubbles.util.helpers.MathHelper;
import org.apache.tools.ant.types.Commandline;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

public class CommandScreen extends Screen<CommandScreen> {
    private final Font defaultFont = new Font(Util.getGame().getPixelFontName(), Font.PLAIN, 32);
    private String currentText = "/";
    private int cursorIndex = 1;
    private EventBus.Handler binding;

    public CommandScreen(Scene scene) {
        super(ScreenInit.COMMAND_SCREEN.get(), scene);
    }

    @Override
    public void openScreen() {
        QBubbles.getEventBus().register(this);

        if (getScene() instanceof GameScene) {
            ((GameScene) getScene()).pause();
        }
    }

    @Override
    public void closeScreen() {
//        QBubbles.getEventBus().unregister(this);
        QBubbles.getEventBus().unregister(this);

        currentText = "/";
        cursorIndex = 1;

        if (getScene() instanceof GameScene) {
            ((GameScene) getScene()).unpause();
        }
    }

    @SubscribeEvent
    public void onKeyboard(KeyboardEvent evt) {
        if (!(getScene() instanceof GameScene)) return;

        if (evt.getType() == KeyEventType.PRESS || evt.getType() == KeyEventType.HOLD) {
            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (currentText.length() == 0) return;

                String leftText = currentText.substring(0, cursorIndex - 1);
                String rightText = currentText.substring(cursorIndex);

                currentText = leftText + rightText;

                cursorIndex = MathHelper.clamp(cursorIndex - 1, 0, currentText.length());
                return;
            }

            @Nullable GameScene gameScene = Utils.getGameScene();
            if (gameScene == null) return;

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
                    gameScene.getGameType().getPlayer().sendMessage("Not a command, start with a ‘/’ for a command.");
                    gameScene.closeCurrentScreen();
                    return;
                }

                List<String> parsed = Arrays.asList(Commandline.translateCommandline(currentText.substring(1)));
                String[] args = parsed.subList(1, parsed.size()).toArray(new String[]{});

                if (!CommandConstructor.execute(parsed.get(0), gameScene.getGameType().getPlayer(), args)) {
                    gameScene.getGameType().getPlayer().sendMessage("Command ‘" + parsed.get(0) + "’ is non-existent.");
                    gameScene.closeCurrentScreen();
                    return;
                }

                gameScene.closeCurrentScreen();
                return;
            }

            if (evt.getType() == KeyEventType.PRESS && evt.getParentEvent().getKeyCode() == KeyEvent.VK_ESCAPE) {
                gameScene.closeCurrentScreen();
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

    @Override
    @SubscribeEvent
    public void onPauseUpdate(PauseTickEvent evt) {

    }

    public void renderGUI(QBubbles game, GraphicsProcessor ngg) {
        if (!(getScene() instanceof GameScene)) return;

        ngg.setColor(new Color(0, 0, 0, 64));
        ngg.fillRect(0, 0, QBubbles.getInstance().getWidth(), QBubbles.getInstance().getHeight());

        ngg.setColor(new Color(0, 0, 0, 128));
        ngg.fillRect(0, QBubbles.getInstance().getHeight() - 32, QBubbles.getInstance().getWidth(), 32);

        ngg.setColor(new Color(255, 255, 255, 255));
        GraphicsUtils.drawLeftAnchoredString(ngg, currentText, new Point2D.Double(2, QBubbles.getInstance().getHeight() - 28), 28, defaultFont);

        FontMetrics fontMetrics = ngg.getFontMetrics(defaultFont);


        int cursorX;
        ngg.setColor(new Color(0, 144, 192, 255));
        if (cursorIndex >= currentText.length()) {
            if (currentText.length() != 0) {
                cursorX = fontMetrics.stringWidth(currentText.substring(0, cursorIndex)) + 2;
            } else {
                cursorX = 0;
            }

            ngg.drawLine(cursorX, QBubbles.getInstance().getHeight() - 30, cursorX, QBubbles.getInstance().getHeight() - 2);
            ngg.drawLine(cursorX + 1, QBubbles.getInstance().getHeight() - 30, cursorX + 1, QBubbles.getInstance().getHeight() - 2);
        } else {
            if (currentText.length() != 0) {
                cursorX = fontMetrics.stringWidth(currentText.substring(0, cursorIndex));
            } else {
                cursorX = 0;
            }

            int width = fontMetrics.charWidth(currentText.charAt(cursorIndex));
            ngg.drawLine(cursorX, QBubbles.getInstance().getHeight() - 2, cursorX + width, QBubbles.getInstance().getHeight() - 2);
            ngg.drawLine(cursorX, QBubbles.getInstance().getHeight() - 1, cursorX + width, QBubbles.getInstance().getHeight() - 1);
        }
    }
}
