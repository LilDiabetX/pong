package com.mygdx.pong.screens;

import com.mygdx.pong.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType.Bitmap;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.pong.Application;
import com.mygdx.pong.managers.GameScreenManager;
import com.mygdx.pong.managers.GameScreenManager.State;

public class EndScreen extends AbstractScreen {

    private final GameScreenManager gsm;

    private OrthographicCamera camera;
    private Viewport viewport;

    private BitmapFont scoreFont;
    private BitmapFont labelFont;
    private BitmapFont buttonFont;
    private BitmapFont replayFont;

    private Label message;

    private final int scoreJ1;
    private final int scoreJ2;

    private final String gagnant;

    private TextButton homeButton;
    private TextButton playButton;
    private TextButton quitButton;

    private Stage stage;


    public EndScreen(final Application app, final GameScreenManager gsm, final int scoreJ1, final int scoreJ2) {
        super(app);
        this.gsm = gsm;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);

        SpriteBatch batch = new SpriteBatch();
        this.viewport = new FitViewport(Application.V_WIDTH, Application.V_HEIGHT, camera);
        this.viewport.apply();

        this.stage = new Stage(viewport, batch);

        this.scoreJ1 = scoreJ1;
        this.scoreJ2 = scoreJ2;

        if (scoreJ1 > scoreJ2) {
            this.gagnant = "Player 1";
        } else if (scoreJ1 < scoreJ2) {
            this.gagnant = "Player 2";
        } else {
            this.gagnant = null;
        }
    }

    @Override
    public void show() {
        scoreFont = setupBitMapFont("fonts/Prototype.ttf", 100, Color.WHITE, 1);
        replayFont = setupBitMapFont("fonts/Prototype.ttf", 60, Color.WHITE, 1);
        buttonFont = setupBitMapFont("fonts/Prototype.ttf", 45, Color.WHITE, 1);
        labelFont = setupBitMapFont("fonts/Prototype.ttf", 80, Color.WHITE, 1);
        Gdx.input.setInputProcessor(stage);

        LabelStyle styleScore = new LabelStyle(scoreFont, Color.WHITE);
        Label score_1 = new Label(scoreJ1+"", styleScore);
        Label score_2 = new Label(scoreJ2+"", styleScore);


        LabelStyle styleLabel = new LabelStyle(labelFont, Color.WHITE);
        if (gagnant != null) {
            message = new Label(gagnant+ " wins !", styleLabel);
        } else {
            message = new Label("Draw", styleLabel);
        }

        TextButton.TextButtonStyle styleReplay = new TextButton.TextButtonStyle();
        styleReplay.font = replayFont;
        styleReplay.fontColor = Color.WHITE;

        playButton = new TextButton("PLAY AGAIN", styleReplay);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                
                gsm.put(State.PLAY, new GameScreen(app, gsm));
                gsm.setScreen(State.PLAY);
            }
        });

        TextButton.TextButtonStyle styleButton = new TextButton.TextButtonStyle();
        styleButton.font = buttonFont;
        styleButton.fontColor = Color.WHITE;

        homeButton = new TextButton("MENU", styleButton);
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                
                gsm.setScreen(State.MENU);
            }
        });

        quitButton = new TextButton("QUIT", styleButton);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Table scoreTable = new Table();
        scoreTable.add(score_1).padRight(Application.V_WIDTH/4); 
        scoreTable.add(score_2).padLeft(Application.V_WIDTH/4);

        
        

        Table buttonsTable = new Table();
        buttonsTable.add(homeButton).padRight(Application.V_WIDTH/16);
        buttonsTable.add(quitButton).padLeft(Application.V_WIDTH/16);
        
        
        VerticalGroup groupe = new VerticalGroup();

        groupe.setOrigin(groupe.getWidth()/2, groupe.getHeight());
        groupe.setPosition(Gdx.graphics.getWidth()/2  - (groupe.getWidth()/2 ), Gdx.graphics.getHeight() - (groupe.getHeight()));

        groupe.addActor(scoreTable);
        groupe.addActor(message);
        groupe.addActor(playButton);
        groupe.addActor(buttonsTable);

        groupe.space(Application.V_HEIGHT/8);

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
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void update(float delta) {
        
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    /*@Override
    public void dispose() {
        super.dispose();
        gsm.dispose();
        stage.dispose();

    }*/
    
    
}
