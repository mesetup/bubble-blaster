package com.qsoftware.bubbles.environment;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.common.renderer.IRenderer;

import javax.annotation.Nullable;
import java.awt.*;

public class EnvironmentRenderer implements IRenderer {
    public EnvironmentRenderer() {

    }

    @Nullable
    public Environment getEnvironment() {
        if (QBubbles.getInstance() == null) {
            return null;
        }

        return QBubbles.getInstance().environment;
    }

    @Override
    public void render(Graphics2D gg) {
        Environment environment = getEnvironment();
        if (environment == null) {
            return;
        }

        for (Entity entity : environment.getEntities()) {
            entity.renderEntity(gg);
        }
    }
}
