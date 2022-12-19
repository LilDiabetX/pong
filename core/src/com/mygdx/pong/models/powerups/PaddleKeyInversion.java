package com.mygdx.pong.models.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pong.models.Ball;
import com.mygdx.pong.models.PowerUp;
import com.mygdx.pong.models.Racket;

public class PaddleKeyInversion extends PowerUp {
    private final Ball hitByBall;
    private Racket appliedTo;

    public PaddleKeyInversion(Ball hitByBall, Vector2 position, float radius, Color color) {
        super(hitByBall, position, radius, color);
        this.hitByBall = hitByBall;
    }

    @Override
    public void applyEffect() {
        appliedTo = hitByBall.getRacketNextHitting();
        appliedTo.flipIsInverted();
    }
    
    @Override
    public void removeEffect() {
        appliedTo.flipIsInverted();
    }
}
