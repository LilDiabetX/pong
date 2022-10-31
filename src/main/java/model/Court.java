package model;
import java.util.ArrayList;

public class Court {

    private Sound sound = new Sound();

    // instance parameters
    private RacketController playerA, playerB;
    private final double width, height; // m
    // instance state
    private ArrayList<Ball> balls;
    //private Ball newBall;
    private Racket racketA, racketB;

    private int scoreA, scoreB;

    public Court(RacketController playerA, RacketController playerB, double width, double height){
        this.width = width;
        this.height = height;
        this.playerA=playerA;
        this.playerB=playerB;
        this.racketA = new Racket(playerA,this.height/2);
        this.racketB = new Racket(playerB,this.height/2);
        this.balls = new ArrayList<Ball>();
        this.balls.add(new Ball(this.width/2,this.height/2,275.0,275.0, racketA, racketB));
        DoubleBall db = new DoubleBall(this, this.balls.get(0).getBallRadius(), this.balls.get(0));
        this.balls.add(db.getNewb());
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

    /*public Ball getBall(){
        return ball;
    }

    public Ball getNewBall() {
        return newBall;
    } */

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
            reset();
        }
    }

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
            playSFX(0);
            return true;
        } else if (nextBallX > width) {
            playerA.incrementScore();
            playSFX(0);
            return true;
        }
        ball.setBallX(nextBallX);
        ball.setBallY(nextBallY);
        return false;
    }



    /**
     * @return true if a player lost a point
     */
    /*private boolean updateBall(double deltaT) {
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
            playSFX(0);
            return true;
        } else if (nextBallX > width) {
            playerA.incrementScore();
            playSFX(0);
            return true;
        }
        ball.setBallX(nextBallX);
        ball.setBallY(nextBallY);
        return false;
    }*/

    /**
     * @return true if a player lost a point
     */
    /*private boolean updateNewBall(double deltaT) {
        // first, compute possible next position if nothing stands in the way.
        double nextBallX = newBall.getBallX() + deltaT * newBall.getBallSpeedX();
        double nextBallY = newBall.getBallY() + deltaT * newBall.getBallSpeedY();
        // next, see if the ball would meet some obstacle
        if (nextBallY < 0 || nextBallY > height) {
            newBall.setBallSpeedY(-newBall.getBallSpeedY());
            nextBallY = newBall.getBallY() + deltaT * newBall.getBallSpeedY();
            playSFX(1);
        }
        if ((nextBallX < 0 && nextBallY > racketA.getRacketPos() && nextBallY < racketA.getRacketPos() + racketA.getRacketSize())) { //si on touche la raquette de gauche
            newBall.setBallSpeedX(-newBall.getBallSpeedX()+20); //on change le sens de la vitesse horizontale et on l'augmente de 20
            newBall.setBallSpeedY(newBall.getBallSpeedY()+20); //on ajoute 20 à la vitesse verticale
            nextBallX = newBall.getBallX() + deltaT * newBall.getBallSpeedX();
            newBall.invertLastHitBy();
            playSFX(1);
        } else if ((nextBallX > width && nextBallY > racketB.getRacketPos() && nextBallY < racketB.getRacketPos() + racketB.getRacketSize())) { //si on touche la raquette de droite
            newBall.setBallSpeedX(-newBall.getBallSpeedX()-20); //on change le sens de la vitesse horizontale et on l'augmente de 20
            newBall.setBallSpeedY(newBall.getBallSpeedY()+20); //on ajoute 20 à la vitesse verticale
            nextBallX = newBall.getBallX() + deltaT * newBall.getBallSpeedX();
            newBall.invertLastHitBy();
            playSFX(1);

        } else if (nextBallX < 0) {
            playerB.incrementScore();
            playSFX(0);
            return true;
        } else if (nextBallX > width) {
            playerA.incrementScore();
            playSFX(0);
            return true;
        }
        newBall.setBallX(nextBallX);
        newBall.setBallY(nextBallY);
        return false;
    }*/

    void reset(){
        this.racketA = new Racket(playerA,this.height/2);
        this.racketB = new Racket(playerB,this.height/2);
        this.balls = new ArrayList<Ball>();
        this.balls.add(new Ball(this.width/2,this.height/2,275.0,275.0, racketA, racketB));
        DoubleBall db = new DoubleBall(this, this.balls.get(0).getBallRadius(), this.balls.get(0));
        this.balls.add(db.getNewb());
        /*if (Ball.getNbBalls() > 1) {
            this.balls.remove(this.balls.size()-1);
        }*/
    }
}
