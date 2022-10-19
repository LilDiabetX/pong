package model;

public class Court {

    private Sound sound = new Sound();

    // instance parameters
    private RacketController playerA, playerB;
    private final double width, height; // m
    // instance state
    private double racketA; // m
    private double racketB; // m
    private double ballX, ballY; // m
    private double ballSpeedX, ballSpeedY; // m

    private int scoreA, scoreB;

    public Court(RacketController playerA, RacketController playerB, double width, double height) {
        this.playerA = playerA;
        this.playerB = playerB;
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

    public Raquette getRacketA() {
        return raquetteA;
    }

    public Raquette getRacketB() {
        return raquetteB;
    }

    public Ball getBall(){
        return ball;
    }

    public int getScoreA() { return scoreA; }

    public int getScoreB() { return scoreB; }

    public int getScoreA() { return scoreA; }

    public int getScoreB() { return scoreB; }

    public void update(double deltaT) {

        switch (raquetteA.getPlayer().getState()) {
            case GOING_UP:
                raquetteA.setRacketPos(raquetteA.getRacketPos()-raquetteA.getRacketSpeed() * deltaT);
                if (raquetteA.getRacketPos() < 0.0) raquetteA.setRacketPos(0.0);
                break;
            case IDLE:
                break;
            case GOING_DOWN:
                raquetteA.setRacketPos(raquetteA.getRacketPos()+raquetteA.getRacketSpeed() * deltaT);
                if (raquetteA.getRacketPos() + raquetteA.getRacketSize() > height) raquetteA.setRacketPos(height - raquetteA.getRacketSize());
                break;
        }
        switch (raquetteB.getPlayer().getState()) {
            case GOING_UP:
                raquetteB.setRacketPos(raquetteB.getRacketPos()-raquetteB.getRacketSpeed() * deltaT);
                if (raquetteB.getRacketPos() < 0.0) raquetteB.setRacketPos(0.0);
                break;
            case IDLE:
                break;
            case GOING_DOWN:
                raquetteB.setRacketPos(raquetteB.getRacketPos()+raquetteB.getRacketSpeed() * deltaT);
                if (raquetteB.getRacketPos() + raquetteB.getRacketSize() > height) raquetteB.setRacketPos(height - raquetteB.getRacketSize());
                break;
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
        // first, compute possible next position if nothing stands in the way
        double nextBallX = ball.getBallX() + deltaT * ball.getBallSpeedX();
        double nextBallY = ball.getBallY() + deltaT * ball.getBallSpeedY();
        // next, see if the ball would meet some obstacle
        if (nextBallY < 0 || nextBallY > height) {
            ballSpeedY = -ballSpeedY;
            nextBallY = ballY + deltaT * ballSpeedY;
            playSFX(1);
        }
        if ((nextBallX < 0 && nextBallY > racketA && nextBallY < racketA + racketSize)) {
            ballSpeedX = -ballSpeedX + 20;
            ballSpeedY = ballSpeedY + 20;
            nextBallX = ballX + deltaT * ballSpeedX;
            playSFX(1);
        } else if ((nextBallX > width && nextBallY > racketB && nextBallY < racketB + racketSize)) {
            ballSpeedX = -ballSpeedX - 20;
            ballSpeedY = ballSpeedY + 20;
            nextBallX = ballX + deltaT * ballSpeedX;
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

    public double getBallRadius() {
        return ballRadius;
    }

    void reset() {
        this.racketA = height / 2;
        this.racketB = height / 2;
        this.ballSpeedX = 275.0;
        this.ballSpeedY = 275.0;
        this.ballX = width / 2;
        this.ballY = height / 2;
    }
  
 
}
