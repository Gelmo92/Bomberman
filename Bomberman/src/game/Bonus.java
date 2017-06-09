package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Bonus extends Entity {

	private Point position;
	private int delay = 20000;  //milliseconds
	Timer t;
	
	public Bonus(Point pos) {
		this.position = pos;
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
		return position;
	}

	@Override
	void move(int movement, int direction) {
		// TODO Auto-generated method stub

	}

	@Override
	Point destroy() {
		deleteObservers();
		return null;
	}

}
