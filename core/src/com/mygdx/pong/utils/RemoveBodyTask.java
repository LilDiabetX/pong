package com.mygdx.pong.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class RemoveBodyTask {
    World world;
    Body body;

    public RemoveBodyTask(World world, Body body) {
        this.world = world;
        this.body = body;
    }

    public void remove() {
        world.destroyBody(body);
    }
}
