package com.mygdx.pong.utils;

import com.badlogic.gdx.physics.box2d.Body;

public class MoveBodyTask {
    Body body;
    float x;
    float y;

    public MoveBodyTask(Body body, float x, float y) {
        this.body = body;
        this.x = x;
        this.y = y;
    }

    public Body getBody() {
        return body;
    }

    public void move() {
        body.setTransform(x, y, body.getAngle());
    }
}
