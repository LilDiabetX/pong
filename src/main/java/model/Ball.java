package model;

public class Ball{
	private final double ballRadius = 10.0; // m
	private double ballX, ballY; // m 
	private double ballSpeedX, ballSpeedY; // m

    private Racket playerA, playerB;

    private boolean isLastHitPlayerB;       // false at the beginning (must be changed)

    private static int nbBalls = 0;

    private boolean hasScored = false;

	public Ball(double ballX, double ballY, double ballSpeedX, double ballSpeedY, Racket playerA, Racket playerB){
		this.ballX = ballX;
		this.ballY = ballY;
		this.ballSpeedX = ballSpeedX;
		this.ballSpeedY = ballSpeedY;
        this.playerA = playerA;
        this.playerB = playerB;
        this.nbBalls ++;
	}

	public double getBallRadius() {
        return ballRadius;
    }

    public double getBallX() {
        return ballX;
    }

    public double getBallY() {
        return ballY;
    }

    public double getBallSpeedX() {
        return ballSpeedX;
    }

    public double getBallSpeedY() {
        return ballSpeedY;
    }

    public Racket getHitBy() {
        if (isLastHitPlayerB) {
            return playerB;
        } else {
            return playerA;
        }
    }

    public Racket getNextReceivedBy() {
        if (isLastHitPlayerB) {
            return playerA;
        } else {
            return playerB;
        }
    }

    public void setBallX(double ballX) {
        this.ballX = ballX;
    }

    public void setBallY(double ballY) {
        this.ballY = ballY;
    }

    public void setBallSpeedX(double ballSpeedX) {
        this.ballSpeedX = ballSpeedX;
    }

    public void setBallSpeedY(double ballSpeedY) {
        this.ballSpeedY = ballSpeedY;
    }

    public void invertLastHitBy() {
        isLastHitPlayerB = !isLastHitPlayerB;
    }

    public Racket getPlayerA() {
        return playerA;
    }

    public Racket getPlayerB() {
        return playerB;
    }

    public boolean isLastHitPlayerB() {
        return isLastHitPlayerB;
    }

    public static int getNbBalls() {
        return nbBalls;
    }

    public static void decrNbBalls() {
        nbBalls--;
    }

    public boolean getHasScored() {
        return hasScored;
    }

    public void setHasScored(boolean b) {
        hasScored = b;
    }

    

}