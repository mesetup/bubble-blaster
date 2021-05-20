package com.qtech.bubbleblaster;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class QBubbles1 extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.00f, 0.75f, 1.00f, 1.00f);
        shapeRenderer.rect(0, 0, 50, 50);
        shapeRenderer.end();
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
