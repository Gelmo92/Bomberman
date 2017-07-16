package game;

import java.awt.Point;
/**
 *La classe Chest rappresenta gli scrigni che se vengono colpiti da una esplisione rilasciano un bonus
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 *
 */
public class Chest extends Entity {
	
	private Point position;
	private static Map mapRef;
	/**
	 * 
	 * @param newPosition sono le coordinate dello scrigno
	 */
	public Chest(Point newPosition, Map map) {
		this.position = newPosition;
		if(mapRef == null) {
			mapRef = map;
		}
	}
	/**
	 * @return le coordinate dello scrigno 
	 */
	@Override
	Point getPos() {
		return position;
	}

	/**
	 * Non necessario
	 */
	@Override
	void move(int movement, Direction direction) {

	}
	
	/**
	 * Il metodo rimuove la cassa dalla lista delle casse di map e invoca il metodo dropBonus di map
	 * 
	 * @see Map#dropBonus(Point)
	 */
	@Override
	void destroy() {
		mapRef.dropBonus(position);
		mapRef.removeFromArrayList(this);
	}
	
	/**
	 * 
	 * @return una stringa per identificare gli oggetti di tipo Chest
	 */
	@Override
	public String toString() {
		return "CHEST";
	}
	
	/**
	 * Il metodo e'utilizzato per ripristinare le variabili statiche alle condizioni iniziali cancellando ogni modifica fatta
	 */
	public static void resetStatic() {
		mapRef = null;
	}
}
