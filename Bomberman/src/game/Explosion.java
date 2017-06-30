package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Explosion extends Entity {

	ArrayList<Point> propagation;
	private static final int DELAY = 3000;  
	private Map mapRef = null;
	private static int explosionRate = 1;
	Timer t;
	
	public Explosion(Point firstPosition, Map map) {
		propagation = new ArrayList<Point>();
		propagation.add(firstPosition);
		if(mapRef == null) {
			mapRef = map;
		}
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers();
							}
		  };
		  t = new Timer(DELAY, taskPerformer);
		  t.setRepeats(false);
		  mapRef.timers.add(t);
		  t.start();
		  burn(firstPosition, explosionRate);
	}

	private void burn(Point firstPosition, int explosionRate) {
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
		Point pos = new Point(propagation.get(0));
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
			
			for(Terrain next : mapRef.myTerrains) {
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
				if(mapRef.canDestroy(nextPos)) {
					propagation.add(nextPos);
				}
				return;
			}
		}

	}

	@Override
	void destroy() {
		mapRef.timers.remove(t);
		deleteObservers();
	}

	public static void increaseRate() {
		explosionRate++;
	}

	@Override
	public String toString() {
		return "EXPLOSION";
	}
}
