package com.mygdx.pong.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.mygdx.pong.utils.B2DConstants.PPM;

public final class B2DBodyBuilder {

    private B2DBodyBuilder() {}
    public static Body createBox(World world, float x, float y, float width, float height, boolean isStatic, boolean fixedRotation, boolean isSensor) {
        Body body;

        BodyDef bDef = new BodyDef();
        if (isStatic) {
            bDef.type = BodyDef.BodyType.StaticBody;
        } else {
            bDef.type = BodyDef.BodyType.DynamicBody;
        }
        bDef.fixedRotation = fixedRotation;
        bDef.position.set(x / PPM, y / PPM);                    // Convertir les pixels en mètres
        body = world.createBody(bDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1f;
        fDef.isSensor = isSensor;
        body.createFixture(fDef);
        shape.dispose();
        return body;
    }

    public static Body createCircle(World world, float x, float y, float radius, boolean isStatic, boolean fixedRotation, boolean restitution) {
        Body body;

        BodyDef bDef = new BodyDef();
        if (isStatic) {
            bDef.type = BodyDef.BodyType.StaticBody;
        } else {
            bDef.type = BodyDef.BodyType.DynamicBody;
        }
        bDef.fixedRotation = fixedRotation;
        bDef.position.set(x / PPM, y / PPM);                    // Convertir les pixels en mètres
        body = world.createBody(bDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1f;
        fDef.friction = 0f;
        fDef.restitution = restitution ? 1.1f : 0f;
        body.createFixture(fDef);
        shape.dispose();
        return body;
    }

    public static Body createChain(World world, Vector2[] vertices, boolean isStatic, boolean fixedRotation) {
        Body body;

        BodyDef bDef = new BodyDef();
        if (isStatic) {
            bDef.type = BodyDef.BodyType.StaticBody;
        } else {
            bDef.type = BodyDef.BodyType.DynamicBody;
        }
        bDef.fixedRotation = fixedRotation;
        body = world.createBody(bDef);

        ChainShape shape = new ChainShape();
        shape.createChain(vertices);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1f;
        body.createFixture(fDef);
        shape.dispose();
        return body;
    }
}
