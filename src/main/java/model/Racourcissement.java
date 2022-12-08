package model;

import java.util.Timer;
import java.util.TimerTask;


public class Racourcissement extends PowerUp{
	
	private Timer timer = new Timer();

	public Racourcissement(Court court, double radius) {
		super(court, radius);
	}
	
	public void applyEffect(){
		Racourcissement self = this;	
		for (int i = 0; i < this.getCourt().getBalls().size(); i++) {
			if(this.getCourt().getBalls().get(i).getHitBy() == this.getCourt().getBalls().get(i).getPlayerB()){
					this.getCourt().getRacketB().racourcir();
					timer.schedule( 
			        		new TimerTask() {
			            		@Override
			            		public void run() {
			            			self.getCourt().getRacketB().ralonger();
			                		timer.cancel();
			            		}
			        		}, 15000 );
				}else{
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
			

}
