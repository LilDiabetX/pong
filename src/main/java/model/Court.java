package model;
import java.time.chrono.ThaiBuddhistChronology;
import java.util.ArrayList;

import gui.GameView;
import javafx.animation.AnimationTimer;

public class Court {

    private Sound sound = new Sound();

    // instance parameters
    private RacketController playerA, playerB;
    private final double width, height; // m
    // instance state
    private ArrayList<Ball> balls;
    private Racket racketA, racketB;

    private int scoreA, scoreB;
 

    public Court(RacketController playerA, RacketController playerB, double width, double height){
        this.width = width;
        this.height = height;
        this.playerA=playerA;
        this.playerB=playerB;
        reset();
    }


    public void playSFX(int i){
        this.sound.setFile(i);
        this.sound.play();
    }
   
    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Racket getRacketA() {
        return racketA;
    }

    public Racket getRacketB() {
        return racketB;
    }

    public ArrayList<Ball> getBalls() {
        return this.balls;
    }

    public RacketController getPlayerA(){
        return playerA;
    }

    public RacketController getPlayerB(){
        return playerB;
    }

    public int getScoreA() { return scoreA; }

    public int getScoreB() { return scoreB; }

    public void update(double deltaT) {

        Racket[] racketTab = {racketA, racketB};
        for (Racket racket : racketTab) {
            switch (racket.getPlayer().getState()) {
                case GOING_UP:
                    racket.setRacketPos(racket.getRacketPos()- racket.getRacketSpeed() * deltaT);
                    if (racket.getRacketPos() < 0.0) racket.setRacketPos(0.0);
                    break;
                case IDLE:
                    break;
                case GOING_DOWN:
                    racket.setRacketPos(racket.getRacketPos()+ racket.getRacketSpeed() * deltaT);
                    if (racket.getRacketPos() + racket.getRacketSize() > height) racket.setRacketPos(height - racket.getRacketSize());
                    break;
            }
        }

        if (updateBalls(deltaT)) {
            scoreA = playerA.getScore();
            scoreB = playerB.getScore();
            if(balls.size() > 1) {
                for (Ball b : balls) {
                    if (b.getHasScored()) {
                        b.setHasScored(false);
                        balls.remove(b);
                    }
                }
            } else {
                reset();
            }
        }
    }

    /**
     * @return true if a player lost a point
     */
    private boolean updateBalls(double deltaT) {
        for (Ball ball : balls) {
            if(updateBall(ball, deltaT)) return true;
        }
        return false;
    }


    private boolean updateBall(Ball ball, double deltaT) {
        // first, compute possible next position if nothing stands in the way.
        double nextBallX = ball.getBallX() + deltaT * ball.getBallSpeedX();
        double nextBallY = ball.getBallY() + deltaT * ball.getBallSpeedY();
        // next, see if the ball would meet some obstacle
        if (nextBallY < 0 || nextBallY > height) { // si on touche le mur du haut ou du bas
            ball.setBallSpeedY(-ball.getBallSpeedY());
            nextBallY = ball.getBallY() + deltaT * ball.getBallSpeedY();
            playSFX(1);
        }
        //WIP
        if ((nextBallX < 0 && nextBallY > racketA.getRacketPos() && nextBallY < racketA.getRacketPos() + racketA.getRacketSize())) { //si on touche la raquette de gauche...
            if (nextBallY > racketA.getRacketPos() + racketA.getRacketSize()/2) { //... sur la moitié du bas
                ball.setBallSpeedY(Math.abs(ball.getBallSpeedY()+20)); //on ajoute 20 à la vitesse verticale et on va vers le bas
                System.out.println("ça a touché le bas");
            } else if (nextBallY < racketA.getRacketPos() + racketA.getRacketSize()/2) { //... sur la moitié du haut
                ball.setBallSpeedY(-Math.abs(ball.getBallSpeedY()+20)); //on ajoute 20 à la vitesse verticale et on va vers le haut
                System.out.println("ça a touché le haut");
            } else { // ... sur le milieu
                ball.setBallSpeedY(ball.getBallSpeedY()+20); //on ajoute 20 à la vitesse verticale
                System.out.println("ça touche le milieu");
            }
            ball.setBallSpeedX(-ball.getBallSpeedX()+20); //on change le sens de la vitesse horizontale et on l'augmente de 20
            nextBallX = ball.getBallX() + deltaT * ball.getBallSpeedX();
            ball.invertLastHitBy();
            playSFX(1);
        } else if ((nextBallX > width && nextBallY > racketB.getRacketPos() && nextBallY < racketB.getRacketPos() + racketB.getRacketSize())) { //si on touche la raquette de droite...
            if (nextBallY > racketB.getRacketPos() + racketB.getRacketSize()/2) { //... sur la moitié du bas
                ball.setBallSpeedY(Math.abs(ball.getBallSpeedY()+20)); //on ajoute 20 à la vitesse verticale et on va vers le bas
                System.out.println("ça a touché le bas");
            } else if (nextBallY < racketB.getRacketPos() + racketB.getRacketSize()/2) { //... sur la moitié du haut
                ball.setBallSpeedY(-Math.abs(ball.getBallSpeedY()+20)); //on ajoute 20 à la vitesse verticale et on va vers le haut
                System.out.println("ça a touché le haut");
            } else { // ... sur le milieu
                ball.setBallSpeedY(ball.getBallSpeedY()+20); //on ajoute 20 à la vitesse verticale
                System.out.println("ça touche le milieu");
            }
            ball.setBallSpeedX(-ball.getBallSpeedX()-20); //on change le sens de la vitesse horizontale et on l'augmente de 20
            nextBallX = ball.getBallX() + deltaT * ball.getBallSpeedX();
            ball.invertLastHitBy();
            playSFX(1);
        } else if (nextBallX < 0) { //si le joueur de gauche rate
            playerB.incrementScore();
            ball.setHasScored(true);
            playSFX(0);
            return true;
        } else if (nextBallX > width) {//si le joueur de droite rate
            playerA.incrementScore();
            ball.setHasScored(true);
            playSFX(0);
            return true;
        }
        ball.setBallX(nextBallX);
        ball.setBallY(nextBallY);
        return false;
    }


    void reset(){
        this.racketA = new Racket(playerA,this.height/2);
        this.racketB = new Racket(playerB,this.height/2);
        this.balls = new ArrayList<Ball>();
        this.balls.add(new Ball(this.width/2,this.height/2,275.0,275.0, racketA, racketB));
    }


    void addBall(Ball ball) {
        balls.add(ball);
    }
}
