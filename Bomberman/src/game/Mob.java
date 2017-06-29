package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import game.Entity.Direction;

class Mob extends Entity implements ActionListener {
	
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.move(MapView.cell, this.direction);
		
	}

}
