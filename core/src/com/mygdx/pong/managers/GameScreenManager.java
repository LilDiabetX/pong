package com.mygdx.pong.managers;

import com.mygdx.pong.Application;
import com.mygdx.pong.screens.AbstractScreen;
import com.mygdx.pong.screens.GameScreen;

import javax.swing.*;
import java.util.HashMap;

public final class GameScreenManager {
    private static GameScreenManager manager;
    private final Application app;
    private HashMap<State, AbstractScreen> gameScreens;
    public enum State {
        MENU,
        PLAY,
        SETTINGS
    }

    private GameScreenManager(final Application app) {
        // évite les soucis s'il y a du multi-threading
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.app = app;
        initGameScreens();
        setScreen(State.PLAY);
    }

    public static GameScreenManager getInstance(final Application app) {
        if (manager == null) {
            manager = new GameScreenManager(app);
        }
        return manager;
    }

    private void initGameScreens() {
        this.gameScreens = new HashMap<State, AbstractScreen>();
        this.gameScreens.put(State.PLAY, new GameScreen(app));
    }

    public void setScreen(State nextScreen) {
        app.setScreen(this.gameScreens.get(nextScreen));
    }

    public AbstractScreen getScreen(State screen) {
        return this.gameScreens.get(screen);
    }

    public void dispose() {
        for (AbstractScreen screen : this.gameScreens.values()) {
            if (screen != null) {
                screen.dispose();
            }
        }
    }
}
