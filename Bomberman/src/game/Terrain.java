package game;

import java.awt.Point;
/**
 * La classe Terrain rappresenta una cella di terreno su cui si possono muovere le entita',
 * Inoltre il terreno camvia aspeto grafico
 * se viene colpito da una esplosione
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 *
 */
class Terrain {

	private Point position;
	private boolean burnt;
	/**
	 * 
	 * @param pos sono le coordinate del terreno
	 */
	public Terrain(Point pos) {
		this.position = pos;
		burnt = false;
	}
	
	/**
	 * 
	 * @return le coordinate del terreno
	 */
	Point getPos() {
		return position;
	}
	/**
	 * 
	 * @return true se il terreno e' bruciato
	 */
	boolean getBurnt() {
		return burnt;
	}
	
	/**
	 * Setta la variabile burnt a true per indicare che quel terreno e' bruciato
	 */
	void setBurnt() {
		this.burnt = true;
	}
}
