package com.mygdx.pong.models;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.pong.controllers.RacketController;

public class Racket {
    private static int counter;
    private final int playerID;
    private RacketController player;
    private Body playerBody;
    private Body goalBody;
    private float racketSize = 80.0f; // m
    private static final float racketThickness = 10.0f;
    private float racketSpeed = 500.0f; // m/s


    public Racket(RacketController player){
        counter++;
        this.player=player;
        this.playerID = counter;
    }

    public float getRacketSize() {
        return racketSize;
    }

    public static float getRacketThickness() {
        return racketThickness;
    }

    public float getRacketSpeed() {
        return racketSpeed;
    }

    public RacketController getPlayer() {
        return player;
    }

    public Body getPlayerBody() {
        return playerBody;
    }

    public Body getGoalBody() {
        return goalBody;
    }

    public void setRacketSize(float racketSize) {
        this.racketSize = racketSize;
    }

    public void setRacketSpeed(float racketSpeed) {
        this.racketSpeed = racketSpeed;
    }

    public void setPlayerBody(Body playerBody) {
        this.playerBody = playerBody;
    }

    public void setGoalBody(Body goalBody) {
        this.goalBody = goalBody;
    }

    public String toString() {
        return String.valueOf(playerID);
    }
}
