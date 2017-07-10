package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Timer;

import game.Entity.Direction;

/**
 * La classe Controller gestisce sia gli input da tastiera dell'utente, sia il timer
 * che scandisce il movimento delle entita' diverse da Player e il refresh
 * del comparto grafico.
 * Per ogni partita viene creato un solo oggetto Controller.
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 */
class Controller extends Observable implements KeyListener, Observer{

	private Map mapRef;
	private GameFrame gameFrame;
	private final static int DELAY = 500;
	private Timer t;
	private ActionListener tickPerformer;
	private static boolean released = true;
	
	/**
	 * Genera un timer, che ad ogni scadenza notifica tutti gli oggetti
	 * che sono suoi ActionListener.
	 * Crea un ActionListener del suddetto timer, che richiama notifyObservers() di
	 * questo oggetto di tipo Controller.
	 * Richiama inoltre la funzione {@link Map#synchMobs(Timer)}
	 * 
	 * @param map indica l'oggetto di tipo Map generato per questa partita
	 * @param mapView indica l'oggetto di tipo MapView generato per questa partita
	 * @param gameFrame indica l'oggetto di tipo GameFrame generato per questa partita,
	 * nel quale questo oggetto di tipo Controller sara' KeyListener
	 */
	Controller(Map map, MapView mapView, GameFrame gameFrame) {
		this.mapRef = map;
		this.gameFrame = gameFrame;
		gameFrame.addKeyListener(this);//myController gestirà la pressione dei tasti della tastiera
		map.setControllerRef(this);
		map.addObserver(this);
		addObserver(mapView);
		 	tickPerformer = new ActionListener() {
		 		@Override
		 		public void actionPerformed(ActionEvent e) {//Causa il tick per il repaint di mapView
		 			setChanged();
		 			notifyObservers(e);
		 		}
		 	};
		  t = new Timer(DELAY, tickPerformer);
		  map.synchMobs(t);//Permette ai mob di muoversi al tick del timer t
		  t.start();
	}

	/**
	 * Non utilizzato.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	/**
	 * Intercetta tutti gli input da tastiera e per determinati tasti permette
	 * il movimento del giocatore o la generazione di una bomba.
	 * Notifica inoltre i suoi Observer per permettere al giocatore di muoversi senza
	 * rispettare i tick del timer di questo oggetto.
	 */
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

	/**
	 * Impedisce azioni multiple da parte dell'utente
	 * con una sola pressione di un tasto.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		released = true;
		
	}

	/**
	 * 
	 */
	@Override
	public void update(Observable o, Object arg) {	
		gameFrame.gameOver((boolean)arg);
	}
	
	Timer getT() {
		return t;
	}
	
	void stopT() {
		t.stop();
	}

}
