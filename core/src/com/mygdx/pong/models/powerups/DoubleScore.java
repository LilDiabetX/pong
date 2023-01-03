package com.mygdx.pong.models.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pong.models.PowerUp;
import com.mygdx.pong.models.Racket;

public class DoubleScore extends PowerUp {

    private Racket appliedTo;
    Sound bonus = Gdx.audio.newSound(Gdx.files.internal("Sounds/bonusPong.wav"));

    public DoubleScore(Vector2 position, float radius, Color color) {
        super(position, radius, color);
    }

    @Override
    public void applyEffect() {
        bonus.play(1.0f);
        appliedTo = getBall().getRacketHitBy();
        appliedTo.setDoubleScore(true);
    }

    @Override
    public void removeEffect() {
        bonus.play(1.0f);
        appliedTo.setDoubleScore(false);
    }
}
