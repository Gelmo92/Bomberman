package game;

import java.awt.Point;

class Player extends Entity {
	private Point position;

	public Player(Point firstPlayerPos) {
		// TODO Auto-generated constructor stub
	}

	@Override
	Point getPos() {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	void move(Point newPosition) {
		position.x = newPosition.x;
		position.y = newPosition.y;
		
	}

	@Override
	void destroy() {
		// TODO Auto-generated method stub
		
	}

}
