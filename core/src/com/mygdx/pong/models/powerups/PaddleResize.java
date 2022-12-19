package com.mygdx.pong.models.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.pong.models.Ball;
import com.mygdx.pong.models.PowerUp;
import com.mygdx.pong.models.Racket;
import com.mygdx.pong.utils.B2DBodyBuilder;

public class PaddleResize extends PowerUp {
    private World world;
    public enum State {
        BIGGER, SMALLER
    }
    private final State powerUpState;
    private final Ball hitByBall;
    private Racket appliedTo;

    public PaddleResize(World world, Ball hitByBall, Vector2 position, float radius, Color color, State state) {
        super(hitByBall, position, radius, color);
        this.world = world;
        powerUpState = state;
        this.hitByBall = hitByBall;
    }
    @Override
    public void applyEffect() {
        appliedTo = hitByBall.getRacketHitBy();
        if (powerUpState == State.BIGGER) {
            appliedTo.setRacketSize(appliedTo.getRacketSize() * 1.5f);
        } else {
            appliedTo.setRacketSize(appliedTo.getRacketSize() * 0.5f);
        }
        updateRacketSize();
    }

    @Override
    public void removeEffect() {
        if (powerUpState == State.BIGGER) {
            appliedTo.setRacketSize(appliedTo.getRacketSize() / 1.5f);
        } else {
            appliedTo.setRacketSize(appliedTo.getRacketSize() / 0.5f);
        }
        updateRacketSize();
    }

    private void updateRacketSize() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                while (world.isLocked()) {}
                B2DBodyBuilder.updateShapeForBox(appliedTo.getPlayerBody(),
                        Racket.getRacketThickness(), appliedTo.getRacketSize(),false);
            }
        });
    }
}
