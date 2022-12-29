package model;
import java.util.Timer;
import java.util.TimerTask;

public class DoubleScore extends PowerUp{
    
    private Timer timer = new Timer();

    public DoubleScore(Court court, double radius) {
        super(court, radius);
    }

    @Override
    public void applyEffect() {
        playSFX();
        court.setDoubleScore(true);
        timer.schedule( 
			new TimerTask() {
				@Override
				public void run() {
                    court.setDoubleScore(false);
                    playSFX();
				}	
            }, 15000 );   
        

    }   
}