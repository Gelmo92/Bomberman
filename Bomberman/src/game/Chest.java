package game;

import java.awt.Point;

public class Chest extends Entity {
	
	private Point position;
	
	public Chest(Point newPosition) {
		this.position = newPosition;
	}

	@Override
	Point getPos() {
		return position;
	}

	@Override
	void move(int movement, int direction) {
		// TODO Auto-generated method stub

	}

	@Override
	Point destroy() {
		return position;
	}

}
