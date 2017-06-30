package game;

import java.awt.Point;
/**
 * La classe terrain rappresenta il terreno su cui si possono muovere i mostri,
 * le bombe e il personaggio. Inoltre il terreno camvia aspeto grafico
 * se viene colpito da una esplosione
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 *
 */
public class Terrain {

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
	 * @param pos sono le coordinate del terreno
	 * @param burnt se vero allora il terreno è bruciato
	 */
	public Terrain(Point pos, boolean burnt) {
		this.position = pos;
		this.burnt = burnt;
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
	 * @return true se il terreno è bruciato
	 */
	boolean getBurnt() {
		return burnt;
	}
	
	/**
	 * setta la variabile burnt a true per indicare che quel terreno è bruciato
	 */
	void setBurnt() {
		this.burnt = true;
	}
}
