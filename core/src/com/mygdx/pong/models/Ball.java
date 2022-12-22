package com.mygdx.pong.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

public class Ball implements Disposable {
    private static int compteur = 0;
    private final int id;
    private final float INITIAL_BALL_ACCELERATION = .02f;                              // L’accélération de la balle (en m/s²)
    private final float MAXIMUM_BALL_SPEED = 60f;
    private float radius = 8.0f; // m
    private Body body;
    private Racket lastHitRacket;

    public Ball(Racket player, float radius) {
        this.id = compteur++;
        setLastHitRacket(player);
        this.radius = radius;
        this.body = null;
    }

    public int getId() {
        return id;
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

    public void update(float delta) {
        if (getBody() == null) return;
        getBody().applyLinearImpulse(getVelocity().add(getVelocity().nor().scl(INITIAL_BALL_ACCELERATION * delta)), getPosition(), true);
        setVelocity(getVelocity().clamp(0, MAXIMUM_BALL_SPEED));           // Limite la vitesse de la balle
    }

    public void dispose() {
        body.getWorld().destroyBody(body);
    }
}
