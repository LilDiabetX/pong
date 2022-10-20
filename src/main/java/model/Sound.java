package model;

import java.net.URL;
import java.io.File;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

public class Sound{

	private Clip clip;
	private URL soundURL[] = new URL[10];

	public Sound(){
		ClassLoader loader = Sound.class.getClassLoader();
		soundURL[0]=loader.getResource("sounds/sonPointPong.wav");
		soundURL[1]=loader.getResource("sounds/sonChocPong.wav");
	}

	public void setFile(int i){
		try{
			URL u = soundURL[i];
			AudioInputStream ais = AudioSystem.getAudioInputStream(u);
			this.clip=AudioSystem.getClip();
			this.clip.open(ais);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public void play(){
		this.clip.start();
	}

	public void loop(){
		this.clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop(){
		this.clip.stop();
	}
	
}