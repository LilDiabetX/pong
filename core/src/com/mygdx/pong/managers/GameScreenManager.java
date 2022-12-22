package com.mygdx.pong.managers;

import com.mygdx.pong.Application;
import com.mygdx.pong.screens.AbstractScreen;
import com.mygdx.pong.screens.GameScreen;

import java.util.HashMap;

public class GameScreenManager {

    private final Application app;

    private GameScreen gs;

    private HashMap<State, AbstractScreen> gameScreens;

    public enum State {
        MENU,
        PLAY,
        SETTINGS
    }

    public GameScreenManager(final Application app) {
        this.app = app;
        this.gs = new GameScreen(app);
        initGameScreens();
        setScreen(State.PLAY);
    }

    private void initGameScreens() {
        this.gameScreens = new HashMap<State, AbstractScreen>();
        this.gameScreens.put(State.PLAY, gs);
    }

    public void setScreen(State nextScreen) {
        app.setScreen(this.gameScreens.get(nextScreen));
    }

    public GameScreen getGameScreen(){
        return gs;
    }

    public void dispose() {
        for (AbstractScreen screen : this.gameScreens.values()) {
            if (screen != null) {
                screen.dispose();
            }
        }
    }
}
