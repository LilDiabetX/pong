package com.mygdx.pong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.pong.Application;
import com.mygdx.pong.managers.InputManager.Player;
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
    private Vector2 gravity = new Vector2(0, 0);                                    // La gravité du monde physique

    /**
     * Un objet permettant d’afficher les formes des corps Box2D (associés aux sprites) à l’écran
     */
    Box2DDebugRenderer b2dr;

    /**
     * Une liste de tâches à effectuer sur les corps Box2D (absolument nécessaire pour éviter les erreurs de concurrence)
     */
    ArrayList<MoveBodyTask> moveBodyTaskList = new ArrayList<>();

    /**
     * Une des raquettes du jeu
     */
    Racket racketA, racketB;

    //TODO: Créer une classe Ball
    //TODO: Ajouter une liste de balles
    Body ball;
    private final boolean IS_TUTORIAL = false;
    private final boolean SHOW_DEBUG = false;
    private Vector2 direction = Vector2.Zero;
    private Vector2 contactPoint = Vector2.Zero;
    private final float BALL_RADIUS = 8f;
    private final float INITIAL_BALL_SPEED_FACTOR = 2f;                                // La vitesse initiale de la balle (en m/s)
    private final float INITIAL_BALL_ACCELERATION = .01f;                                      // L'accélération de la balle (en m/s²)
    private final float MAXIMUM_BALL_SPEED = 60f;
    private float ballSpawnOffset = 120.0f;
    private final float RAY_DISTANCE = 8f;

    private boolean paused = false;
    private boolean nextDirectionRight = true;
    public GameScreen(final Application app) {
        super(app);

        this.camera = new OrthographicCamera();                                                         // Crée une caméra orthographique
        this.camera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);                // Définit la taille de la caméra
        this.b2dr = new Box2DDebugRenderer();
    }

    public boolean getPaused(){
        return paused;
    }

    @Override
    public void show() {
        world = new World(gravity, true);                                                       // Crée un monde physique Box2D
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

                if (fa.getBody() == ball || fb.getBody() == ball) {                                                         // Si la balle entre en collision…
                    // Position aléatoire à laquelle la balle va apparaître suite à une sortie de la balle
                    float randomY = MathUtils.random(ballSpawnOffset, camera.viewportHeight - ballSpawnOffset);
                    if (fa.getBody() == racketA.getGoalBody() || fb.getBody() == racketA.getGoalBody() ||
                        fa.getBody() == racketB.getGoalBody() || fb.getBody() == racketB.getGoalBody()) {                   // avec l’une des buts
                        moveBodyTaskList.add(new MoveBodyTask(ball,camera.viewportWidth / 2 / PPM, randomY / PPM));
                    }
                }

                // À partir d’un point de contact, on s’occupe de la réflexion de la balle
                float contactPointY = contact.getWorldManifold().getPoints()[0].y;
                handleBallCollisionWith(racketA, fa, fb, contactPointY);
                handleBallCollisionWith(racketB, fa, fb, contactPointY);
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
            private boolean isBallCollidingWith(Racket racket, Fixture fa, Fixture fb) {
                return (fa.getBody() == ball && fb.getBody() == racket.getPlayerBody()) ||
                        (fb.getBody() == ball && fa.getBody() == racket.getPlayerBody());
            }

            /**
             * Gère la collision entre la balle et la raquette donnée en paramètre
             * @param racket La raquette considérée
             * @param fa Le premier corps Box2D du contact
             * @param fb Le second corps Box2D du contact
             * @param contactPointY La coordonnée Y du point de contact entre la balle et la raquette
             */
            private void handleBallCollisionWith(Racket racket, Fixture fa, Fixture fb, float contactPointY) {
                if (isBallCollidingWith(racket, fa, fb)) {
                    float collisionOffset = (contactPointY - racket.getPlayerBody().getPosition().y) / (racket.getRacketSize() / 2 / PPM);
                    ball.setLinearVelocity(new Vector2(
                            -ball.getLinearVelocity().x * (1 / Math.abs(collisionOffset) / 2),
                            MathUtils.clamp(Math.abs(ball.getLinearVelocity().y) * 100 * (collisionOffset / Math.abs(collisionOffset)), -6, 6)
                    ).nor().scl(ball.getLinearVelocity().dst(0, 0)));
                }
            }
        });

        initArena();                                        // Initialise le terrain de jeu (bords, buts, raquettes, balle)

        app.batch.setProjectionMatrix(camera.combined);                                             // Définit la caméra pour le batch
        app.shapeBatch.setProjectionMatrix(camera.combined);                                        // Définit la caméra pour le shapeBatch
    }

    @Override
    public void update(float delta) {
        if(!paused){
            world.step(1f / Application.APP_FPS, 6, 2);             // Met à jour le monde physique Box2D
            if (!moveBodyTaskList.isEmpty()) {                                                       // Si la liste de tâches n’est pas vide,
                for (MoveBodyTask moveBodyTask : moveBodyTaskList) {                                 // pour chaque tâche,
                    moveBodyTask.move();                                                             // effectue la tâche de déplacement.

                    if (moveBodyTask.getBody() == ball) {                                            // Si la tâche concerne la balle…
                        ball.setLinearVelocity(0, 0);
                        Vector2 randomImpulse = new Vector2((nextDirectionRight ? 1 : -1) * 1, MathUtils.random(-.6f, .6f)).nor().scl(INITIAL_BALL_SPEED_FACTOR);
                        ball.applyLinearImpulse(randomImpulse, ball.getPosition(), true);
                        nextDirectionRight = !nextDirectionRight;
                    }

                }
                moveBodyTaskList.clear();                                                            // Vide la liste de tâches
            }
        }
        app.input.handleInput(delta);                                                            // Gère les entrées de l’utilisateur

        if(!paused){

            movePaddle(delta, racketA);                                                              // Déplace la raquette A
            movePaddle(delta, racketB);
            ball.applyLinearImpulse(ball.getLinearVelocity().add(ball.getLinearVelocity().nor().scl(INITIAL_BALL_ACCELERATION * delta)), ball.getPosition(), true);
            ball.setLinearVelocity(ball.getLinearVelocity().clamp(0, MAXIMUM_BALL_SPEED));           // Limite la vitesse de la balle

            // On envoie un rayon à partir de la balle dans la direction de la vitesse de la balle pour identifier si elle se dirige vers une des raquettes.
            world.rayCast(new RayCastCallback() {
                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                    if (fixture.getBody() == racketA.getPlayerBody() && (racketA.getPlayerBody().getPosition().dst2(racketA.getPrevPosition()) > 0.01f || direction == Vector2.Zero)) {
                        // Si le rayon rencontre la raquette A et si la balle est proche de cette dernière, on met à jour la direction opposée à la collision et le point de contact.
                        updateDirectionAndContactPoint(racketA, point);
                    } else if (fixture.getBody() == racketB.getPlayerBody() && (racketB.getPlayerBody().getPosition().dst2(racketB.getPrevPosition()) > 0.01f || direction == Vector2.Zero)) {
                        // Si le rayon rencontre la raquette B et si la balle est proche de cette dernière, on met à jour la direction opposée à la collision et le point de contact.
                        updateDirectionAndContactPoint(racketB, point);
                    } else {
                        direction = Vector2.Zero;
                        contactPoint = Vector2.Zero;
                    }

                    return 0;
                }
            }, ball.getPosition(), ball.getLinearVelocity().nor().scl(RAY_DISTANCE).add(ball.getPosition()));

            stage.act(delta);                                                                     // Met à jour le stage
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);                                                                                // Appelle la méthode render de la classe parente

        app.shapeBatch.begin(ShapeRenderer.ShapeType.Filled);                                                                           // Début du rendu des formes
        // ATTENTION: shapeBatch.begin() consomme beaucoup de ressources [À NE APPELER QUE EN CAS DE STRICTE NECESSITE]
        app.shapeBatch.setColor(Color.WHITE);                                                                                           // Définit la couleur du rendu des formes
        app.shapeBatch.rect(racketA.getPlayerBody().getPosition().x * PPM - Racket.getRacketThickness() / 2,
                racketA.getPlayerBody().getPosition().y * PPM - racketA.getRacketSize() / 2, Racket.getRacketThickness(), racketA.getRacketSize());     // Dessine la raquette A
        app.shapeBatch.rect(racketB.getPlayerBody().getPosition().x * PPM - Racket.getRacketThickness() / 2,
                racketB.getPlayerBody().getPosition().y * PPM - racketA.getRacketSize() / 2, Racket.getRacketThickness(), racketB.getRacketSize());     // Dessine la raquette B
        app.shapeBatch.circle(ball.getPosition().x * PPM, ball.getPosition().y * PPM, BALL_RADIUS);
        // Dessine la balle
        // ATTENTION: NE PAS OUBLIER DE FERMER LE shapeBatch.
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
        paused=!paused;
        System.out.println(paused);
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

    private void initArena() {
        createWalls();

        float randomY = MathUtils.random(ballSpawnOffset, camera.viewportHeight - ballSpawnOffset);
        ball = B2DBodyBuilder.createCircle(world, camera.viewportWidth / 2, randomY, BALL_RADIUS, false, false, true);
        ball.setLinearVelocity(0, 0);
        Vector2 randomImpulse = new Vector2(MathUtils.random(0, 1f) > 0.5f ? MathUtils.random(0.5f, 1f) : MathUtils.random(-1f, -0.5f), MathUtils.random(-.8f, .8f)).nor().scl(INITIAL_BALL_SPEED_FACTOR);
        nextDirectionRight = randomImpulse.x < 0;
        ball.applyLinearImpulse(randomImpulse, ball.getPosition(), true);

        // Initialise les raquettes
        racketA = new Racket(app.input.getPlayerA());
        racketB = new Racket(app.input.getPlayerB());

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
        Body goalRight = B2DBodyBuilder.createBox(world, camera.viewportWidth - Racket.getRacketThickness() / 2, camera.viewportHeight / 2, Racket.getRacketThickness(), camera.viewportHeight,
                true, true, true);

        // Communique les corps Box2D des buts aux raquettes
        racketA.setGoalBody(goalLeft);
        racketB.setGoalBody(goalRight);

        // Crée des joints Box2D pour les raquettes avec les buts correspondants
        B2DJointBuilder.createPrismaticJoint(world, goalLeft, paddleLeft, camera.viewportHeight / 2, -camera.viewportHeight / 2,
                new Vector2(35 / PPM, 0), new Vector2(0, 0));
        B2DJointBuilder.createPrismaticJoint(world, goalRight, paddleRight, camera.viewportHeight / 2, -camera.viewportHeight / 2,
                new Vector2(-35 / PPM, 0), new Vector2(0, 0));
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
            racket.getPlayerBody().setLinearVelocity(0, racket.getRacketSpeed() * delta);
        } else if (racket.getPlayer().getState() == Player.State.GOING_DOWN) {
            racket.getPlayerBody().setLinearVelocity(0, -racket.getRacketSpeed() * delta);
        } else {
            racket.getPlayerBody().setLinearVelocity(0, 0);
        }
    }

    private void updateDirectionAndContactPoint(Racket racket, Vector2 point) {
        float collisionOffset = (point.y - racket.getPlayerBody().getPosition().y) / (racket.getRacketSize() / 2 / PPM);
        direction = new Vector2(
                -ball.getLinearVelocity().x * (1 / Math.abs(collisionOffset) / 2),
                MathUtils.clamp(Math.abs(ball.getLinearVelocity().y) * 100 * (collisionOffset / Math.abs(collisionOffset)), -6, 6)
        ).nor().scl(ball.getLinearVelocity().dst(0, 0));
        contactPoint = point;
        racket.setPrevPosition(racket.getPlayerBody().getPosition());
    }
}
