package com.mygdx.pong.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.pong.Application;
import com.mygdx.pong.controllers.RacketController;
import com.mygdx.pong.screens.GameScreen;

public final class InputManager {
    public class Player implements RacketController {
        State state = State.IDLE;
        @Override
        public State getState() { return state; }
    }
    private static InputManager manager = null;
    private final Application app;
    Player playerA = new Player(), playerB = new Player();

    private InputManager(final Application app) {
        // évite les soucis s'il y a du multi-threading
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.app = app;
    }

    public static InputManager getInstance(final Application app) {
        if (manager == null) {
            manager = new InputManager(app);
        }
        return manager;
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    /**
     * Mettre à jour l’état des joueurs
     * @param delta Temps écoulé depuis le dernier appel
     */
    public void handleInput(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerA.state = RacketController.State.GOING_UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerA.state = RacketController.State.GOING_DOWN;
        } else if (playerA.state != RacketController.State.IDLE) {
            playerA.state = RacketController.State.IDLE;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerB.state = RacketController.State.GOING_UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerB.state = RacketController.State.GOING_DOWN;
        } else if (playerB.state != RacketController.State.IDLE) {
            playerB.state = RacketController.State.IDLE;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ((GameScreen) app.gsm.getScreen(GameScreenManager.State.PLAY)).switchPause();
        }
    }
}
