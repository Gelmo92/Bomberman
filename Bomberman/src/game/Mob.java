package game;

import java.awt.Point;

class Mob extends Entity {
	
	private Point position;
	private Point nextPos;
	private int direction = 0;
	
	public Mob(Point firstMobPos) {
		position = firstMobPos;
	}

	@Override
	Point getPos() {
		return position;
	}

	@Override
	void move(int movement, int direction) {
		nextPos = new Point(position);
		if(this.direction == 0) {
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
		}
		if(Map.canMove(nextPos, 2)) {
			position = nextPos;
		}
		else{
			nextPos = position;
			switch(this.direction) {
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
	}

	@Override
	Point destroy() {
		return null;
		
	}

}
