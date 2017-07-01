package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import game.Entity.Direction;

class Controller extends Observable implements KeyListener, Observer{

	private Map mapRef;
	private final static int DELAY = 500;
	private Timer t;
	private ActionListener taskPerformer;
	private static boolean released = true;
	
	public Controller(Map myMap, MapView myMapView) {
		this.mapRef = myMap;
		mapRef.controllerRef = this;
		mapRef.addObserver(this);
		addObserver(myMapView);
		 	taskPerformer = new ActionListener() {
		 		@Override
		 		public void actionPerformed(ActionEvent e) {
		 			setChanged();
		 			notifyObservers(e);
		 		}
		 	};
		  t = new Timer(DELAY, taskPerformer);
		  myMap.synchMobs(t);
		  t.start();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!mapRef.isPlayerAlive() || !released) return;
		released = false;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_RIGHT: 
			mapRef.myPlayer.move(MapView.CELL, Direction.RIGHT);
			break;
		case KeyEvent.VK_DOWN: 
			mapRef.myPlayer.move(MapView.CELL, Direction.DOWN);
			break;
		case KeyEvent.VK_LEFT: 
			mapRef.myPlayer.move(MapView.CELL, Direction.LEFT);
			break;
		case KeyEvent.VK_UP: 
			mapRef.myPlayer.move(MapView.CELL, Direction.UP);
			break;
		case KeyEvent.VK_SPACE:
			mapRef.dropBomb();
			break;
		}
		setChanged();
		notifyObservers(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		released = true;
		
	}

	@Override
	public void update(Observable o, Object arg) {
		
		t.stop();
		
		setChanged();
		notifyObservers(arg);
		deleteObservers();		
	}
	
	Timer getT() {
		return t;
	}

}
