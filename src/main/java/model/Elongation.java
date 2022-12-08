package model;

import java.util.Timer;
import java.util.TimerTask;

public class Elongation extends PowerUp {
	
private Timer timer = new Timer();
	
	Elongation(Court court, double radius){
		super(court,radius);
	}

	public void applyEffect(){
		Elongation self = this;	
		for (int i = 0; i < this.getCourt().getBalls().size(); i++) {
			if(this.getCourt().getBalls().get(i).getHitBy() == this.getCourt().getBalls().get(i).getPlayerB()){
					this.getCourt().getRacketB().ralonger();
					timer.schedule( 
			        		new TimerTask() {
			            		@Override
			            		public void run() {
			            			self.getCourt().getRacketB().racourcir();
			                		timer.cancel();
			            		}
			        		}, 15000 );
				}else{
					this.getCourt().getRacketA().ralonger();
					timer.schedule( 
					new TimerTask() {
						@Override
						public void run() {
							self.getCourt().getRacketA().racourcir();
							timer.cancel();
						}
					}, 15000 );
			}
		}
			
		
	}
	

}
