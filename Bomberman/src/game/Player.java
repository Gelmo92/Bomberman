package game;

import java.awt.Point;

class Player extends Entity {
	private Point position;
	private Point nextPos;
	private Map mapRef;
	private int life;
	private final static int MAX_LIFE = 3;

	public Player(Point firstPlayerPos, Map mapRef) {
		position = firstPlayerPos;
		this.life = MAX_LIFE;
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
		if(mapRef.canMove(nextPos, this)) {
			position = nextPos;
		}
	}
	
	void harm() {
		this.life--;
		if (this.life == 0) {
			this.destroy();
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

	public void regen() {
		if(this.life < MAX_LIFE) {
			life++;
		}
		
	}

}
