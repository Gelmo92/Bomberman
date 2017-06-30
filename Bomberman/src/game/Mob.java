package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Mob extends Entity implements ActionListener {
	
	private Point position;
	Point nextPos;
	private Direction direction = Direction.NONE;
	private boolean leftFoot = true;
	private Map mapRef = null;
	
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
		mapRef.controllerRef.getT().removeActionListener(this);
		this.direction = Direction.DEAD;
		setChanged();
		notifyObservers();
		deleteObservers();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.move(MapView.CELL, this.direction);
		
	}
	
	@Override
	public String toString() {
		return "MOB";
	}
}
