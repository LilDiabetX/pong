package com.mygdx.pong.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import java.util.Timer;

public abstract class PowerUp extends RandomEvent implements Disposable {

    public enum State {
        UNTOUCHED, IN_USE, TO_BE_REMOVED
    }

    private final float radius;
    private final Color color;
    private Ball ball;
    private Body body;
    private State powerUpState = State.UNTOUCHED;
    public final Timer timer = new Timer();
    
    public PowerUp(Vector2 position, float radius, Color color) {
        super(position);
        this.radius = radius;
        this.color = color;
    }

    public float getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public Racket getRacketHitBy() {
        return ball.getRacketHitBy();
    }

    public Racket getRacketNextHitting() {
        return ball.getRacketNextHitting();
    }
    public Body getBody() {
        return body;
    }
    public State getState() {
        return powerUpState;
    }
    public Ball getBall() {
        return ball;
    }
    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);
        body.setTransform(position, 0);
    }
    public void setBody(Body body) {
        this.body = body;
    }

    public void setPowerUpState(State state) {
        powerUpState = state;
    }
    public void setBall(Ball ball) {
        if (this.ball == null) {
            this.ball = ball;
        }
    }
    public abstract void applyEffect();

    public abstract void removeEffect();
    
    public void ApplyAndRemoveEffect() {
        applyEffect();
        timer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if (powerUpState != State.TO_BE_REMOVED) {
                            removeEffect();
                            powerUpState = State.TO_BE_REMOVED;
                        }
                    }
                }, 15000
        );
    }

    public void dispose() {
        getBody().getWorld().destroyBody(getBody());
        timer.cancel();
    }
}
