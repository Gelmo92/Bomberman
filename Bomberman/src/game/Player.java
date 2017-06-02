package game;

import java.awt.Point;

class Player extends Entity {
	private Point position;
	private Point nextPos;

	public Player(Point firstPlayerPos) {
		position = firstPlayerPos;
		
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
		if(Map.canMove(nextPos, 1)) {
			position = nextPos;
		}
	}
		

	@Override
	void destroy() {
		Map.playerAlive = false;
		setChanged();
		notifyObservers();
		deleteObservers();
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
