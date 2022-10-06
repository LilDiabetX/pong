package model;

public class Court {

    private Sound sound = new Sound();

    // instance parameters
    private final RacketController playerA, playerB;
    private final double width, height; // m
    private final double racketSpeed = 300.0; // m/s
    private final double racketSize = 100.0; // m
    private final double ballRadius = 10.0; // m
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

    public double getRacketSize() {
        return racketSize;
    }

    public double getRacketA() {
        return racketA;
    }

    public double getRacketB() {
        return racketB;
    }

    public double getBallX() {
        return ballX;
    }

    public double getBallY() {
        return ballY;
    }

    public int getScoreA() { return scoreA; }

    public int getScoreB() { return scoreB; }

    public void update(double deltaT) {

        switch (playerA.getState()) {
            case GOING_UP:
                racketA -= racketSpeed * deltaT;
                if (racketA < 0.0) racketA = 0.0;
                break;
            case IDLE:
                break;
            case GOING_DOWN:
                racketA += racketSpeed * deltaT;
                if (racketA + racketSize > height) racketA = height - racketSize;
                break;
        }
        switch (playerB.getState()) {
            case GOING_UP:
                racketB -= racketSpeed * deltaT;
                if (racketB < 0.0) racketB = 0.0;
                break;
            case IDLE:
                break;
            case GOING_DOWN:
                racketB += racketSpeed * deltaT;
                if (racketB + racketSize > height) racketB = height - racketSize;
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
        double nextBallX = ballX + deltaT * ballSpeedX;
        double nextBallY = ballY + deltaT * ballSpeedY;
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
        ballX = nextBallX;
        ballY = nextBallY;
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
