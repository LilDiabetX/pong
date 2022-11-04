package model;

public class RandomEvent {
	
	// Les évènements aléatoires ont une position en abscisse (posX) et une position en ordonnée (posY). Ils ont également un Court sur lequel ils seront créés
	private double posX;
	private double posY;
	private Court court;

	// Crée un évènement aléatoire quelque part dans le deuxième tier du court donné en paramètre
	RandomEvent(Court court){
		this.court = court;
		this.posX = this.court.getWidth()/3 + (Math.random()*(this.court.getWidth()/3));
		this.posY = (Math.random()*(this.court.getHeight()));
	}

	// renvoie la position en abscisse de l’évènement aléatoire
	public double getPosX(){
		return this.posX;
	}

	// renvoie la position en ordonnée de l’évènement aléatoire
	public double getPosY(){
		return this.posY;
	}

	// renvoie le court
	public Court getCourt(){
		return this.court;
	}
}