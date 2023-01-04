package com.mygdx.pong.models;

import com.badlogic.gdx.math.Vector2;

public class RandomEvent {
    private Vector2 position;

    public RandomEvent(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
