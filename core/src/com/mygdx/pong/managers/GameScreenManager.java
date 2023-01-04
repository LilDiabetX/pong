package com.mygdx.pong.managers;

import com.badlogic.gdx.utils.Disposable;
import com.mygdx.pong.Application;
import com.mygdx.pong.screens.AbstractScreen;
import com.mygdx.pong.screens.EndScreen;
import com.mygdx.pong.screens.GameScreen;
import com.mygdx.pong.screens.HomeScreen;

import java.util.HashMap;

public final class GameScreenManager implements Disposable {
    private static GameScreenManager manager;
    private final Application app;
    private HashMap<State, AbstractScreen> gameScreens;
    public enum State {
        MENU,
        PLAY,
        END
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
        setScreen(State.MENU);
    }

    public static GameScreenManager getInstance(final Application app) {
        if (manager == null) {
            manager = new GameScreenManager(app);
        }
        return manager;
    }

    /**
     * initialise les écrans du menu et du jeu
     */
    private void initGameScreens() {
        this.gameScreens = new HashMap<State, AbstractScreen>();
        this.put(State.MENU, new HomeScreen(app, this));
        this.put(State.PLAY, new GameScreen(app, this));
        
    }

    public void put(State key, AbstractScreen screen) {
        this.gameScreens.put(key, screen);
    }

    public void remove(State key) {
        this.gameScreens.remove(key);
    }

    public boolean containsState(State key) {
        return this.gameScreens.containsKey(key);
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
