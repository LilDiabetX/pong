package com.mygdx.pong.models.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pong.models.PowerUp;
import com.mygdx.pong.models.Racket;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class PaddleKeyInversion extends PowerUp {
    private Racket appliedTo;
    Sound bonus = Gdx.audio.newSound(Gdx.files.internal("Sounds/bonusPong.wav"));

    public PaddleKeyInversion(Vector2 position, float radius, Color color) {
        super(position, radius, color);
    }

    @Override
    public void applyEffect() {
        bonus.play(1.0f);
        appliedTo = getBall().getRacketNextHitting();
        appliedTo.flipIsInverted();
    }
    
    @Override
    public void removeEffect() {
        bonus.play(1.0f);
        appliedTo.flipIsInverted();
    }
}
