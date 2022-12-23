package com.mygdx.pong.models.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.pong.models.PowerUp;
import com.mygdx.pong.models.Racket;
import com.mygdx.pong.utils.B2DBodyBuilder;
import com.badlogic.gdx.audio.Sound;

public class PaddleResize extends PowerUp {
    Sound bonus = Gdx.audio.newSound(Gdx.files.internal("Sounds/bonusPong.wav"));
    private final World world;
    public enum State {
        BIGGER, SMALLER
    }
    private final State powerUpState;
    private Racket appliedTo;

    public PaddleResize(World world, Vector2 position, float radius, Color color, State state) {
        super(position, radius, color);
        this.world = world;
        powerUpState = state;
    }
    @Override
    public void applyEffect() {
        bonus.play(1.0f);
        appliedTo = getBall().getRacketHitBy();
        if (powerUpState == State.BIGGER) {
            appliedTo.setRacketSize(appliedTo.getRacketSize() * 1.5f);
        } else {
            appliedTo.setRacketSize(appliedTo.getRacketSize() * 0.5f);
        }
        updateRacketSize();
    }

    @Override
    public void removeEffect() {
        bonus.play(1.0f);
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
