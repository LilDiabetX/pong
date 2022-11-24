package model;

public class DoubleBall extends PowerUp {
    private Ball newb;

    public DoubleBall(Court court, double radius) {
        super(court, radius);
        this.newb = new Ball(getPosX(), getPosY(), -court.getBalls().get(0).getBallSpeedX(), -court.getBalls().get(0).getBallSpeedY(), court.getRacketA(), court.getRacketB());
    }

    public void applyEffect() {
        court.addBall(newb);
    }

    public Ball getNewb() {
        return newb;
    }

}
