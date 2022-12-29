package model;
import java.time.chrono.ThaiBuddhistChronology;
import java.util.ArrayList;
import model.Countdown;

import gui.GameView;
import javafx.animation.AnimationTimer;

import model.Countdown;

public class Court {

    private Sound soundEffect = new Sound(); // permettra de jouer des effets sonores
    private Sound music = new Sound(); // permettra de jouer de la musique en continu

    // instance parameters
    private RacketController playerA, playerB;
    private final double width, height; // m
    // instance state
    private ArrayList<Ball> balls;
    private Racket racketA, racketB;

    private int scoreA, scoreB;
    
    private Countdown cd;

    private boolean isEnd = false;
 

    public Court(RacketController playerA, RacketController playerB, double width, double height){
        this.width = width;
        this.height = height;
        this.playerA=playerA;
        this.playerB=playerB;
        cd = new Countdown(3, 0);
        playMusic();
        reset();
      
        
    }

    public void playMusic(){
        this.music.setFile(3);
        this.music.loop();
    }

    public void playSFX(int i){
        this.soundEffect.setFile(i);
        this.soundEffect.play();
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
    
    public Countdown getCd(){
        return cd;
    }

     
    public boolean isEnd() {return this.isEnd;}


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
        if (nextBallY < 0 || nextBallY > height) {
            ball.setBallSpeedY(-ball.getBallSpeedY());
            nextBallY = ball.getBallY() + deltaT * ball.getBallSpeedY();
            playSFX(1);
        }
        if ((nextBallX < 0 && nextBallY > racketA.getRacketPos() && nextBallY < racketA.getRacketPos() + racketA.getRacketSize())) { //si on touche la raquette de gauche
            ball.setBallSpeedX(-ball.getBallSpeedX()+20); //on change le sens de la vitesse horizontale et on l'augmente de 20
            ball.setBallSpeedY(ball.getBallSpeedY()+20); //on ajoute 20 à la vitesse verticale
            nextBallX = ball.getBallX() + deltaT * ball.getBallSpeedX();
            ball.invertLastHitBy();
            playSFX(1);
        } else if ((nextBallX > width && nextBallY > racketB.getRacketPos() && nextBallY < racketB.getRacketPos() + racketB.getRacketSize())) { //si on touche la raquette de droite
            ball.setBallSpeedX(-ball.getBallSpeedX()-20); //on change le sens de la vitesse horizontale et on l'augmente de 20
            ball.setBallSpeedY(ball.getBallSpeedY()+20); //on ajoute 20 à la vitesse verticale
            nextBallX = ball.getBallX() + deltaT * ball.getBallSpeedX();
            ball.invertLastHitBy();
            playSFX(1);

        } else if (nextBallX < 0) {
            playerB.incrementScore();
            ball.setHasScored(true);
            playSFX(0);
            return true;
        } else if (nextBallX > width) {
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
        if (cd.isEnd()) {
            this.isEnd = true;
        } else {
            this.racketA = new Racket(playerA,this.height/2);
            this.racketB = new Racket(playerB,this.height/2);
            this.balls = new ArrayList<Ball>();
            this.balls.add(new Ball(this.width/2,this.height/2,275.0,275.0, racketA, racketB));
        }
    }


    void addBall(Ball ball) {
        balls.add(ball);
    }
}
