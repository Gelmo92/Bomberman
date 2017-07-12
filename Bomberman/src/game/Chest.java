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

	@Override
	void move(int movement, Direction direction) {

	}
	
	@Override
	void destroy() {
		mapRef.dropBonus(position);
		mapRef.removeFromArrayList(this);
	}
	
	@Override
	public String toString() {
		return "CHEST";
	}
	
	public static void resetStatic() {
		mapRef = null;
	}
}
