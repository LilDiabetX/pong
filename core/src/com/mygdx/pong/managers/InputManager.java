package com.mygdx.pong.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.pong.Application;
import com.mygdx.pong.controllers.RacketController;

public class InputManager {
    public class Player implements RacketController {
        State state = State.IDLE;
        private int score = 0;
        private boolean inverted = false;

        @Override
        public State getState() { return state; }
    }
    private final Application app;
    Player playerA = new Player(), playerB = new Player();

    public InputManager(final Application app) {
        this.app = app;
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

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            app.getGameScreen().pause();
        }
    }
}
