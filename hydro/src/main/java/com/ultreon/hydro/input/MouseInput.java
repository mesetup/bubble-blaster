package com.ultreon.hydro.input;

import com.ultreon.hydro.core.input.MouseController;
import com.ultreon.hydro.vector.Vector2i;

import java.awt.*;

import static java.lang.System.out;

public final class MouseInput {
    private final Controller controller = new Controller();
    private static MouseInput instance;

    private MouseInput() {

    }

    public static void init() {
        if (instance == null) {
            instance = new MouseInput();
        }
    }
    
    public static boolean isPressed(Button button) {
        return instance.controller.isPressed(button.id);
    }

    public static int getX() {
        return getPos().x;
    }

    public static int getY() {
        return getPos().y;
    }

    public static Vector2i getPos() {
        Point currentPoint = instance.controller.getCurrentPoint();
        return currentPoint == null ? new Vector2i(-1, -1) : new Vector2i(currentPoint.x, currentPoint.y);
    }

    public static void listen(Component canvas) {
        out.printf("Listening[mouseInput]: %s\n", canvas);
        canvas.addMouseListener(instance.controller);
        canvas.addMouseMotionListener(instance.controller);
        canvas.addMouseWheelListener(instance.controller);
    }

    private static class Controller extends MouseController {
        @Override
        protected Point getCurrentLocationOnScreen() {
            return super.getCurrentLocationOnScreen();
        }

        @Override
        protected Point getCurrentPoint() {
            return super.getCurrentPoint();
        }

        @Override
        protected int getClickCount() {
            return super.getClickCount();
        }

        @Override
        protected boolean isPressed(int button) {
            return super.isPressed(button);
        }
    }

    enum Button {
        LEFT(1),
        RIGHT(2),
        MIDDLE(3),
        ;

        private final int id;

        Button(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
