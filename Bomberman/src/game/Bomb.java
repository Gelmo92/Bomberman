package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

class Bomb extends Entity {

	private Point pos;
	private int delay = 3000;  //milliseconds
	Timer t;
	
	public Bomb(Point newPos) {
		pos = newPos;
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers();
							}
		  };
		  t = new Timer(delay, taskPerformer);
		  t.setRepeats(false);
		  t.start();
		
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
	Point destroy() {
		deleteObservers();
		return this.getPos();
		
	}

	public void dominoEffect() {
		t.stop();
		t.setInitialDelay(10);
		t.start();
		
	}

}
