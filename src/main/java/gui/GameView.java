package gui;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import model.Court;

public class GameView {
    // class parameters
    private final Court court;
    private final StackPane gameRoot; // main node of the game
    private final double scale;
    private final double xMargin = 50.0, racketThickness = 10.0; // pixels

    // children of the game main node
    private final Rectangle racketA, racketB;

    private final Label racketAScore, racketBScore;
    private final Circle ball;

    /**
     * @param court le "modèle" de cette vue (le terrain de jeu de raquettes et tout ce qu'il y a dessus)
     * @param root  le nœud racine dans la scène JavaFX dans lequel le jeu sera affiché
     * @param scale le facteur d'échelle entre les distances du modèle et le nombre de pixels correspondants dans la vue
     */
    public GameView(Court court, StackPane root, double scale) {
        this.court = court;
        this.gameRoot = root;
        this.scale = scale;

        root.setMinWidth(court.getWidth() * scale + 2 * xMargin);
        root.setMinHeight(court.getHeight() * scale);

        racketA = new Rectangle();
        racketA.setHeight(court.getRacketA().getRacketSize() * scale);
        racketA.setWidth(racketThickness);
        racketA.setFill(Color.BLACK);

        racketA.setX(xMargin - racketThickness);
        racketA.setY(court.getRacketA().getRacketPos() * scale);

        racketB = new Rectangle();
        racketB.setHeight(court.getRacketB().getRacketSize() * scale);
        racketB.setWidth(racketThickness);
        racketB.setFill(Color.BLACK);

        racketB.setX(court.getWidth() * scale + xMargin);
        racketB.setY(court.getRacketB().getRacketPos() * scale);

        ball = new Circle();
        ball.setRadius(court.getBall().getBallRadius());
        ball.setFill(Color.BLACK);

        ball.setCenterX(court.getBall().getBallX() * scale + xMargin);
        ball.setCenterY(court.getBall().getBallY() * scale);

        Pane gameObjects = new Pane(racketA, racketB, ball);

        racketAScore = new Label("0");
        racketBScore = new Label("0");

        HBox scoresBox = new HBox(100, racketAScore, racketBScore);
        scoresBox.setPadding(new Insets(50, 0, 0, 0));
        scoresBox.setAlignment(Pos.TOP_CENTER);
        scoresBox.getStylesheets().add(getClass().getResource("/fontstyle.css").toExternalForm());

        gameRoot.getChildren().addAll(gameObjects, scoresBox);

    }

    public void animate() {
        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) { // ignore the first tick, just compute the first deltaT
                    last = now;
                    return;
                }
                court.update((now - last) * 1.0e-9); // convert nanoseconds to seconds
                last = now;
                racketA.setY(court.getRacketA().getRacketPos() * scale);
                racketB.setY(court.getRacketB().getRacketPos() * scale);
                ball.setCenterX(court.getBall().getBallX() * scale + xMargin);
                ball.setCenterY(court.getBall().getBallY() * scale);
                if (Integer.parseInt(racketAScore.getText()) != court.getScoreA() || Integer.parseInt(racketBScore.getText()) != court.getScoreB()) {
                    racketAScore.setText(String.valueOf(court.getScoreA()));
                    racketBScore.setText(String.valueOf(court.getScoreB()));
                }
            }
        }.start();
    }
}
