package com.mygdx.pong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.pong.Application;
import com.mygdx.pong.managers.GameScreenManager;
import com.mygdx.pong.managers.GameScreenManager.State;

public class HomeScreen extends AbstractScreen {

    private final GameScreenManager gsm;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Image logo;

    private BitmapFont buttonFont;

    private TextButton playButton;
    private TextButton quitButton;

    private Stage stage;


    public HomeScreen(final Application app, final GameScreenManager gsm) {
        super(app);
        this.gsm = gsm;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);

        SpriteBatch batch = new SpriteBatch();
        this.viewport = new FitViewport(Application.V_WIDTH, Application.V_HEIGHT, camera);
        this.viewport.apply();

        this.stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        buttonFont = setupBitMapFont("fonts/Prototype.ttf", 60, Color.WHITE, 1);
        Gdx.input.setInputProcessor(stage);

        logo = new Image(new Texture("logopong.png"));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = buttonFont;
        style.fontColor = Color.WHITE;


        playButton = new TextButton("PLAY", style);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setScreen(State.PLAY);
            }
        });

        quitButton = new TextButton("QUIT", style);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        
        VerticalGroup groupe = new VerticalGroup();

        groupe.setOrigin(groupe.getWidth()/2, groupe.getHeight());
        groupe.setPosition(Application.V_WIDTH/2  - (groupe.getWidth()/2 ), Application.V_HEIGHT - (groupe.getHeight()));        

        groupe.addActor(logo);
        groupe.addActor(playButton);
        groupe.addActor(quitButton);

        stage.addActor(groupe);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        stage.act();
        stage.draw();
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    
}
