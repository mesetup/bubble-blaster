package com.ultreon.hydro.screen.gui;

import com.ultreon.hydro.render.Renderer;
import org.apache.commons.configuration2.io.ConfigurationLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public abstract class Container extends Widget {
    protected final List<Widget> children = new ArrayList<>();
    protected Widget hoveredWidget;
    private static final Logger logger = LogManager.getLogger("Widget-Containment");

    public Container(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render(Renderer renderer) {
        Renderer containment = renderer.create(this.x, this.y, this.width, this.height);
        renderChildren(containment);
    }

    protected void renderChildren(Renderer renderer) {
        for (Widget child : this.children) {
            child.render(renderer);
        }
    }

    public <T extends Widget> T add(T child) {
        this.children.add(child);
        return child;
    }

    public void remove(Widget child) {
        this.children.remove(child);
    }

    @Nullable
    public Widget getWidgetAt(int x, int y) {
//        logger.info("Container[c7f17d76]: CHILDREN" + this.children);
        for (Widget child : this.children) {
//            logger.info("Container[b610e134]: X(" + x + ") : Y(" + y + ") : CONTAINS(" + child.getX() + "," + child.getY() + "," + child.isWithinBounds(x, y) + ")");
            if (child.isWithinBounds(x, y)) return child;
        }
        return null;
    }

    @Override
    public void onMouseClick(int x, int y, int button, int count) {
        logger.info("Container[aeb7abd0]: MOUSE CLICK EVENT RECEIVED (" + x + ", " + y + ", " + button + ", " + count + ")");
        Widget widget = getWidgetAt(x, y);
        if (widget != null) widget.onMouseClick(x, y, button, count);
    }

    @Override
    public void onMousePress(int x, int y, int button) {
        Widget widget = getWidgetAt(x, y);
        if (widget != null) widget.onMousePress(x, y, button);
    }

    @Override
    public void onMouseRelease(int x, int y, int button) {
        Widget widget = getWidgetAt(x, y);
        if (widget != null) widget.onMouseRelease(x, y, button);
    }

    @Override
    public void onMouseMove(int x, int y) {
        boolean widgetChanged = false;
        if (this.hoveredWidget != null && !this.hoveredWidget.isWithinBounds(x, y)) {
            this.hoveredWidget.onMouseLeave();
        }

        Widget widgetAt = this.getWidgetAt(x, y);
        if (widgetAt != this.hoveredWidget) widgetChanged = true;
        this.hoveredWidget = widgetAt;

        if (this.hoveredWidget != null) {
            this.hoveredWidget.onMouseMove(x, y);

            if (widgetChanged) {
                this.hoveredWidget.onMouseEnter(x, y);
            }
        }
    }

    @Override
    public void onMouseDrag(int x, int y, int button) {
        Widget widget = getWidgetAt(x, y);
        if (widget != null) widget.onMouseDrag(x, y, button);
    }

    @Override
    public void onMouseLeave() {
        if (this.hoveredWidget != null) {
            this.hoveredWidget.onMouseLeave();
            this.hoveredWidget = null;
        }
    }

    @Override
    public void onMouseWheel(int x, int y, double rotation, int amount, int units) {
        Widget widget = getWidgetAt(x, y);
        if (widget != null) widget.onMouseWheel(x, y, rotation, amount, units);
    }

    public Widget getHoveredWidget() {
        return hoveredWidget;
    }
}
