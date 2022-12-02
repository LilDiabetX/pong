package model;

public class Racket {
    private static int counter;

    private final int playerID;
	private RacketController player;
	private double racketSpeed = 300.0; // m/s
    private double racketSize = 100.0; // m
    private double racketPos; //m

    public Racket(RacketController player, double racketPos){
    	counter++;
        this.player=player;
        this.playerID = counter;
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

    public String toString() {
        return String.valueOf(playerID);
    }
    
    // augmente la taille de la raquette
    public void  ralonger(){
  	   this.setRacketSize(this.getRacketSize()+15);  
  	}
     
    //diminue la taille de la raqiette
      public void  racourcir(){
      	this.setRacketSize(this.getRacketSize()-15);
   	}
}