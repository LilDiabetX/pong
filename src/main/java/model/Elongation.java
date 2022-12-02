package model;


import java.util.Timer;
import java.util.TimerTask;

public class Elongation extends PowerUp {
	
	private Timer timer = new Timer();

	public Elongation(Court court, double radius) {
		super(court, radius);
	}

	@Override
	public void applyEffect() {
		Elongation self = this;
		
		//si le playerB a le powerup
		if(this.getCourt().getBall().getHitBy() == this.getCourt().getBall().getPlayerB()) {
			this.getCourt().getRacketB().ralonger();
			timer.schedule( 
	        		new TimerTask() {
	            		@Override
	            		public void run() {
	            			self.getCourt().getRacketB().racourcir();
	                		timer.cancel();
	            		}
	        		}, 15000 );
		}else {
			this.getCourt().getRacketA().ralonger();
			timer.schedule( 
	        		new TimerTask() {
	            		@Override
	            		public void run() {
	                		self.getCourt().getRacketB().racourcir();
	                		timer.cancel();
	            		}
	        		}, 15000 );
			
		}
		
		
	}
     

}
