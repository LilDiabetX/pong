package gui;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import model.Ball;
import model.Court;

public class GameView {
    // class parameters
    private final Court court;
    private final StackPane gameRoot; // main node of the game
    private final Pane gameObjects;
    private final double scale;
    private final double xMargin = 50.0, racketThickness = 10.0; // pixels

    // children of the game main node
    private final Rectangle racketA1, racketA2, racketB1, racketB2;

    private final Label racketAScore, racketBScore;
    private ArrayList<Circle> balls;

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

        racketA1 = new Rectangle();
        racketA2 = new Rectangle();//à retirer
        racketA1.setHeight(court.getRacketA().getRacketSize() * scale / 2);
        racketA2.setHeight(court.getRacketA().getRacketSize() * scale / 2);//à retirer
        racketA1.setWidth(racketThickness);
        racketA2.setWidth(racketThickness);//à retirer
        racketA1.setFill(Color.RED); //noir
        racketA2.setFill(Color.GREEN); //à retirer

        racketA1.setX(xMargin - racketThickness);
        racketA2.setX(xMargin - racketThickness);//à retirer
        racketA1.setY(court.getRacketA().getRacketPos() * scale);
        racketA2.setY((court.getRacketA().getRacketPos() + court.getRacketA().getRacketSize()/2) * scale);//à retirer


        racketB1 = new Rectangle();
        racketB2 = new Rectangle();//à retirer
        racketB1.setHeight(court.getRacketB().getRacketSize() * scale);
        racketB2.setHeight(court.getRacketB().getRacketSize() * scale / 2);//à retirer
        racketB1.setWidth(racketThickness);
        racketB2.setWidth(racketThickness);//à retirer
        racketB1.setFill(Color.RED);
        racketB2.setFill(Color.GREEN); //à retirer

        racketB1.setX(court.getWidth() * scale + xMargin);
        racketB2.setX(court.getWidth() * scale + xMargin);//à retirer
        racketB1.setY(court.getRacketB().getRacketPos() * scale);
        racketB2.setY(court.getRacketB().getRacketPos() * scale / 2);// à retirer

        balls = new ArrayList<Circle>();
        for (int i = 0; i < court.getBalls().size(); i++) {
            balls.add(new Circle());
            balls.get(i).setRadius(court.getBalls().get(i).getBallRadius());
            balls.get(i).setFill(Color.BLACK);

            balls.get(i).setCenterX(court.getBalls().get(i).getBallX() * scale + xMargin);
            balls.get(i).setCenterY(court.getBalls().get(i).getBallY() * scale);
        }
        
        gameObjects = new Pane(racketA1, racketA2, racketB1, racketB2, balls.get(0));

        racketAScore = new Label("0");
        racketBScore = new Label("0");

        HBox scoresBox = new HBox(100, racketAScore, racketBScore);
        scoresBox.setPadding(new Insets(50, 0, 0, 0));
        scoresBox.setAlignment(Pos.TOP_CENTER);
        scoresBox.getStylesheets().add(getClass().getResource("/fontstyle.css").toExternalForm());

        gameRoot.getChildren().addAll(gameObjects, scoresBox);

    }

    public ArrayList<Circle> getBalls() {
        return this.balls;
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
                racketA1.setY(court.getRacketA().getRacketPos() * scale);
                racketA2.setY((court.getRacketA().getRacketPos() + court.getRacketA().getRacketSize()/2) * scale);//à retirer
                racketB1.setY(court.getRacketB().getRacketPos() * scale);
                racketB2.setY((court.getRacketB().getRacketPos() + court.getRacketB().getRacketSize()/2) * scale);//à retirer
                while (court.getBalls().size() < balls.size()) {
                    gameObjects.getChildren().remove(balls.get(balls.size()-1));
                    balls.remove(balls.size()-1);
                }
                while (court.getBalls().size() > balls.size()) {
                    balls.add(new Circle());
                    balls.get(balls.size()-1).setRadius(court.getBalls().get(balls.size()-1).getBallRadius());
                    balls.get(balls.size()-1).setFill(Color.BLACK);

                    balls.get(balls.size()-1).setCenterX(court.getBalls().get(balls.size()-1).getBallX() * scale + xMargin);
                    balls.get(balls.size()-1).setCenterY(court.getBalls().get(balls.size()-1).getBallY() * scale);
                    gameObjects.getChildren().add(balls.get(balls.size()-1));
                }
                for (int i = 0; i < balls.size(); i++) {
                    balls.get(i).setCenterX(court.getBalls().get(i).getBallX() * scale + xMargin);
                    balls.get(i).setCenterY(court.getBalls().get(i).getBallY() * scale);
                }
                if (Integer.parseInt(racketAScore.getText()) != court.getScoreA() || Integer.parseInt(racketBScore.getText()) != court.getScoreB()) {
                    racketAScore.setText(String.valueOf(court.getScoreA()));
                    racketBScore.setText(String.valueOf(court.getScoreB()));
                }
            }
        }.start();
    }
}
