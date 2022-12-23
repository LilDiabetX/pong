package com.mygdx.pong.models.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pong.managers.BallsManager;
import com.mygdx.pong.models.Ball;
import com.mygdx.pong.models.PowerUp;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import static com.mygdx.pong.utils.B2DConstants.PPM;

public class DoubleBall extends PowerUp {
    private final BallsManager ballsManager;
    private Ball newBall;
    private Sound bonus = Gdx.audio.newSound(Gdx.files.internal("Sounds/bonusPong.wav"));

    public DoubleBall(BallsManager ballsManager, Vector2 position, float radius, Color color) {
        super(position, radius, color);
        this.ballsManager = ballsManager;
    }

    @Override
    public void applyEffect() {
        bonus.play(1.0f);
        if (ballsManager.getBallsCount() < 2) {
            Ball prevBall = ballsManager.getBalls()[0];
            newBall = new Ball(prevBall.getRacketNextHitting(), prevBall.getRadius());
            ballsManager.addBall(newBall, prevBall.getPosition(), prevBall.getBody().getLinearVelocity().scl(-1));
        }
    }

    @Override
    public void removeEffect() {
        if (newBall != null) {
            ballsManager.removeBall(newBall);
        }
    }
}
