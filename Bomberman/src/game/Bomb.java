package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

class Bomb extends Entity implements ActionListener{

	private Point pos;
	private Point nextPos;
	private static Map mapRef;
	private static boolean bonusMoveBomb = false;
	private boolean moving = false;
	private Direction direction = Direction.NONE;
	private static int numberBomb = 1;
	private static int droppedBombs = 0;
	private static final int DELAY = 3000;  //milliseconds
	Timer t;
	
	public Bomb(Point newPos, Map map) {
		pos = newPos;
		droppedBombs++;
		if(mapRef == null) {
			mapRef = map;
		}
		Controller.t.addActionListener(this);
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers();
							}
		  };
		  t = new Timer(DELAY, taskPerformer);
		  t.setRepeats(false);
		  t.start();
		  
		
	}

	@Override
	Point getPos() {
		return pos;
	}

	@Override
	void move(int movement, Direction dir) {
		nextPos = new Point(pos);
		this.direction = dir;
		switch(this.direction) {
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
		if(mapRef.canMove(nextPos, this)) {
			pos = nextPos;
			
			this.moving = true;
		}
		else {
			this.direction = Direction.NONE;
			this.moving = false;
		}
		
	}

	@Override
	Point destroy() {
		droppedBombs--;
		this.direction = Direction.NONE;
		this.moving = false;
		Controller.t.removeActionListener(this);
		deleteObservers();
		return this.getPos();
		
	}

	public void dominoEffect() {
		t.stop();
		t.setInitialDelay(10);
		t.start();
		
	}

	public static void increaseNumberBomb() {
		numberBomb++;
	}
	public static int getDroppedBombs() {
		return droppedBombs;
	}

	public static int getNumberBomb() {
		return numberBomb;
	}
	
	public static boolean getCanMove() {
		return bonusMoveBomb;
	}
	
	public static void setCanMove() {
		bonusMoveBomb = true;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(bonusMoveBomb && moving) {
			this.move(MapView.cell, this.direction);
		}
		
	}
}
