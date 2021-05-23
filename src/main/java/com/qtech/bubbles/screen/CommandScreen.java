package com.qtech.bubbles.screen;

import com.qtech.bubbles.LoadedGame;
import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.common.command.CommandConstructor;
import com.qtech.bubbles.common.screen.Screen;
import com.qtech.bubbles.core.utils.categories.GraphicsUtils;
import com.qtech.bubbles.event.KeyboardEvent;
import com.qtech.bubbles.event.SubscribeEvent;
import com.qtech.bubbles.event.type.KeyEventType;
import com.qtech.bubbles.util.Util;
import com.qtech.bubbles.util.helpers.MathHelper;
import org.apache.tools.ant.types.Commandline;

import java.awt.*;
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
        QBubbles.getEventBus().register(this);
    }

    @Override
    public boolean onClose(Screen to) {
        QBubbles.getEventBus().unregister(this);

        currentText = "/";
        cursorIndex = 1;

        return super.onClose(to);
    }

    @SubscribeEvent
    public void onKeyboard(KeyboardEvent evt) {
        LoadedGame loadedGame = QBubbles.getInstance().getLoadedGame();

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
                    QBubbles.getInstance().displayScene(null);
                    return;
                }

                List<String> parsed = Arrays.asList(Commandline.translateCommandline(currentText.substring(1)));
                String[] args = parsed.subList(1, parsed.size()).toArray(new String[]{});

                if (!CommandConstructor.execute(parsed.get(0), loadedGame.getGameType().getPlayer(), args)) {
                    Objects.requireNonNull(loadedGame.getGameType().getPlayer()).sendMessage("Command ‘" + parsed.get(0) + "’ is non-existent.");
                    QBubbles.getInstance().displayScene(null);
                    return;
                }

                QBubbles.getInstance().displayScene(null);
                return;
            }

            if (evt.getType() == KeyEventType.PRESS && evt.getParentEvent().getKeyCode() == KeyEvent.VK_ESCAPE) {
                QBubbles.getInstance().displayScene(null);
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

    public void render(QBubbles game, Graphics2D gg) {
        if (!QBubbles.getInstance().isGameLoaded()) {
            return;
        }

        gg.setColor(new Color(0, 0, 0, 64));
        gg.fillRect(0, 0, QBubbles.getInstance().getWidth(), QBubbles.getInstance().getHeight());

        gg.setColor(new Color(0, 0, 0, 128));
        gg.fillRect(0, QBubbles.getInstance().getHeight() - 32, QBubbles.getInstance().getWidth(), 32);

        gg.setColor(new Color(255, 255, 255, 255));
        GraphicsUtils.drawLeftAnchoredString(gg, currentText, new Point2D.Double(2, QBubbles.getInstance().getHeight() - 28), 28, defaultFont);

        FontMetrics fontMetrics = gg.getFontMetrics(defaultFont);


        int cursorX;
        gg.setColor(new Color(0, 144, 192, 255));
        if (cursorIndex >= currentText.length()) {
            if (currentText.length() != 0) {
                cursorX = fontMetrics.stringWidth(currentText.substring(0, cursorIndex)) + 2;
            } else {
                cursorX = 0;
            }

            gg.drawLine(cursorX, QBubbles.getInstance().getHeight() - 30, cursorX, QBubbles.getInstance().getHeight() - 2);
            gg.drawLine(cursorX + 1, QBubbles.getInstance().getHeight() - 30, cursorX + 1, QBubbles.getInstance().getHeight() - 2);
        } else {
            if (currentText.length() != 0) {
                cursorX = fontMetrics.stringWidth(currentText.substring(0, cursorIndex));
            } else {
                cursorX = 0;
            }

            int width = fontMetrics.charWidth(currentText.charAt(cursorIndex));
            gg.drawLine(cursorX, QBubbles.getInstance().getHeight() - 2, cursorX + width, QBubbles.getInstance().getHeight() - 2);
            gg.drawLine(cursorX, QBubbles.getInstance().getHeight() - 1, cursorX + width, QBubbles.getInstance().getHeight() - 1);
        }
    }
}
