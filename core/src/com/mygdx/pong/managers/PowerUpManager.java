package com.mygdx.pong.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.pong.Application;
import com.mygdx.pong.models.Ball;
import com.mygdx.pong.models.PowerUp;
import com.mygdx.pong.models.powerups.PaddleKeyInversion;
import com.mygdx.pong.models.powerups.PaddleResize;
import com.mygdx.pong.screens.GameScreen;
import com.mygdx.pong.utils.B2DBodyBuilder;

public final class PowerUpManager {
    private static PowerUpManager manager;
    private final Application app;
    private World world;
    public enum POWER_UPS {
        NONE,
        PADDLE_KEY_INVERSION,
        PADDLE_SIZE_INCREASE,
        PADDLE_SIZE_DECREASE,
        BALL_DOUBLING
    }
    private final float POWER_UP_EVERY = 1.0f;
    private final float POWER_UP_DURATION = 15.0f;
    private float[] powerUpDistribution = {0.3f, 0.1f, 0.2f, 0.1f, 0.1f};
    private float powerUpTimer = 0.0f;
    private boolean isPowerUpInScreen = false;
    private PowerUp currPowerUp = null;

    private PowerUpManager(final Application app) {
        // évite les soucis s'il y a du multi-threading
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.app = app;
    }

    public static PowerUpManager getInstance(final Application app) {
        if (manager == null) {
            manager = new PowerUpManager(app);
        }
        return manager;
    }

    public PowerUp getCurrPowerUp() {
        return currPowerUp;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void update(float delta) {
        if (isPowerUpInScreen) {
            powerUpTimer += delta;
            if (powerUpTimer >= POWER_UP_DURATION) {
                removePowerUp();
                powerUpTimer = 0.0f;
                isPowerUpInScreen = false;
            }
        } else {
            powerUpTimer += delta;
            if (powerUpTimer >= POWER_UP_EVERY) {
                addPowerUp(getRandomPowerUp(powerUpDistribution));
                powerUpTimer = 0.0f;
                isPowerUpInScreen = true;
            }
        }
        if (currPowerUp != null && currPowerUp.getState() == PowerUp.State.TO_BE_REMOVED) {
            removePowerUp();
        }
    }

    private POWER_UPS getRandomPowerUp(float[] distribution) {
        float rand = (float) Math.random();
        float sum = 0.0f;
        for (int i = 0; i < distribution.length; i++) {
            sum += distribution[i];
            if (rand <= sum) {
                return POWER_UPS.values()[i];
            }
        }
        return null;
    }

    private void addPowerUp(POWER_UPS powerUpType) {
        Ball ball = ((GameScreen) app.gsm.getScreen(GameScreenManager.State.PLAY)).getBall();

        if (currPowerUp != null) {
            world.destroyBody(currPowerUp.getBody());
        }

        switch (powerUpType) {
            case PADDLE_KEY_INVERSION:
                currPowerUp = new PaddleKeyInversion(ball, new Vector2(200, 200), 20, Color.RED);
                break;
            case PADDLE_SIZE_INCREASE:
                currPowerUp = new PaddleResize(world, ball, new Vector2(200, 200), 100, Color.GREEN, PaddleResize.State.BIGGER);
                break;
            case PADDLE_SIZE_DECREASE:
                currPowerUp = new PaddleResize(world, ball, new Vector2(200, 200), 20, Color.BLUE, PaddleResize.State.SMALLER);
                break;
            case BALL_DOUBLING:
                break;
            default:
                currPowerUp = null;
                break;
        }

        if (currPowerUp != null) {
            currPowerUp.setBody(B2DBodyBuilder.createCircle(world, currPowerUp.getPosition().x, currPowerUp.getPosition().y, currPowerUp.getRadius(),
                    true, true, false, true));
        }
    }

    public void removePowerUp() {
        currPowerUp = null;
    }
}
