package com.ultreon.bubbles.render.gui.view;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.hydro.event.TickEvent;
import com.ultreon.hydro.event._common.SubscribeEvent;
import com.ultreon.hydro.event.bus.EventBus;
import com.ultreon.hydro.gui.Widget;
import com.ultreon.hydro.gui.view.View;
import com.ultreon.hydro.screen.Screen;
import org.jetbrains.annotations.NotNull;

import com.ultreon.hydro.render.Renderer;
import java.awt.geom.Rectangle2D;
import java.util.Set;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class ScrollView extends View {
    @SuppressWarnings("FieldCanBeLocal")
    private final Screen screen;
    private Rectangle2D outerBounds;
    private Rectangle2D innerBounds;

    private int tickEventCode;
    private int renderEventCode;
    private Set<Widget> children;
    private boolean eventsActive;
    private EventBus.Handler binding;

    public ScrollView(Screen screen, Rectangle2D innerBounds, Rectangle2D outerBounds) {
        this.innerBounds = innerBounds;
        this.outerBounds = outerBounds;
        this.screen = screen;
        this.bindEvents();
    }

    public void setOuterBounds(Rectangle2D outerBounds) {
        this.outerBounds = outerBounds;
    }

    public void setInnerBounds(Rectangle2D innerBounds) {
        this.innerBounds = innerBounds;
    }

    public void bindEvents() {
//        this.tickEventCode = QUpdateEvent.addListener(QUpdateEvent.getInstance(), scene, this::tick, RenderEventPriority.HIGHER);
//        this.renderEventCode = QRenderEvent.addListener(QRenderEvent.getInstance(), scene, this::render, RenderEventPriority.HIGHER);

        BubbleBlaster.getEventBus().register(this);
        this.eventsActive = true;
    }

    @SuppressWarnings("EmptyMethod")
    @SubscribeEvent
    private void tick(TickEvent event) {

    }

    @SuppressWarnings("EmptyMethod")
    private void render(BubbleBlaster game, Renderer gg) {
    }

    public void add(Widget widget) {
        this.children.add(widget);
    }

    public void unbindEvents() {
//        QUpdateEvent.getInstance().removeListener(tickEventCode);
//        QRenderEvent.getInstance().removeListener(renderEventCode);

        BubbleBlaster.getEventBus().unregister(this);
        this.eventsActive = false;
    }

    public boolean eventsAreActive() {
        return eventsActive;
    }

    public void destroy() {
        this.unbindEvents();
    }

    @Override
    public void paint(@NotNull Renderer g) {
        this.containerGraphics = (Renderer) g.create((int) outerBounds.getX(), (int) outerBounds.getY(), (int) outerBounds.getWidth(), (int) outerBounds.getHeight());
        for (Widget child : this.children) {
            child.paint(this.containerGraphics);
        }
    }
}
