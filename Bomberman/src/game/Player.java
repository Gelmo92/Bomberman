package game;

import java.awt.Point;

class Player extends Entity {
	private Point position;
	private Point nextPos;
	private Map mapRef;

	public Player(Point firstPlayerPos, Map mapRef) {
		position = firstPlayerPos;
		this.mapRef = mapRef;
		
	}

	@Override
	Point getPos() {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	void move(int movement, int direction) {
		nextPos = new Point(position);
		switch(direction) {
		case 1:
			nextPos.x += movement;//*MapView.cell;
			break;
		case 2:
			nextPos.y += movement;//*MapView.cell;
			break;
		case 3:
			nextPos.x -= movement;//*MapView.cell;
			break;
		case 4:
			nextPos.y -= movement;//*MapView.cell;
			break;
		}
		if(mapRef.canMove(nextPos, 1)) {
			position = nextPos;
		}
	}
		

	@Override
	Point destroy() {
		Map.playerAlive = false;
		setChanged();
		notifyObservers();
		deleteObservers();
		return null;
	}

}
