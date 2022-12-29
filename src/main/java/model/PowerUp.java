package model;

public abstract class PowerUp extends RandomEvent {
    private double radius;
    private Sound sound;

    public PowerUp(Court court, double radius) {
        super(court);
        this.radius = radius;
    }
    public abstract void applyEffect();

    public void playSFX(){
        this.sound.setFile(2);
        this.sound.play();
    }

    public void collide(Ball ball) {
        if (ball.getBallX() - radius <= getPosX() && ball.getBallX() + radius >= getPosX()
                && ball.getBallY() - radius <= getPosY() && ball.getBallY() + radius >= getPosY()) {    // if power up inside ball's "zone" then apply effect
            playSFX();
            applyEffect();
        }
    }

    public Racket getBallHitBy(Ball ball) { return ball.getHitBy(); }

    public Racket getBallNextReceivedBy(Ball ball) {            // opponent next to be hit
        return ball.getNextReceivedBy();
    }
}
