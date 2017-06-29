package game;

import java.awt.Point;
import java.util.Random;

import game.Entity.Direction;

class Mob extends Entity {
	
	private Point position;
	Point nextPos;
	private Direction direction = Direction.UP;
	private boolean leftFoot = true;
	private static Map mapRef = null;
	private Direction[]directions=Direction.values();
	private Random random = null;
	
	public Mob(Point firstMobPos, Map map) {
		position = firstMobPos;
		if(mapRef == null) {
			mapRef = map;
		}
	}

	@Override
	Point getPos() {
		return position;
	}

	@Override
	void move(int movement, Direction direction) {
		nextPos = new Point(position);
		Direction newDirection = Direction.getRandom();
				
		for(int counter = 0; counter < 4; counter++) {
			switch(newDirection) {
				case RIGHT:
					nextPos.x += movement;//*MapView.cell;
					break;
				case DOWN:
					nextPos.y += movement;//*MapView.cell;
					break;
				case LEFT:
					nextPos.x -= movement;//*MapView.cell;
					break;
				case UP:
					nextPos.y -= movement;//*MapView.cell;
					break;
				}
			this.direction = newDirection;
			setFoot();
			if(mapRef.canMove(nextPos, this)) {
				
				position = nextPos;
				
				break;
			}
			else {
				nextPos.setLocation(position);
			}
			/*else {
				nextPos.setLocation(position);
				switch(newDirection) {
					case RIGHT:
						newDirection = Direction.DOWN;
						break;
					case DOWN:
						newDirection = Direction.LEFT;
						break;
					case LEFT:
						newDirection = Direction.UP;
						break;
					case UP:
						newDirection = Direction.RIGHT;
						break;
				}
			}*/
			
		}
	}
	
	public Direction getDir() {
		return this.direction;
	}
	
	public boolean getFoot() {
		return this.leftFoot;
	}
	private void setFoot() {
		if(leftFoot) {
			leftFoot = false;
		}
		else {
			leftFoot = true;
		}
	}
	
	@Override
	Point destroy() {
		deleteObservers();
		return null;
		
	}

}
