package com.qtech.bubbleblaster.screen;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.LoadedGame;
import com.qtech.bubbleblaster.common.command.CommandConstructor;
import com.qtech.bubbleblaster.common.screen.Screen;
import com.qtech.bubbleblaster.core.utils.categories.GraphicsUtils;
import com.qtech.bubbleblaster.event.KeyboardEvent;
import com.qtech.bubbleblaster.event.SubscribeEvent;
import com.qtech.bubbleblaster.event.type.KeyEventType;
import com.qtech.bubbleblaster.util.Util;
import com.qtech.bubbleblaster.util.helpers.MathHelper;
import org.apache.tools.ant.types.Commandline;

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
                    BubbleBlaster.getInstance().displayScene(null);
                    return;
                }

                List<String> parsed = Arrays.asList(Commandline.translateCommandline(currentText.substring(1)));
                String[] args = parsed.subList(1, parsed.size()).toArray(new String[]{});

                if (!CommandConstructor.execute(parsed.get(0), loadedGame.getGameType().getPlayer(), args)) {
                    Objects.requireNonNull(loadedGame.getGameType().getPlayer()).sendMessage("Command ‘" + parsed.get(0) + "’ is non-existent.");
                    BubbleBlaster.getInstance().displayScene(null);
                    return;
                }

                BubbleBlaster.getInstance().displayScene(null);
                return;
            }

            if (evt.getType() == KeyEventType.PRESS && evt.getParentEvent().getKeyCode() == KeyEvent.VK_ESCAPE) {
                BubbleBlaster.getInstance().displayScene(null);
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

    public void render(BubbleBlaster game, GraphicsProcessor gg) {
        if (!BubbleBlaster.getInstance().isGameLoaded()) {
            return;
        }

        gg.setColor(new Color(0, 0, 0, 64));
        gg.fillRect(0, 0, BubbleBlaster.getInstance().getWidth(), BubbleBlaster.getInstance().getHeight());

        gg.setColor(new Color(0, 0, 0, 128));
        gg.fillRect(0, BubbleBlaster.getInstance().getHeight() - 32, BubbleBlaster.getInstance().getWidth(), 32);

        gg.setColor(new Color(255, 255, 255, 255));
        GraphicsUtils.drawLeftAnchoredString(gg, currentText, new Point2D.Double(2, BubbleBlaster.getInstance().getHeight() - 28), 28, defaultFont);

        FontMetrics fontMetrics = gg.getFontMetrics(defaultFont);


        int cursorX;
        gg.setColor(new Color(0, 144, 192, 255));
        if (cursorIndex >= currentText.length()) {
            if (currentText.length() != 0) {
                cursorX = fontMetrics.stringWidth(currentText.substring(0, cursorIndex)) + 2;
            } else {
                cursorX = 0;
            }

            gg.drawLine(cursorX, BubbleBlaster.getInstance().getHeight() - 30, cursorX, BubbleBlaster.getInstance().getHeight() - 2);
            gg.drawLine(cursorX + 1, BubbleBlaster.getInstance().getHeight() - 30, cursorX + 1, BubbleBlaster.getInstance().getHeight() - 2);
        } else {
            if (currentText.length() != 0) {
                cursorX = fontMetrics.stringWidth(currentText.substring(0, cursorIndex));
            } else {
                cursorX = 0;
            }

            int width = fontMetrics.charWidth(currentText.charAt(cursorIndex));
            gg.drawLine(cursorX, BubbleBlaster.getInstance().getHeight() - 2, cursorX + width, BubbleBlaster.getInstance().getHeight() - 2);
            gg.drawLine(cursorX, BubbleBlaster.getInstance().getHeight() - 1, cursorX + width, BubbleBlaster.getInstance().getHeight() - 1);
        }
    }
}
