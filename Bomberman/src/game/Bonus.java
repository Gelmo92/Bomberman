package game;

import java.awt.Point;

public class Bonus extends Entity {

	private Point position;
	
	public Bonus(Point pos) {
		this.position = pos;
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
		// TODO Auto-generated method stub
		return null;
	}

}
