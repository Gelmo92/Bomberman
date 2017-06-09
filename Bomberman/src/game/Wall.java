package game;

import java.awt.Point;

class Wall extends Entity {
	
	private Point position;
	boolean destroyable;
	public boolean perimetry;
	
	public Wall(Point firstPosition, boolean destroyable, boolean perimetry) {
		position = firstPosition;
		this.destroyable = destroyable;
		this.perimetry = perimetry;
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
		return position;
		
	}

	

}
