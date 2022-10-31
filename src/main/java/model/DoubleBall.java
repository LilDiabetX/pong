package model;

public class DoubleBall extends PowerUp {
    private Ball newb;

    public DoubleBall(Court court, double radius, Ball b) {
        super(court, radius);
        this.newb = new Ball(getPosX(), getPosY(), -b.getBallSpeedX(), -b.getBallSpeedY(), b.getHitBy(), b.getNextReceivedBy());
    }

    
    

    public void applyEffect() {

    }


    

    public Ball getNewb() {
        return newb;
    }

}
