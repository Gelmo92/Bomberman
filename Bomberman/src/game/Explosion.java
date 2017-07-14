package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

class Explosion extends Entity {

	private ArrayList<Point> propagation;
	private final Point firstPosition;
	private static final int DELAY = 3000;  
	private static Map mapRef = null;
	private static int explosionRate = 1;
	private Timer t;
	
	public Explosion(Point firstPosition, Map map) {
		propagation = new ArrayList<Point>();
		addObserver(map);
		this.firstPosition = firstPosition;
		if(mapRef == null) {
			mapRef = map;
		}
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mapRef !=null) {//Controlliamo di non essere gia usciti dalla partita
					setChanged();
					notifyObservers();
				}
							}
		  };
		  t = new Timer(DELAY, taskPerformer);
		  t.setRepeats(false);
		  t.start();
		  burn(explosionRate);
	}

	private void burn(int explosionRate) {
		move(explosionRate,Direction.NONE);
		move(explosionRate,Direction.UP);
		move(explosionRate,Direction.DOWN);
		move(explosionRate,Direction.LEFT);
		move(explosionRate,Direction.RIGHT);
		
	}

	@Override
	Point getPos() {
		return null;
	}

	@Override
	void move(int movement, Direction direction) {
		Point pos = new Point(this.firstPosition);
		for(int i = 0; movement > i; i++) {
			Point nextPos = new Point(pos);
			switch(direction) {
			case NONE:
				break;
			case RIGHT:
				nextPos.x += MapView.CELL;
				break;
			case DOWN:
				nextPos.y += MapView.CELL;
				break;
			case LEFT:
				nextPos.x -= MapView.CELL;
				break;
			case UP:
				nextPos.y -= MapView.CELL;
				break;
			default:
				break;
			}
			
			for(Terrain next : mapRef.getMyTerrains()) {
				if(nextPos.equals(next.getPos())) {
					next.setBurnt();
					break;
				}
			}
			if(mapRef.canMove(nextPos, this)) {
				propagation.add(nextPos);
				pos = nextPos;
			}
			else {
				if(mapRef.canDestroyWall(nextPos)) {
					propagation.add(nextPos);
				}
				return;
			}
		}

	}

	@Override
	void destroy() {
		mapRef.removeFromArrayList(this);
		deleteObservers();
	}

	static void increaseRate() {
		explosionRate++;
	}

	static int getExplosionRate() {
		return explosionRate;
	}
	ArrayList<Point> getPropagation() {
		return propagation;
	}

	@Override
	public String toString() {
		return "EXPLOSION";
	}

	public static void resetStatic() {
		mapRef = null;
		explosionRate = 1;
		
	}
}
