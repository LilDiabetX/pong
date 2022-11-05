package model;

import javafx.scene.paint.Color;

public abstract class PowerUp extends RandomEvent {
    private double radius;          // Rayon du pouvoir spécial
    private Color color;

    public PowerUp(Court court, double radius, Color color) {
        super(court);
        this.radius = radius;
        this.color = color;
    }

    /**
     * Applique l'effet du pouvoir spécial à la raquette correspondante.
     */
    public abstract void applyEffect();

    /**
     * Détermine s'il y a collision entre la balle et le pouvoir spécial et applique l'effet.
     * @param ball la balle dans le jeu courant
     */
    public void collide(Ball ball) {
        if (ball.getBallX() - radius <= getPosX() && ball.getBallX() + radius >= getPosX()
                && ball.getBallY() - radius <= getPosY() && ball.getBallY() + radius >= getPosY()) {
            applyEffect();
        }
    }

    public double getRadius() { return radius; }

    public Color getColor() { return color; }

    /**
     * Retourne la raquette qui a touché la balle en dernier.
     * @param ball la balle dans le jeu courant
     * @return une instance de Racket
     */
    public Racket getBallHitBy(Ball ball) { return ball.getHitBy(); }

    /**
     * Retourne la raquette qui va toucher la balle prochainement.
     * @param ball la balle dans le jeu courant
     * @return une instance de Racket
     */
    public Racket getBallNextReceivedBy(Ball ball) {
        return ball.getNextReceivedBy();
    }
}
