package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Court;
import model.RacketController;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        var root = new StackPane();
        var gameScene = new Scene(root);
        class Player implements RacketController {
            State state = State.IDLE;
            private int score = 0;
            private boolean inverted = false;
            @Override
            public State getState() { return state; }
            @Override
            public int getScore() { return score; }
            @Override
            public void incrementScore() { score++; }
            @Override
            public void doubleIncrementScore() { score +=2; }
            @Override
            public void setInverted(boolean b){ inverted = b; }
            @Override
            public boolean getInverted() { return inverted; }
        }
        var playerA = new Player();
        var playerB = new Player();
        gameScene.setOnKeyPressed(ev -> {
            switch (ev.getCode()) {
                case Z :
                    if(!playerA.getInverted()){
                        playerA.state = RacketController.State.GOING_UP;
                        break;
                    }
                    else{
                        playerA.state = RacketController.State.GOING_DOWN;
                        break;
                    }
                case W:
                    if(!playerA.getInverted()){
                        playerA.state = RacketController.State.GOING_UP;
                        break;
                    }
                    else{
                        playerA.state = RacketController.State.GOING_DOWN;
                        break;
                    }
                case S:
                    if(!playerA.getInverted()){
                        playerA.state = RacketController.State.GOING_DOWN;
                        break;
                    }
                    else{
                        playerA.state = RacketController.State.GOING_UP;
                        break;
                    }
                case UP:
                    if(!playerB.getInverted()){
                        playerB.state = RacketController.State.GOING_UP;
                        break;
                    }
                    else{
                        playerB.state = RacketController.State.GOING_DOWN;
                        break;
                    }
                case DOWN:
                    if(!playerB.getInverted()){
                        playerB.state = RacketController.State.GOING_DOWN;
                        break;
                    }
                    else{
                        playerB.state = RacketController.State.GOING_UP;
                        break;
                    }
            }
        });
        gameScene.setOnKeyReleased(ev -> {
            switch (ev.getCode()) {
                case Z :
                    if (playerA.state == RacketController.State.GOING_UP||playerA.state == RacketController.State.GOING_DOWN) playerA.state = RacketController.State.IDLE;
                    break;
                case W:
                    if (playerA.state == RacketController.State.GOING_UP||playerA.state == RacketController.State.GOING_DOWN) playerA.state = RacketController.State.IDLE;
                    break;
                case S:
                    if (playerA.state == RacketController.State.GOING_DOWN||playerA.state == RacketController.State.GOING_UP) playerA.state = RacketController.State.IDLE;
                    break;
                case UP:
                    if (playerB.state == RacketController.State.GOING_UP||playerB.state == RacketController.State.GOING_DOWN) playerB.state = RacketController.State.IDLE;
                    break;
                case DOWN:
                    if (playerB.state == RacketController.State.GOING_DOWN||playerB.state == RacketController.State.GOING_UP) playerB.state = RacketController.State.IDLE;
                    break;
            }
        });
        var court = new Court(playerA, playerB, 1000, 600);
        var gameView = new GameView(court, root, 1.0);
        primaryStage.setScene(gameScene);
        primaryStage.show();
        gameView.animate();
    }


}
