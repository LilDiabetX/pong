package model;

import java.net.URL;
import java.io.File;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

public class Sound{

	private Clip clip; // le Clip permet de précharger les sons qui seront éventuellement joués afin de pouvoir les jouer n'importe quand et en boucle si besoin
	private URL soundURL[] = new URL[10]; // tableau d'URL dans lequel on placera les chemins relatifs des sons à jouer
	

	
	// initialise le tableau d'URL avec les sons à jouer. Il n'y en a que deux pour l'instant, d'autres seront à ajouter en temps voulu
	public Sound(){
		ClassLoader loader = Sound.class.getClassLoader();
		soundURL[0]=loader.getResource("sounds/sonPointPong.wav");
		soundURL[1]=loader.getResource("sounds/sonChocPong.wav");
	}

	// méthode permettant de définir le son à jouer
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

	// méthode jouant une fois le son définit avec setFile(i)
	public void play(){
		this.clip.start();
	}

	// méthode jouant en boucle le son définit avec setFile(i)
	public void loop(){
		this.clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	// méthode stopant la boucle initiée avec loop()
	public void stop(){
		this.clip.stop();
	}
	
}