package model;


import gui.GameView;

public class Court {

    private Sound sound = new Sound();

    private final PowerUpManager powerUpManager = PowerUpManager.getInstance(this);

    // instance parameters
    private RacketController playerA, playerB;
    private final double width, height; // m
    // instance state
    private Ball ball;
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

    public Ball getBall(){
        return ball;
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

        if (powerUpManager.decrementCountdownBy(deltaT)) {
            if (GameView.isPowerUpVisible()) {
                GameView.hidePowerUp();
                powerUpManager.resetCountdown();
            } else {
                powerUpManager.createNewPowerUp();
                powerUpManager.resetVisibleCountdown();
            }
        }

        if (updateBall(deltaT)) {
            scoreA = playerA.getScore();
            scoreB = playerB.getScore();
            reset();
        }
    }


    /**
     * @return true if a player lost
     */
    private boolean updateBall(double deltaT) {
        // first, compute possible next position if nothing stands in the way.
        double nextBallX = ball.getBallX() + deltaT * ball.getBallSpeedX();
        double nextBallY = ball.getBallY() + deltaT * ball.getBallSpeedY();
        // next, see if the ball would meet some obstacle
        if (nextBallY < 0 || nextBallY > height) {
            ball.setBallSpeedY(-ball.getBallSpeedY());
            nextBallY = ball.getBallY() + deltaT * ball.getBallSpeedY();
            playSFX(1);
        }
        if ((nextBallX < 0 && nextBallY > racketA.getRacketPos() && nextBallY < racketA.getRacketPos() + racketA.getRacketSize())) {
            ball.setBallSpeedX(-ball.getBallSpeedX()+20);
            ball.setBallSpeedY(ball.getBallSpeedY()+20);
            nextBallX = ball.getBallX() + deltaT * ball.getBallSpeedX();
            ball.invertLastHitBy();
            playSFX(1);
        } else if ((nextBallX > width && nextBallY > racketB.getRacketPos() && nextBallY < racketB.getRacketPos() + racketB.getRacketSize())) {
            ball.setBallSpeedX(-ball.getBallSpeedX()-20);
            ball.setBallSpeedY(ball.getBallSpeedY()+20);
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

    void reset(){
        this.racketA = new Racket(playerA,this.height/2);
        this.racketB = new Racket(playerB,this.height/2);
        this.ball = new Ball(this.width/2,this.height/2,275.0,275.0, racketA, racketB);
    }
}
