package game;

import java.awt.Point;
/**
 * La classe Wall rappresenta il muro nella mappa.
 * Gli oggetti Wall possono essere o muri di perimetro che delimitano l'area di gioco
 * o possono essere muri interni se sono interni possono essere o distruttibili o non 
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
	 * @param perimetry definisce se il muro è perimetrale
	 */
	public Wall(Point firstPosition, boolean destroyable, boolean perimetry) {
		position = firstPosition;
		this.destroyable = destroyable;
		this.perimetry = perimetry;
	}
	
	/**
	 * @return le coordinate del muro
	 */
	@Override
	Point getPos() {
		return position;
	}
	
	/**
	 * Non implementato
	 */
	@Override
	void move(int movement, Direction direction) {
		
	}

	/**
	 * Non implementato
	 */
	@Override
	void destroy() {
		
	}
	
	@Override
	public String toString() {
		return "WALL";
	}

}
