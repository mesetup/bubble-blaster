package com.qtech.bubbleblaster.gui.view;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.common.GraphicsProcessor;
import com.qtech.bubbleblaster.common.screen.Screen;
import com.qtech.bubbleblaster.event.SubscribeEvent;
import com.qtech.bubbleblaster.event.TickEvent;
import com.qtech.bubbleblaster.event.bus.EventBus;
import com.qtech.bubbleblaster.gui.Widget;

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
    private void render(BubbleBlaster game, GraphicsProcessor gg) {
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
    public void paint(GraphicsProcessor g) {
        this.containerGraphics = (GraphicsProcessor) g.create((int) outerBounds.getX(), (int) outerBounds.getY(), (int) outerBounds.getWidth(), (int) outerBounds.getHeight());
        for (Widget child : this.children) {
            child.paint(this.containerGraphics);
        }
    }
}