package game;

import java.awt.Point;

class Mob extends Entity{
	
	private Point position;
	private Point nextPos;
	private Direction direction = Direction.NONE;
	private boolean leftFoot = true;
	private static Map mapRef = null;
	
	public Mob(Point firstMobPos, Map map) {
		position = firstMobPos;
		addObserver(map);
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
		switch(newDirection) {
		case RIGHT:
			nextPos.x += movement;
			break;
		case DOWN:
			nextPos.y += movement;
			break;
		case LEFT:
			nextPos.x -= movement;
			break;
		case UP:
			nextPos.y -= movement;
			break;
		default:
			break;
			}
		this.direction = newDirection;
		setFoot();
		if(mapRef.canMove(nextPos, this)) {
			position = nextPos;
		}
	}
	
	public Direction getDir() {
		return this.direction;
	}
	
	public boolean getFoot() {
		return this.leftFoot;
	}
	private void setFoot() {
		leftFoot = !leftFoot;
	}
	
	@Override
	void destroy() {
		mapRef.removeFromArrayList(this);
		this.direction = Direction.DEAD;
		setChanged();
		notifyObservers();
		deleteObservers();
	}
	
	@Override
	public String toString() {
		return "MOB";
	}

	public static void resetStatic() {
		mapRef = null;
		
	}
}
