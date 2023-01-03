package com.mygdx.pong.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.pong.Application;
import com.mygdx.pong.models.PowerUp;
import com.mygdx.pong.models.powerups.DoubleBall;
import com.mygdx.pong.models.powerups.DoubleScore;
import com.mygdx.pong.models.powerups.PaddleKeyInversion;
import com.mygdx.pong.models.powerups.PaddleResize;
import com.mygdx.pong.utils.B2DBodyBuilder;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.mygdx.pong.utils.B2DConstants.PPM;

public final class PowerUpManager implements Disposable {
    private static PowerUpManager manager;
    private final Application app;
    private final BallsManager ballsManager;

    private World world;
    public enum POWER_UPS {
        NONE,
        PADDLE_KEY_INVERSION,
        PADDLE_SIZE_INCREASE,
        PADDLE_SIZE_DECREASE,
        BALL_DOUBLING,
        SCORE_DOUBLING
    }
    private final float POWER_UP_EVERY = 8.0f;
    private final float POWER_UP_DURATION = 10.0f;
    private final float[] powerUpDistribution = {.0f, .0f, .0f, .0f, .0f, 1f};
    private float powerUpTimer = 0.0f;
    private boolean isPowerUpInScreen = false;
    private PowerUp currPowerUp = null;
    private Body currPowerUpBody;

    private PowerUpManager(final Application app, final BallsManager ballsManager) {
        // évite les soucis s'il y a du multi-threading
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.app = app;
        this.ballsManager = ballsManager;
    }

    public static PowerUpManager getInstance(final Application app, final BallsManager ballsManager) {
        if (manager == null) {
            manager = new PowerUpManager(app, ballsManager);
        }
        return manager;
    }

    public void dispose() {
        if (currPowerUpBody != null && currPowerUp != null) {
            currPowerUp.dispose();
            currPowerUp = null;
        }
    }

    public PowerUp getCurrPowerUp() {
        return currPowerUp;
    }

    public void setCurrPowerUpNull() {
        currPowerUp = null;
    }
    public void setCurrPowerUpBodyNull() {
        currPowerUpBody = null;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void update(float delta) {
        if (isPowerUpInScreen) {
            powerUpTimer += delta;
            if (powerUpTimer >= POWER_UP_DURATION) {
                if (currPowerUp != null) {
                    setPowerUpBodyActive(world, false);
                    currPowerUp.setBody(null);
                    removePowerUp();
                }
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
        if (currPowerUp != null && (currPowerUp.getState() == PowerUp.State.IN_USE || currPowerUp.getState() == PowerUp.State.TO_BE_REMOVED)) {
            currPowerUpBody.setActive(false);
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

    public void setPowerUpBodyActive(World world, final boolean active) {
        final World worldFinal = world;
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (!worldFinal.isLocked()) {
                    currPowerUpBody.setActive(active);
                }
            }
        };
        executor.schedule(task, 0, TimeUnit.MILLISECONDS);
    }

    private void addPowerUp(POWER_UPS powerUpType) {
        Vector2 randomPos = new Vector2(app.V_WIDTH / 3 + (float) Math.random() * app.V_WIDTH / 3, MathUtils.clamp((float) Math.random() * app.V_HEIGHT, 100, app.V_HEIGHT - 100));
        switch (powerUpType) {
            case PADDLE_KEY_INVERSION:
                currPowerUp = new PaddleKeyInversion(randomPos.scl(1 / PPM), 100, Color.RED);
                break;
            case PADDLE_SIZE_INCREASE:
                currPowerUp = new PaddleResize(world, randomPos.scl(1 / PPM), 100, Color.GREEN, PaddleResize.State.BIGGER);
                break;
            case PADDLE_SIZE_DECREASE:
                currPowerUp = new PaddleResize(world, randomPos.scl(1 / PPM), 100, Color.ORANGE, PaddleResize.State.SMALLER);
                break;
            case BALL_DOUBLING:
                currPowerUp = new DoubleBall(ballsManager, randomPos.scl(1 / PPM), 100, Color.BLUE);
                break;
            case SCORE_DOUBLING:
                currPowerUp = new DoubleScore(randomPos.scl(1 / PPM), 100, Color.YELLOW);
                break;
            default:
                currPowerUp = null;
                break;
        }

        if (currPowerUp != null) {
            if (currPowerUpBody == null) {
                currPowerUpBody = B2DBodyBuilder.createCircle(world, currPowerUp.getPosition().x * PPM, currPowerUp.getPosition().y * PPM, currPowerUp.getRadius(),
                        true, true, false, true);
            } else {
                B2DBodyBuilder.updateShapeForCircle(currPowerUpBody, currPowerUp.getRadius(), true);
                currPowerUpBody.setTransform(currPowerUp.getPosition(), 0);
            }
            currPowerUp.setBody(currPowerUpBody);
            setPowerUpBodyActive(world, true);
        }
    }

    public void removePowerUp() {
        currPowerUp = null;
    }
}
