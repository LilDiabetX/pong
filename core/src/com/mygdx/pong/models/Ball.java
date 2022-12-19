package com.mygdx.pong.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;
import java.util.Vector;

public class Ball {
    private float radius = 8.0f; // m
    private Body body;
    private Racket lastHitRacket;

    public Ball(Racket player, float radius) {
        setLastHitRacket(player);
        this.radius = radius;
        this.body = null;
    }

    public float getRadius() {
        return radius;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Vector2 getVelocity() {
        return body.getLinearVelocity();
    }

    public Racket getRacketHitBy() {
        return lastHitRacket;
    }

    public Racket getRacketNextHitting() {
        return lastHitRacket.getOpponent();
    }

    public void setVelocity(Vector2 velocity) {
        body.setLinearVelocity(velocity);
    }

    public void setBody(Body body) throws IllegalStateException {
        if (this.body == null) {
            this.body = body;
        } else {
            throw new IllegalStateException("Body already set");
        }
    }

    public void setLastHitRacket(Racket racket) {
        lastHitRacket = racket;
    }
}
