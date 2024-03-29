package com.mygdx.pong.models;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.pong.controllers.RacketController;

public class Racket {
    private final World world;
    private final Camera camera;
    private Racket opponent;
    private static int counter;
    private final int playerID;
    private final RacketController player;
    private Body playerBody;
    private Body goalBody;
    private float racketSize = 80.0f; // m
    private static final float racketThickness = 13.0f;
    private float racketSpeed = 600.0f; // m/s
    private Vector2 prevPosition = Vector2.Zero;
    private boolean isInverted = false;
    private boolean doubleScore = false;

    public Racket(World world, Camera camera, RacketController player){
        this.world = world;
        this.camera = camera;
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

    public int getScore() {
        return this.player.getScore();
    }

    public boolean getIsInverted() {
        return isInverted;
    }

    public Body getPlayerBody() {
        return playerBody;
    }

    public Body getGoalBody() {
        return goalBody;
    }

    public Vector2 getPrevPosition() {
        return prevPosition;
    }
    public Racket getOpponent() {
        return opponent;
    }
    public void setRacketSize(float racketSize) {
        this.racketSize = racketSize;
    }

    public void setRacketSpeed(float racketSpeed) {
        this.racketSpeed = racketSpeed;
    }

    public void setDoubleScore(boolean doubleScore) {
        this.doubleScore = doubleScore;
    }

    public void setPlayerBody(Body playerBody) {
        this.playerBody = playerBody;
    }

    public void setGoalBody(Body goalBody) {
        this.goalBody = goalBody;
    }

    public void setPrevPosition(Vector2 prevPosition) {
        this.prevPosition = prevPosition;
    }
    public void setOpponent(Racket opponent) {
        this.opponent = opponent;
    }

    public void resetScore() {
        this.player.resetScore();
    }

    public void addScore() {
        if (doubleScore) {
            player.doublilyIncrementScore();
        }
        else {
            player.incrementScore();
        }
    }

    public void flipIsInverted() {
        isInverted = !isInverted;
    }
    
    public String toString() {
        return String.valueOf(playerID);
    }
}
