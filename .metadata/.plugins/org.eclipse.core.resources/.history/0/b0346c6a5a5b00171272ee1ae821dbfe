package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

class Bomb extends Entity {

	private Point pos;
	private static boolean bonusMoveBomb = false;
	private static int numberBomb = 1;
	private static int droppedBombs = 0;
	private static final int DELAY = 3000;  //milliseconds
	Timer t;
	
	public Bomb(Point newPos) {
		pos = newPos;
		droppedBombs++;
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
	void move(int movement, Direction direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	Point destroy() {
		droppedBombs--;
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
}
