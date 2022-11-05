package model;

import gui.GameView;

/** SINGLETON
 * -------------------
 * Patron de conception de création qui s’assure de l’existence d’un seul
 * objet de son genre et fournit un unique point d’accès vers cet objet.
 */

// La classe PowerUpManager suit le patron du singleton
public final class PowerUpManager {
    private static PowerUpManager manager;
    private final Court court;
    private final double INITIAL_COUNTDOWN = 5;
    private double countdown;

    private PowerUpManager(Court court) {
        // évite les soucis s'il y a du multi-threading
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.court = court;
    }

    /**
     * Retourne l’unique instance de la classe PowerUpManager
     * @return Unique instance de PowerUpManager
     */
    public static PowerUpManager getInstance(Court court) {
        if (manager == null) {
            manager = new PowerUpManager(court);
        }
        return manager;
    }

    /**
     * Calcule la nouvelle valeur du compte à rebours et retourne un booléen selon cette valeur.
     * @param deltaT La variation du temps qu’il faut enlever au compte à rebours
     * @return vrai si le compte à rebours est inférieur ou égal à zéro et faux sinon.
     */
    public boolean decrementCountdownBy(double deltaT) {
        countdown -= deltaT;
        return countdown <= 0;
    }

    /**
     * Réinitialise la valeur du compte à rebours à la valeur initiale.
     */
    public void resetCountdown() {
        countdown = INITIAL_COUNTDOWN;
    }

    public void resetVisibleCountdown() {
        countdown = INITIAL_COUNTDOWN - 3;
    }

    public PowerUp createNewPowerUp() {
        InversionTouches it = new InversionTouches(court, 50);
        GameView.setPowerUp(it);
        return it;
    }
}
