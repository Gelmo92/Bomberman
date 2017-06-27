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
	/**
	 * 
	 * @param newPosition sono le coordinate dello scrigno
	 */
	public Chest(Point newPosition) {
		this.position = newPosition;
	}
	/**
	 * @return le coordinate dello scrigno 
	 */
	@Override
	Point getPos() {
		return position;
	}

	@Override
	void move(int movement, int direction) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * @return le coordinate dello scrigno 
	 */
	@Override
	Point destroy() {
		return position;
	}

}
