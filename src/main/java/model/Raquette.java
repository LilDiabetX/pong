package model;

public class Raquette {

	private RacketController player;
	private double racketSpeed = 300.0; // m/s
    private double racketSize = 100.0; // m
    private double racketPos; //m

    public Raquette(RacketController player,double racketPos){
    	this.player=player;
    	this.racketPos=racketPos;
    }

    public double getRacketSize() {
        return racketSize;
    }

    public double getRacketSpeed() {
        return racketSpeed;
    }

    public double getRacketPos() {
        return racketPos;
    }

    public RacketController getPlayer() {
        return player;
    }

    public void setRacketSize(double racketSize) {
        this.racketSize=racketSize;
    }

    public void setRacketSpeed(double racketSpeed) {
        this.racketSpeed=racketSpeed;
    }

    public void setRacketPos(double racketPos) {
        this.racketPos=racketPos;
    }

    public void setPlayer(RacketController player) {
        this.player=player;
    }
	
}
