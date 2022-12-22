package com.mygdx.pong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.pong.Application;
import com.mygdx.pong.managers.BallsManager;
import com.mygdx.pong.managers.InputManager.Player;
import com.mygdx.pong.managers.PowerUpManager;
import com.mygdx.pong.models.Ball;
import com.mygdx.pong.models.PowerUp;
import com.mygdx.pong.models.Racket;
import com.mygdx.pong.utils.B2DBodyBuilder;
import com.mygdx.pong.utils.B2DJointBuilder;
import com.mygdx.pong.utils.MoveBodyTask;

import java.util.ArrayList;

import static com.mygdx.pong.utils.B2DConstants.PPM;

// ------- ATTENTION -------
// À NE SURTOUT PAS CONFONDRE
// SCREEN COORDINATES AVEC WORLD COORDINATES
// ET DONC BIEN COMPRENDRE L'UTILISATION DE PPM
// -------------------------

public class GameScreen extends AbstractScreen {
    /**
     * Une caméra utilisant la projection orthographique pour dessiner les éléments du jeu en 2D.
     */
    OrthographicCamera camera;

    /**
     * Le monde physique Box2D.
     */
    World world;
    private final Vector2 gravity = new Vector2(0, 0);                                    // La gravité du monde physique

    /**
     * Un objet permettant d’afficher les formes des corps Box2D (associés aux sprites) à l’écran
     */
    Box2DDebugRenderer b2dr;

    /**
     * Une liste de tâches à effectuer sur les corps Box2D (absolument nécessaire pour éviter les erreurs de concurrence)
     */
    ArrayList<MoveBodyTask> moveBodyTaskList = new ArrayList<>();
    ArrayList<Body> bodiesToRemove = new ArrayList<>();

    /**
     * Une des raquettes du jeu
     */
    Racket racketA, racketB;

    private final BallsManager ballsManager = BallsManager.getInstance(app);
    private final PowerUpManager powerUpManager = PowerUpManager.getInstance(app, ballsManager);

    //Body ball;
    private final boolean IS_TUTORIAL = false;
    private final boolean SHOW_DEBUG = false;
    private Vector2 direction = Vector2.Zero;
    private Vector2 contactPoint = Vector2.Zero;
    private final float INITIAL_BALL_SPEED_FACTOR = 2f;                                // La vitesse initiale de la balle (en m/s)

    private final float BALL_SPAWN_OFFSET = 120.0f;
    private final float RAY_DISTANCE = 8f;

    private boolean nextDirectionRight = true;
    public GameScreen(final Application app) {
        super(app);

        this.camera = new OrthographicCamera();                                                         // Crée une caméra orthographique
        this.camera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);                // Définit la taille de la caméra
        this.b2dr = new Box2DDebugRenderer();
    }

    @Override
    public void show() {
        world = new World(gravity, true);                                                       // Crée un monde physique Box2D
        powerUpManager.setWorld(world);
        // Contrôle les collisions entre les corps Box2D du monde physique
        world.setContactListener(new ContactListener() {
            /**
             * Appelée lorsque deux corps Box2D commencent à entrer en collision.
             * @param contact Le contact entre les deux corps Box2D
             */
            @Override
            public void beginContact(Contact contact) {
                Fixture fa = contact.getFixtureA();
                Fixture fb = contact.getFixtureB();

                for (Ball ball : ballsManager.getBalls()) {
                    if (ball == null) continue;

                    if (fa.getBody() == ball.getBody() || fb.getBody() == ball.getBody()) {
                        for (Ball otherBall : ballsManager.getBallExcept(ball)) {
                            if (otherBall == null) continue;
                            if (fa.getBody() == otherBall.getBody() || fb.getBody() == otherBall.getBody()) {
                                contact.setEnabled(false);
                            }
                        }
                        // Position aléatoire à laquelle la balle va apparaître suite à une sortie de la balle
                        float randomY = MathUtils.random(BALL_SPAWN_OFFSET, camera.viewportHeight - BALL_SPAWN_OFFSET);
                        if ((fa.getBody() == racketA.getGoalBody() || fb.getBody() == racketA.getGoalBody() ||
                                fa.getBody() == racketB.getGoalBody() || fb.getBody() == racketB.getGoalBody())) {                   // avec l’une des buts
                            if (ballsManager.getBallsCount() == 1) {
                                moveBodyTaskList.add(new MoveBodyTask(ball.getBody(), camera.viewportWidth / 2 / PPM, randomY / PPM));
                            } else if (ballsManager.getBallsCount() > 1) {
                                ballsManager.removeBall(ball);
                            }
                        }
                    }

                    // À partir d’un point de contact, on s’occupe de la réflexion de la balle
                    float contactPointY = contact.getWorldManifold().getPoints()[0].y;
                    handleBallCollisionWith(ball, racketA, fa, fb, contactPointY);
                    handleBallCollisionWith(ball, racketB, fa, fb, contactPointY);


                    if (fa.getBody() == ball.getBody() || fb.getBody() == ball.getBody()) {
                        if (powerUpManager.getCurrPowerUp() != null &&
                                (fa.getBody() == powerUpManager.getCurrPowerUp().getBody() || fb.getBody() == powerUpManager.getCurrPowerUp().getBody())) {
                            powerUpManager.setPowerUpBodyActive(world, false);
                            powerUpManager.getCurrPowerUp().setBall(ball);
                            powerUpManager.getCurrPowerUp().ApplyAndRemoveEffect();
                            powerUpManager.getCurrPowerUp().setPowerUpState(PowerUp.State.IN_USE);
                        }
                    }
                }
            }


            /**
             * Appelée lorsque deux corps Box2D cessent d’être en collision.
             * @param contact Le contact entre les deux corps Box2D
             */
            @Override
            public void endContact(Contact contact) {

            }

            /**
             * Appelée lorsque deux corps Box2D sont en collision et que l’un des deux entre en mouvement.
             * @param contact Le contact entre les deux corps Box2D
             * @param oldManifold L’ancien état de collision entre les deux corps Box2D
             */
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            /**
             * Appelée lorsque deux corps Box2D sont en collision et que l’un des deux entre en mouvement.
             * @param contact Le contact entre les deux corps Box2D
             * @param impulse L’impulsion appliquée aux deux corps Box2D
             */
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }

            /**
             * @param racket La raquette considérée
             * @param fa Le premier corps Box2D du contact
             * @param fb Le second corps Box2D du contact
             * @return true si la balle entre en collision avec la raquette donnée en paramètre
             */
            private boolean isBallCollidingWith(Ball ball, Racket racket, Fixture fa, Fixture fb) {
                return (fa.getBody() == ball.getBody() && fb.getBody() == racket.getPlayerBody()) ||
                        (fb.getBody() == ball.getBody() && fa.getBody() == racket.getPlayerBody());
            }

            /**
             * Gère la collision entre la balle et la raquette donnée en paramètre
             * @param racket La raquette considérée
             * @param fa Le premier corps Box2D du contact
             * @param fb Le second corps Box2D du contact
             * @param contactPointY La coordonnée Y du point de contact entre la balle et la raquette
             */
            private void handleBallCollisionWith(Ball ball, Racket racket, Fixture fa, Fixture fb, float contactPointY) {
                if (isBallCollidingWith(ball, racket, fa, fb)) {
                    float collisionOffset = (contactPointY - racket.getPlayerBody().getPosition().y) / (racket.getRacketSize() / 2 / PPM);
                    ball.setVelocity(new Vector2(
                            -ball.getVelocity().x * (1 / Math.abs(collisionOffset) / 2),
                            MathUtils.clamp(Math.abs(ball.getVelocity().y) * 100 * (collisionOffset / Math.abs(collisionOffset)), -6, 6)
                    ).nor().scl(ball.getVelocity().dst(0, 0)));
                    ball.setLastHitRacket(racket);
                }
            }
        });

        initArena();                                        // Initialise le terrain de jeu (bords, buts, raquettes, balle)

        app.batch.setProjectionMatrix(camera.combined);                                             // Définit la caméra pour le batch
        app.shapeBatch.setProjectionMatrix(camera.combined);                                        // Définit la caméra pour le shapeBatch
    }

    @Override
    public void update(float delta) {
        world.step(1f / Application.APP_FPS, 6, 2);             // Met à jour le monde physique Box2D
        if (!moveBodyTaskList.isEmpty()) {                                                       // Si la liste de tâches n’est pas vide,
            for (MoveBodyTask moveBodyTask : moveBodyTaskList) {                                 // pour chaque tâche,
                moveBodyTask.move();                                                             // effectue la tâche de déplacement.
                for (Ball ball : ballsManager.getBalls()) {                                       // Pour chaque balle,
                    if (ball == null) continue;
                    if (moveBodyTask.getBody() == ball.getBody()) {                                            // Si la tâche concerne la balle…
                        ball.setVelocity(new Vector2(0, 0));
                        Vector2 randomImpulse = new Vector2(nextDirectionRight ? 1 : -1, MathUtils.random(-.6f, .6f)).nor().scl(INITIAL_BALL_SPEED_FACTOR);
                        ball.getBody().applyLinearImpulse(randomImpulse, ball.getPosition(), true);
                        nextDirectionRight = !nextDirectionRight;
                    }
                }
            }
            moveBodyTaskList.clear();                                                            // Vide la liste de tâches
        }

        if (!bodiesToRemove.isEmpty()) {
            for (Body body : bodiesToRemove) {
                world.destroyBody(body);
            }
            bodiesToRemove.clear();
        }

        app.input.handleInput(delta);                                                            // Gère les entrées de l’utilisateur

        movePaddle(delta, racketA);                                                              // Déplace la raquette A
        movePaddle(delta, racketB);                                                              // Déplace la raquette B
        ballsManager.updateBalls(delta);

        for (final Ball ball : ballsManager.getBalls()) {
            if (ball == null || ball.getBody() == null || !ball.getBody().isActive() || ball.getVelocity().isZero())
                continue;
            // On envoie un rayon à partir de la balle dans la direction de la vitesse de la balle pour identifier si elle se dirige vers une des raquettes.
            world.rayCast(new RayCastCallback() {
                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                    if (fixture.getBody() == racketA.getPlayerBody() &&
                            (racketA.getPlayerBody().getPosition().dst2(racketA.getPrevPosition()) > 0.01f || direction == Vector2.Zero)) {
                        // Si le rayon rencontre la raquette A et si la balle est proche de cette dernière, on met à jour la direction opposée à la collision et le point de contact.
                        updateDirectionAndContactPoint(ball, racketA, point);
                    } else if (fixture.getBody() == racketB.getPlayerBody() &&
                            (racketB.getPlayerBody().getPosition().dst2(racketB.getPrevPosition()) > 0.01f || direction == Vector2.Zero)) {
                        // Si le rayon rencontre la raquette B et si la balle est proche de cette dernière, on met à jour la direction opposée à la collision et le point de contact.
                        updateDirectionAndContactPoint(ball, racketB, point);
                    } else {
                        direction = Vector2.Zero;
                        contactPoint = Vector2.Zero;
                    }
                    return 0;
                }
            }, ball.getPosition(), ball.getVelocity().nor().scl(RAY_DISTANCE).add(ball.getPosition()));
        }
        powerUpManager.update(delta);                                                            // Met à jour les power ups

        stage.act(delta);                                                                        // Met à jour le stage
    }

    @Override
    public void render(float delta) {
        super.render(delta);// Appelle la méthode render de la classe parente

        app.shapeBatch.begin(ShapeRenderer.ShapeType.Filled);                                                                           // Début du rendu des formes
        // ATTENTION: shapeBatch.begin() consomme beaucoup de ressources [À NE APPELER QU'EN CAS DE STRICTE NÉCESSITÉ]
        app.shapeBatch.setColor(Color.WHITE);                                                                                           // Définit la couleur du rendu des formes
        app.shapeBatch.rect(racketA.getPlayerBody().getPosition().x * PPM - Racket.getRacketThickness() / 2,
                racketA.getPlayerBody().getPosition().y * PPM - racketA.getRacketSize() / 2, Racket.getRacketThickness(), racketA.getRacketSize());     // Dessine la raquette A
        app.shapeBatch.rect(racketB.getPlayerBody().getPosition().x * PPM - Racket.getRacketThickness() / 2,
                racketB.getPlayerBody().getPosition().y * PPM - racketB.getRacketSize() / 2, Racket.getRacketThickness(), racketB.getRacketSize());     // Dessine la raquette B
        for (Ball ball : ballsManager.getBalls()) {
            if (ball == null) continue;
            app.shapeBatch.circle(ball.getPosition().x * PPM, ball.getPosition().y * PPM, ball.getRadius());
        }
        // Dessine la balle
        // ATTENTION: NE PAS OUBLIER DE FERMER LE shapeBatch.
        if (powerUpManager.getCurrPowerUp() != null && powerUpManager.getCurrPowerUp().getState() == PowerUp.State.UNTOUCHED) {
            app.shapeBatch.setColor(powerUpManager.getCurrPowerUp().getColor());
            app.shapeBatch.circle(powerUpManager.getCurrPowerUp().getPosition().x * PPM, powerUpManager.getCurrPowerUp().getPosition().y * PPM, powerUpManager.getCurrPowerUp().getRadius());
        }
        app.shapeBatch.end();

        if (IS_TUTORIAL && contactPoint != Vector2.Zero && direction != Vector2.Zero) {             // Dessine la direction de rebondissement de la balle.
            Gdx.gl.glLineWidth(5);
            app.shapeBatch.begin(ShapeRenderer.ShapeType.Line);
            app.shapeBatch.setColor(Color.RED);
            app.shapeBatch.line(contactPoint.x * PPM, contactPoint.y * PPM, contactPoint.x * PPM + direction.x * 8, contactPoint.y * PPM + direction.y * 8);
            app.shapeBatch.end();
            Gdx.gl.glLineWidth(1);
        }

        if (SHOW_DEBUG) {
            b2dr.render(world, camera.combined.cpy().scl(PPM));                                              // Dessine les formes Box2D
        }

        stage.draw();                                                                                        // Dessine le stage
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();                                                             // Détruit le monde physique Box2D
        b2dr.dispose();                                                              // Détruit le renderer Box2D
    }

    public World getWorld() {
        return world;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    private void initArena() {
        createWalls();

        ballsManager.createUnActiveBodies(world, camera);

        // Initialise les raquettes
        racketA = new Racket(world, camera, app.input.getPlayerA());
        racketB = new Racket(world, camera, app.input.getPlayerB());

        racketA.setOpponent(racketB);
        racketB.setOpponent(racketA);

        // Crée des corps Box2D pour les raquettes
        Body paddleLeft = B2DBodyBuilder.createBox(world, 40, camera.viewportHeight / 2, Racket.getRacketThickness(), racketA.getRacketSize(),
                false, true, false);
        Body paddleRight = B2DBodyBuilder.createBox(world, camera.viewportWidth - 40, camera.viewportHeight / 2, Racket.getRacketThickness(), racketB.getRacketSize(),
                false, true, false);

        // Communique les corps Box2D aux raquettes
        racketA.setPlayerBody(paddleLeft);
        racketB.setPlayerBody(paddleRight);

        // Crée des corps Box2D pour les buts qui sont également des capteurs
        Body goalLeft = B2DBodyBuilder.createBox(world, Racket.getRacketThickness() / 2, camera.viewportHeight / 2, Racket.getRacketThickness(), camera.viewportHeight,
                true, true, true);
        Body goalRight = B2DBodyBuilder.createBox(world, camera.viewportWidth - Racket.getRacketThickness() / 2,
                camera.viewportHeight / 2, Racket.getRacketThickness(), camera.viewportHeight, true, true, true);

        // Communique les corps Box2D des buts aux raquettes
        racketA.setGoalBody(goalLeft);
        racketB.setGoalBody(goalRight);

        // Crée des joints Box2D pour les raquettes avec les buts correspondants
        B2DJointBuilder.createPrismaticJoint(world, goalLeft, paddleLeft, camera.viewportHeight / 2, -camera.viewportHeight / 2,
                new Vector2(35 / PPM, 0), new Vector2(0, 0));
        B2DJointBuilder.createPrismaticJoint(world, goalRight, paddleRight, camera.viewportHeight / 2, -camera.viewportHeight / 2,
                new Vector2(-35 / PPM, 0), new Vector2(0, 0));

        float randomY = MathUtils.random(BALL_SPAWN_OFFSET, camera.viewportHeight - BALL_SPAWN_OFFSET);
        Ball ball = new Ball(MathUtils.random() > .5 ? racketA : racketB, 8.0f);
        if (ballsManager.addBall(ball, new Vector2(camera.viewportWidth / 2 / PPM, randomY / PPM), new Vector2(0, 0))) {
            Vector2 randomImpulse = new Vector2(MathUtils.random(0, 1f) > 0.5f ? MathUtils.random(0.5f, 1f) : MathUtils.random(-1f, -0.5f),
                    MathUtils.random(-.8f, .8f)).nor().scl(INITIAL_BALL_SPEED_FACTOR);
            nextDirectionRight = randomImpulse.x < 0;
            ball.getBody().applyLinearImpulse(randomImpulse, ball.getPosition(), true);
        }
    }

    /**
     * Crée les murs de l’arène
     */
    private void createWalls() {
        Vector2[] verts = new Vector2[5];
        verts[0] = new Vector2(1f / PPM, 0);
        verts[1] = new Vector2(camera.viewportWidth / PPM, 0);
        verts[2] = new Vector2(camera.viewportWidth / PPM, (camera.viewportHeight - 1f) / PPM);
        verts[3] = new Vector2(1f / PPM, (camera.viewportHeight - 1f) / PPM);
        verts[4] = new Vector2(1f / PPM, 0);
        B2DBodyBuilder.createChain(world, verts, true, true);
    }

    /**
     * Déplace la raquette en fonction de l’état du joueur
     * @param delta Temps écoulé depuis le dernier appel
     * @param racket Raquette à déplacer
     */
    private void movePaddle(float delta, Racket racket) {
        if (racket.getPlayer().getState() == Player.State.GOING_UP) {
            racket.getPlayerBody().setLinearVelocity(0, (racket.getIsInverted() ? -1 : 1) * racket.getRacketSpeed() * delta);
        } else if (racket.getPlayer().getState() == Player.State.GOING_DOWN) {
            racket.getPlayerBody().setLinearVelocity(0, (racket.getIsInverted() ? 1 : -1) * racket.getRacketSpeed() * delta);
        } else {
            racket.getPlayerBody().setLinearVelocity(0, 0);
        }
    }

    /**
     * Met à jour la direction de collision et le point de contact de la balle
     * @param racket Raquette avec laquelle la balle a entré en collision
     * @param point Point de contact de la balle avec la raquette
     */
    private void updateDirectionAndContactPoint(Ball ball, Racket racket, Vector2 point) {
        float collisionOffset = (point.y - racket.getPlayerBody().getPosition().y) / (racket.getRacketSize() / 2 / PPM);
        direction = new Vector2(
                -ball.getVelocity().x * (1 / Math.abs(collisionOffset) / 2),
                MathUtils.clamp(Math.abs(ball.getVelocity().y) * 100 * (collisionOffset / Math.abs(collisionOffset)), -6, 6)
        ).nor().scl(ball.getVelocity().dst(0, 0));
        contactPoint = point;
        racket.setPrevPosition(racket.getPlayerBody().getPosition());
    }
}
