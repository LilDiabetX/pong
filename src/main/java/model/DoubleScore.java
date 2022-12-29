package model;

public class DoubleScore extends PowerUp{
    boolean isActivated;

    public DoubleScore(Court court, double radius) {
        super(court, radius);
        isActivated = false;
    }

    @Override
    public void applyEffect() {
        isActivated = !isActivated;   
    }

    public boolean getIsActivated(){
        return isActivated;
    }

    
}