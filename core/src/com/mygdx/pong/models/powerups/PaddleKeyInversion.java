package com.mygdx.pong.models.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pong.models.PowerUp;
import com.mygdx.pong.models.Racket;

public class PaddleKeyInversion extends PowerUp {
    private Racket appliedTo;

    public PaddleKeyInversion(Vector2 position, float radius, Color color) {
        super(position, radius, color);
    }

    @Override
    public void applyEffect() {
        appliedTo = getBall().getRacketNextHitting();
        appliedTo.flipIsInverted();
    }
    
    @Override
    public void removeEffect() {
        appliedTo.flipIsInverted();
    }
}
