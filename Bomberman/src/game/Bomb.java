package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

class Bomb extends Entity {

	private Point pos;
	
	public Bomb(Point newPos) {
		pos = newPos;
		
	}

	@Override
	Point getPos() {
		return pos;
	}

	@Override
	void move(int movement, int direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void destroy() {
		setChanged();
		notifyObservers();
		deleteObservers();
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
