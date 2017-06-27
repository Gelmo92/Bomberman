package game;

import java.awt.Point;
/**
 * La classe Wall rappresenta il muro nella mappa.
 * Gli oggetti Wall possono essere o muri di perimetro che delimitano l'area di gioco
 * o posso essere muri interni se sono interni possono essere o distruttibili o non 
 * distruttibili.
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 */
class Wall extends Entity {
	
	private Point position;
	boolean destroyable;
	public boolean perimetry;
	
	/**
	 * 
	 * @param firstPosition sono le coordinate del muro
	 * @param destroyable definisce se il muro è distruttibile
	 * @param perimetry definisce se il muro è distruttibile
	 */
	public Wall(Point firstPosition, boolean destroyable, boolean perimetry) {
		position = firstPosition;
		this.destroyable = destroyable;
		this.perimetry = perimetry;
	}
	/**
	 * @return le cooridnate del muro
	 */
	@Override
	Point getPos() {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	void move(int movement, int direction) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Il metodo va a rimuovere il muro dagli oggetti osservati
	 * 
	 * @return le coordiante del muro
	 */
	@Override
	Point destroy() {
		deleteObservers();
		return position;
		
	}

	

}
