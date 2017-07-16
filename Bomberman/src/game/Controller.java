package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import game.Entity.Direction;

/**
 * La classe Controller gestisce sia gli input da tastiera dell'utente, sia un timer,
 * il quale permette a Map di muovere gli oggetti di tipo Entity
 * che quest'ultima gestisce.
 * Per ogni partita viene creato un solo oggetto Controller.
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 */
class Controller implements KeyListener{

	private Map mapRef;
	private GameFrame gameFrame;
	private final static int DELAY = 500;
	private Timer t;
	private ActionListener tickPerformer;
	private static boolean released = true;
	
	/**
	 * Genera un timer.
	 * Crea un ActionListener del suddetto timer, che richiama map.moveEntities().
	 * 
	 * @param map indica l'oggetto di tipo Map generato per questa partita
	 * @param gameFrame indica l'oggetto di tipo GameFrame generato per questa partita,
	 * nel quale questo oggetto di tipo Controller sara' KeyListener
	 */
	Controller(Map map, GameFrame gameFrame) {
		this.mapRef = map;
		this.gameFrame = gameFrame;
		mapRef.setControllerRef(this);
		gameFrame.addKeyListener(this);//myController gestirà la pressione dei tasti della tastiera
		 	tickPerformer = new ActionListener() {
		 		@Override
		 		public void actionPerformed(ActionEvent e) {//Causa il tick per il movimento delle entita'
		 			mapRef.moveEntities();
		 		}
		 	};
		  t = new Timer(DELAY, tickPerformer);
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
	 * il movimento del giocatore o la generazione di una bomba. Per qualsiasi altro
	 * tasto non compie azioni.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(!mapRef.getPlayerAlive() || !released) return;//Non permette all'utente di compiere ulteriori azioni in gioco finche' non rilascia il tasto premuto o se il giocatore e' morto
		released = false;
		Direction playerDir = null;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_RIGHT: 
			playerDir = Direction.RIGHT;
			break;
		case KeyEvent.VK_DOWN: 
			playerDir = Direction.DOWN;
			break;
		case KeyEvent.VK_LEFT: 
			playerDir = Direction.LEFT;
			break;
		case KeyEvent.VK_UP: 
			playerDir = Direction.UP;
			break;
		case KeyEvent.VK_SPACE:
			mapRef.dropBomb();
			return;
		default:
			return;
		}
		mapRef.movePlayer(playerDir);
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
	 * chiama la funzione omonima di GameFrame per fermare la partita.
	 * 
	 * @param playerAlive true se il giocatore e' vivo, false altrimenti
	 * @param score indica il punteggio finale
	 */
	void gameOver(boolean playerAlive, int score) {	
		gameFrame.gameOver(playerAlive, score);
	}

	/**
	 * Ferma il timer creato da questo oggetto.
	 * Svuota tutti gli ArrayList di map.
	 */
	void clearMap() {
		t.stop();
		mapRef.clearArrays();
	}

}
