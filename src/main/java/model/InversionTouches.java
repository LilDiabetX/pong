package model;

import java.util.Timer;
import java.util.TimerTask;

public class InversionTouches extends PowerUp{
	
	private Timer timer = new Timer();
	
	InversionTouches(Court court, double radius){
		super(court,radius);
	}

	public void applyEffect(){
		InversionTouches self = this;	
		for (int i = 0; i < this.getCourt().getBalls().size(); i++) {
			if(this.getCourt().getBalls().get(i).getHitBy() == this.getCourt().getBalls().get(i).getPlayerB()){
				this.getCourt().getPlayerA().setInverted(true);	
				timer.schedule( 
					new TimerTask() {
						@Override
						public void run() {
							self.getCourt().getPlayerA().setInverted(false);
							timer.cancel();
						}
					}, 15000 );
			}
			else{
				this.getCourt().getPlayerB().setInverted(true);
				timer.schedule( 
					new TimerTask() {
						@Override
						public void run() {
							self.getCourt().getPlayerB().setInverted(false);
							timer.cancel();
						}
					}, 15000 );
			}
		}
			
		
	}

}