package game;

import java.awt.Point;

/**
 * La classe Wall rappresenta un muro nella mappa.
 * Gli oggetti Wall possono essere muri perimetrali che delimitano l'area di gioco
 * o muri interni.I muri interni possono essere distruttibili o non 
 * distruttibili.
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 */
class Wall extends Entity {
	
	private Point position;
	private boolean destroyable;
	private boolean perimetral;
	
	/**
	 * 
	 * @param pos sono le coordinate del muro
	 * @param destroyable definisce se il muro è distruttibile
	 * @param perimetral definisce se il muro è perimetrale
	 */
	Wall(Point pos, boolean destroyable, boolean perimetral) {
		position = pos;
		this.destroyable = destroyable;
		this.perimetral = perimetral;
	}
	
	/**
	 * @return le coordinate del muro
	 */
	@Override
	Point getPos() {
		return position;
	}
	
	/**
	 * Non necessario dato che i muri non si possono muovere
	 */
	@Override
	void move(int movement, Direction direction) {
		
	}

	/**
	 * Non necessario, se ne occupa Map
	 */
	@Override
	void destroy() {
		
	}
	
	/**
	 * @return true se il muro e' distruttibile
	 */
	boolean getDestroyable() {
		return this.destroyable;
	}
	
	/**
	 * @return true se il muro e' perimetrale
	 */
	boolean getPerimetral() {
		return this.perimetral;
	}
	
	/**
	 * @return una String mnemonica della classe di questo oggetto
	 */
	@Override
	public String toString() {
		return "WALL";
	}

}
