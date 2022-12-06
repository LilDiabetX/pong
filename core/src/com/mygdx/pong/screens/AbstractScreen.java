package com.mygdx.pong.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.pong.Application;
import com.mygdx.pong.managers.GameScreenManager;

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
        this.stage.dispose();
    }
}
