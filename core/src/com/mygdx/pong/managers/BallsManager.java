package com.mygdx.pong.managers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.pong.Application;
import com.mygdx.pong.models.Ball;
import com.mygdx.pong.utils.B2DBodyBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.mygdx.pong.utils.B2DConstants.PPM;

public class BallsManager implements Disposable {
    private static BallsManager manager;
    private World world;
    private final float MAX_BALL_NUM = 2;
    private int ballsCount;
    private Ball[] balls;
    private ArrayList<Body> ballBodiesUnActive;
    private BallsManager(final Application app) {
        // évite les soucis s'il y a du multi-threading
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.ballsCount = 0;
        this.balls = new Ball[2];

        this.ballBodiesUnActive = new ArrayList<>();
    }

    public static BallsManager getInstance(final Application app) {
        if (manager == null) {
            manager = new BallsManager(app);
        }
        return manager;
    }

    public int getBallsCount() {
        return this.ballsCount;
    }

    public Ball[] getBalls() {
        return this.balls;
    }

    public Ball[] getBallExcept(Ball ball) {
        Ball[] balls = new Ball[1];
        if (this.balls[0] == ball) {
            balls[0] = this.balls[1];
        } else {
            balls[0] = this.balls[0];
        }
        return balls;
    }

    public boolean addBall(final Ball ball, final Vector2 position, final Vector2 velocity) {
        final World worldFinal = world;
        if (this.ballBodiesUnActive.size() > 0) {
            final Body ballBody = this.ballBodiesUnActive.get(0);
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    if (!worldFinal.isLocked()) {
                        ballBody.setTransform(position, 0);
                        ballBody.setActive(true);

                    }
                }
            };
            executor.schedule(task, 0, TimeUnit.MILLISECONDS);
            ball.setBody(ballBody);
            ball.setVelocity(velocity);
            ballBody.setLinearVelocity(velocity);
            this.ballBodiesUnActive.remove(0);

            for (int i = 0; i < this.balls.length; i++) {
                if (this.balls[i] == null) {
                    this.balls[i] = ball;
                    this.ballsCount++;
                    return true;
                }
            }
        }
        return false;
    }

    public void removeBall(Ball ball) {
        if (this.ballsCount > 1) {

            for (int i = 0; i < this.ballsCount; i++) {
                if (this.balls[i] == ball) {
                    this.balls[i] = null;
                    this.ballsCount--;
                    break;
                }
            }

            for (int i = 1; i < this.balls.length; i++) {
                if (this.balls[i] != null) {
                    this.balls[i - 1] = this.balls[i];
                    this.balls[i] = null;
                }
            }


            final Body ballBody = ball.getBody();
            final World worldFinal = world;
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    if (!worldFinal.isLocked()) {
                        ballBody.setActive(false);
                    }
                }
            };
            executor.schedule(task, 0, TimeUnit.MILLISECONDS);

            this.ballBodiesUnActive.add(ballBody);
        }
    }

    public void createUnActiveBodies(final World world, Camera camera) {
        this.world = world;
        ballBodiesUnActive = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            final Body ballBody = B2DBodyBuilder.createCircle(world, camera.viewportWidth / 2,
                    camera.viewportHeight / 2, 8.0f, false, false, true, false);

            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    if (!world.isLocked()) {
                        ballBody.setActive(false);
                    }
                }
            };
            executor.schedule(task, 0, TimeUnit.MILLISECONDS);

            this.ballBodiesUnActive.add(ballBody);
        }
    }

    public void updateBalls(float delta) {
        for (int i = 0; i < this.balls.length; i++) {
            if (this.balls[i] != null) {
                this.balls[i].update(delta);
            }
        }
    }

    public void dispose() {
        for (int i = 0; i < this.balls.length; i++) {
            if (this.balls[i] != null) {
                this.balls[i].dispose();
                this.balls[i] = null;
            }
        }
    }
}
