package model;

public class Ball  {
	
	private final double ballRadius = 10.0; // m
	private double ballX, ballY; // m 
	private double ballSpeedX, ballSpeedY; // m

	public Ball(double ballX, double ballY, double ballSpeedX, double ballSpeedY){
		this.ballX=ballX;
		this.ballY=ballY;
		this.ballSpeedX=ballSpeedX;
		this.ballSpeedY=ballSpeedY;
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

    public void setBallX(double ballX) {
        this.ballX=ballX;
    }

    public void setBallY(double ballY) {
        this.ballY=ballY;
    }

    public void setBallSpeedX(double ballSpeedX) {
        this.ballSpeedX=ballSpeedX;
    }

    public void setBallSpeedY(double ballSpeedY) {
        this.ballSpeedY=ballSpeedY;
    }
    
	
}
