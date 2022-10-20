package model;

public class EvenementAleatoire{
	
	private double posX;
	private double posY;
	private Court court;

	// Crée un évenement aléatoire quelque part dans le deuxième tier du court donné en paramètre
	EvenementAleatoire(Court court){
		this.court = court;
		this.posX = this.court.getWidth()/3 + (Math.random()*(this.court.getWidth()/3));
		this.posY = (Math.random()*(this.court.getHeight()));
	}

	public double getPosX(){
		return this.posX;
	}

	public double getPosY(){
		return this.posY;
	}
}