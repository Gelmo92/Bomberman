package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Timer;

import game.Entity.Direction;

class Controller extends Observable implements KeyListener, Observer{

	private Map mapRef;
	private final static int DELAY = 500;
	private Timer t;
	private ActionListener tickPerformer;
	private static boolean released = true;
	
	public Controller(Map map, MapView mapView) {
		this.mapRef = map;
		map.setControllerRef(this);
		map.addObserver(this);
		addObserver(mapView);
		 	tickPerformer = new ActionListener() {
		 		@Override
		 		public void actionPerformed(ActionEvent e) {//Causa il tick per il repaint di mapView e il movimento delle entita'
		 			setChanged();
		 			notifyObservers(e);
		 		}
		 	};
		  t = new Timer(DELAY, tickPerformer);
		  map.synchMobs(t);//Permette ai mob di muoversi al tick del timer t
		  t.start();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!mapRef.getPlayerAlive() || !released) return;//Non permette all'utente di compiere ulteriori azioni in gioco finche' non rilascia il tasto premuto o se il giocatore e' morto
		released = false;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_RIGHT: 
			mapRef.getMyPlayer().move(MapView.CELL, Direction.RIGHT);
			break;
		case KeyEvent.VK_DOWN: 
			mapRef.getMyPlayer().move(MapView.CELL, Direction.DOWN);
			break;
		case KeyEvent.VK_LEFT: 
			mapRef.getMyPlayer().move(MapView.CELL, Direction.LEFT);
			break;
		case KeyEvent.VK_UP: 
			mapRef.getMyPlayer().move(MapView.CELL, Direction.UP);
			break;
		case KeyEvent.VK_SPACE:
			mapRef.dropBomb();
			break;
		}
		setChanged();
		notifyObservers(e);//Permette al giocatore di muoversi senza tener conto del tick del timer t
	}

	@Override
	public void keyReleased(KeyEvent e) {
		released = true;
		
	}

	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers(arg);//Causa in GameFrame la fine della partita, in quanto questo metodo "update" viene chiamato solo in caso di notifica da Map di vincita o perdita della partita
				
	}
	
	Timer getT() {
		return t;
	}
	
	void stopT() {
		t.stop();
	}

}
