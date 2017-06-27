package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Explosion extends Entity {

	ArrayList<Point> propagation;
	private static final int DELAY = 3000;  //milliseconds
	private Map mapRef;
	private static int explosionRate = 1;
	
	public Explosion(Point firstPosition, Map mapRef) {
		propagation = new ArrayList<Point>();
		propagation.add(firstPosition);
		this.mapRef = mapRef;
		burn(firstPosition, explosionRate);
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers();
							}
		  };
		  Timer timer = new Timer(DELAY, taskPerformer);
		  timer.setRepeats(false);
		  timer.start();
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
		// TODO Auto-generated method stub
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
				nextPos.x += MapView.cell;
				break;
			case DOWN:
				nextPos.y += MapView.cell;
				break;
			case LEFT:
				nextPos.x -= MapView.cell;
				break;
			case UP:
				nextPos.y -= MapView.cell;
				break;
			}
			for(Terrain next : mapRef.myTerrain) {
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
				if(Map.canDestroy(nextPos)) {
					propagation.add(nextPos);
				}
				return;
			}
		}

	}

	@Override
	Point destroy() {
		deleteObservers();
		return null;
	}

	public static void increaseRate() {
		explosionRate++;
	}

}
