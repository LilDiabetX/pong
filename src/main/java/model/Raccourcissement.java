package model;


import java.util.Timer;
import java.util.TimerTask;

public class Raccourcissement extends PowerUp{
	
	private Timer timer = new Timer();

	public Raccourcissement(Court court, double radius) {
		super(court, radius);
	}

	@Override
	public void applyEffect() {
		
		Raccourcissement self = this;
		if(this.getCourt().getBall().getHitBy() == this.getCourt().getBall().getPlayerB()) {
			this.getCourt().getRacketB().racourcir();
			timer.schedule( 
	        		new TimerTask() {
	            		@Override
	            		public void run() {
	            			self.getCourt().getRacketB().ralonger();
	                		timer.cancel();
	            		}
	        		}, 15000 );
		}else {
			this.getCourt().getRacketA().racourcir();
			timer.schedule( 
	        		new TimerTask() {
	            		@Override
	            		public void run() {
	                		self.getCourt().getRacketA().ralonger();
	                		timer.cancel();
	            		}
	        		}, 15000 );
			
		}
		
		
		
	}

}

