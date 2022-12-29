package model;
import java.util.*;

public class DoubleScore extends PowerUp{
    
    private Timer timer = new Timer();

    public DoubleScore(Court court, double radius) {
        super(court, radius);
    }

    @Override
    public void applyEffect() {
        court.setDoubleScore(true);
        timer.schedule( 
			new TimerTask() {
				@Override
				public void run() {
                    court.setDoubleScore(false);
				}	
            }, 30000 );   
        

    }

    
    
}