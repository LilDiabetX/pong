 package model;




	import java.awt.Font;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.text.DecimalFormat;

	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.Timer;
	
	
	public class Countdown {

		JFrame window;
		JLabel counterLabel;
		Font font1= new Font("Arial",Font.PLAIN,60);
	    Timer timer;
	    Court c;
	    
	    int second,minute;
	    String ddSecond,ddMinute;
	    
	    DecimalFormat dFormat =new DecimalFormat("00");
	    
	
		
		public Countdown() {
			window = new JFrame() ;
			window.setSize(800, 600);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setLayout(null);
			
			counterLabel =new JLabel("");
			counterLabel.setBounds(300, 230, 200, 100);
			counterLabel.setVerticalAlignment(JLabel.NORTH);
			counterLabel.setFont(font1);
			
			window.add(counterLabel);
			window.setVisible(true);
			
			
	
			counterLabel.setText("00:10");
			second=10;
			minute=0;
			countDownTimer();
			timer.start();
			
		}
		
	
		
		
	
		
		public void countDownTimer() {
			timer= new Timer(1000, new ActionListener() {
				
				 @Override
				 public void actionPerformed(ActionEvent e) {
					 
					second--;
					
					ddSecond =dFormat.format(second);
					ddMinute= dFormat.format(minute);
					counterLabel.setText(ddMinute+":"+ddSecond);
					
					if(second==-1) {
						second=59;
						minute--;

						ddSecond =dFormat.format(second);
						ddMinute= dFormat.format(minute);
						counterLabel.setText(ddMinute+":"+ddSecond);
					}
					
					if(minute==0 && second==0) {
						
						
						timer.stop();
					
						
					}
					
					
					
				 }
			});
			
			
		}
		
		
	}


