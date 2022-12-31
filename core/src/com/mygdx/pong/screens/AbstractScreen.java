package com.mygdx.pong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.pong.Application;

public abstract class AbstractScreen implements Screen {
    protected final Application app;
    Stage stage;

    public AbstractScreen(final Application app) {
        this.app = app;
        this.stage = new Stage(new FitViewport(Application.V_WIDTH, Application.V_HEIGHT));
    }

    public abstract void update(float delta);

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0f, 0f, 0f, 1f);             // Efface l’écran
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        
    }

    public BitmapFont setupBitMapFont(String internalPath, int size, Color color, float a) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(internalPath));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = new Color(color.r, color.g, color.b, a);
        BitmapFont fnt = generator.generateFont(parameter);
        generator.dispose();
        return fnt;
    }
}
