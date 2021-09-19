package com.ultreon.bubbles.render.gui.view;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.hydro.event.TickEvent;
import com.ultreon.hydro.event.SubscribeEvent;
import com.ultreon.hydro.event.bus.EventBus;
import com.ultreon.hydro.screen.gui.Rectangle;
import com.ultreon.hydro.screen.gui.Widget;
import com.ultreon.hydro.screen.gui.view.View;
import com.ultreon.hydro.screen.Screen;
import org.jetbrains.annotations.NotNull;

import com.ultreon.hydro.render.Renderer;

import java.util.Set;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class ScrollView extends View {
    @SuppressWarnings("FieldCanBeLocal")
    private final Screen screen;
    private Rectangle outerBounds;
    private Rectangle innerBounds;

    private int tickEventCode;
    private int renderEventCode;
    private Set<Widget> children;
    private boolean eventsActive;
    private EventBus.Handler binding;

    public ScrollView(Screen screen, Rectangle innerBounds, Rectangle outerBounds) {
        super(outerBounds.x, outerBounds.y, outerBounds.width, outerBounds.height);
        this.innerBounds = innerBounds;
        this.outerBounds = outerBounds;
        this.screen = screen;
        this.make();
    }

    public void setOuterBounds(Rectangle outerBounds) {
        this.outerBounds = outerBounds;
    }

    public void setInnerBounds(Rectangle innerBounds) {
        this.innerBounds = innerBounds;
    }

    @SuppressWarnings("EmptyMethod")
    @SubscribeEvent
    private void tick(TickEvent event) {

    }

    @Override
    public void render(@NotNull Renderer renderer) {
        this.containerGraphics = renderer.create(outerBounds.getX(), outerBounds.getY(), outerBounds.getWidth(), outerBounds.getHeight());
        for (Widget child : this.children) {
            child.render(this.containerGraphics);
        }
    }

    public void add(Widget widget) {
        this.children.add(widget);
    }

    @Override
    public void make() {
        BubbleBlaster.getEventBus().register(this);
        this.eventsActive = true;
    }

    @Override
    public void destroy() {
        BubbleBlaster.getEventBus().unregister(this);
        this.eventsActive = false;
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
