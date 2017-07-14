package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

class Bomb extends Entity{

	private Point pos;
	private Point nextPos;
	private static Map mapRef;
	private static boolean bonusMoveBomb = false;
	private boolean moving = false;
	private Direction direction = Direction.NONE;
	private static int numberBomb = 1; //Massimo numero di bombe posizionabili
	private static int droppedBombs = 0;
	private static final int DELAY = 3000;  
	private Timer t;
	
	public Bomb(Point newPos, Map map) {
		pos = newPos;
		droppedBombs++;
		addObserver(map);
		if(mapRef == null) {
			mapRef = map;
		}
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mapRef != null) {//Controlliamo di non essere gia usciti dalla partita
					setChanged();
					notifyObservers();
				}
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
		if(!bonusMoveBomb) {
			return;
		}
		if(!moving) {
			moving = !moving;
			this.direction = dir;
			return;
		}
		nextPos = new Point(pos);
		switch(this.direction) {
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
		if(mapRef.canMove(nextPos, this)) {
			pos = nextPos;
		}
		else {
			this.direction = Direction.NONE;
			this.moving = false;
		}
		
	}

	@Override
	void destroy() {
		mapRef.removeFromArrayList(this);
		droppedBombs--;
		this.direction = Direction.NONE;
		this.moving = false;
		deleteObservers();		
	}

	/**
	 * 
	 */
	public void dominoEffect() {
		t.stop();
		t.setInitialDelay(10);
		t.start();
		
	}

	static int getDroppedBombs() {
		return droppedBombs;
	}

	static int getNumberBomb() {
		return numberBomb;
	}
	
	static boolean getCanMove() {
		return bonusMoveBomb;
	}
	
	Direction getDirection() {
		return direction;
	}
	
	static void setCanMove() {
		bonusMoveBomb = true;
		
	}
	
	@Override
	public String toString() {
		return "BOMB";
	}

	static void resetStatic() {
		mapRef = null;
		bonusMoveBomb = false;
		numberBomb = 1;
		droppedBombs = 0;
	}

	boolean isMoving() {
		return moving;
	}

	static void increaseNumberBomb() {
		numberBomb++;		
	}
}
