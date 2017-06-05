package game;

import java.awt.Point;

class Wall extends Entity {
	
	private Point position;
	boolean destroyable;

	public Wall(Point firstPosition) {
		position = firstPosition;
		destroyable = true;
	}

	@Override
	Point getPos() {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	void move(int movement, int direction) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	Point destroy() {
		deleteObservers();
		return null;
		
	}

	

}
