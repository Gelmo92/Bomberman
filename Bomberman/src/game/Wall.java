package game;

import java.awt.Point;

class Wall extends Entity {
	
	private Point position;

	public Wall(Point firstPosition) {
		position = firstPosition;
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
	void destroy() {
		// TODO Auto-generated method stub
		
	}

	

}
