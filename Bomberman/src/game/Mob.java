package game;

import java.awt.Point;

class Mob extends Entity {
	
	private Point position;
	Point nextPos;
	private int direction = 0;
	private Map mapRef;
	
	public Mob(Point firstMobPos, Map mapRef) {
		position = firstMobPos;
		this.mapRef = mapRef;
	}

	@Override
	Point getPos() {
		return position;
	}

	@Override
	void move(int movement, int direction) {
		nextPos = new Point(position);
		int newDirection =(int)(Math.random()*4+1);
		for(int counter = 0; counter < 4; counter++) {
			switch(newDirection) {
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
				break;
			}
			else {
				nextPos.setLocation(position);
				if(newDirection < 4) {
					newDirection++;
				}
				else {
					newDirection = 1;
				}
			}
		}
		
		/*if(this.direction == 0) {
			while(this.direction == 0) {
				direction++;
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
				if(Map.canMove(nextPos, 2)) {
					position = nextPos;
					this.direction = direction;
				}
				else {
					nextPos = position;
				}
			}
		}
		else{
			switch(this.direction) {
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
			if(Map.canMove(nextPos, 2)) {
				position = nextPos;
			}
			else{
				nextPos = position;
				int newDirection =(int)(Math.random()*4+1);
				System.out.println(newDirection);
				switch(newDirection) {
				case 1:
					this.direction = 3;
					break;
				case 2:
					this.direction = 4;
					break;
				case 3:
					this.direction = 1;
					break;
				case 4:
					this.direction = 2;
					break;
			
				}
			}
		}*/
	}

	@Override
	Point destroy() {
		deleteObservers();
		return null;
		
	}

}
