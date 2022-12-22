package com.mygdx.pong.managers;

import com.badlogic.gdx.Game;
import com.mygdx.pong.Application;
import com.mygdx.pong.screens.AbstractScreen;
import com.mygdx.pong.screens.GameScreen;

import java.util.HashMap;

public class GameScreenManager {

    private final Application app;

    private HashMap<State, AbstractScreen> gameScreens;

    public enum State {
        MENU,
        PLAY,
        SETTINGS
    }

    public GameScreenManager(final Application app) {
        this.app = app;
        initGameScreens();
        setScreen(State.PLAY);
    }

    private void initGameScreens() {
        this.gameScreens = new HashMap<State, AbstractScreen>();
        this.gameScreens.put(State.PLAY, new GameScreen(app));
    }

    public void setScreen(State nextScreen) {
        app.setScreen(this.gameScreens.get(nextScreen));
    }

    public GameScreen getPlayScreen(){
        return (GameScreen) gameScreens.get(State.PLAY);
    }

    public void dispose() {
        for (AbstractScreen screen : this.gameScreens.values()) {
            if (screen != null) {
                screen.dispose();
            }
        }
    }
}
