package model;
	import java.awt.Font;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.text.DecimalFormat;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import java.util.Timer;
	import java.util.TimerTask;
		
		
	public class Countdown {

			//attribut

		    private Timer timer;
		    private Court c;
		    
		    private int second,minute;
		    private String ddSecond,ddMinute;
		    
		    private DecimalFormat dFormat =new DecimalFormat("00");
			private boolean isEnd=false;


			// constructeurs
		    
			public Countdown(int minute, int seconde) {
				this.second=seconde;
				this.minute=minute;
				countDownTimer();
			}


			//getter 
			public int getMinute(){
			return this.minute;
			}	

			public int getSecond(){
			return this.second;
			}

			public String getDdSecond(){
	        return this.ddSecond;
	    	}

	    	public String getDdMinute(){
	        return this.ddMinute;
	   		}

			public boolean isEnd(){
			return this.isEnd;
			}
			

			//méthodes
			
			public void countDownTimer() {
				
			//création d'un timer
			 timer = new Timer();


	        timer.scheduleAtFixedRate(new TimerTask() {
	          

	            public void run() {
					
	                second--;
					ddSecond =dFormat.format(second);
					ddMinute= dFormat.format(minute);
					
				
	                if (minute==0 && second==0) {
	                    isEnd=true;
						timer.cancel();
	                   System.out.println("over");
	                }

					if(second==0&& minute!=0){
						second=59;
						minute--;
						ddSecond =dFormat.format(second);
						ddMinute= dFormat.format(minute);
					}
	            }
	        }, 0, 1000);
				
			}

			
			
		}
