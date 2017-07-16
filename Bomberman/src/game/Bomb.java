package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
/**
 * La classe Bomb rappresenta le bombe che possono essere posizionate dal giocatore sul terreno di gioco.
 * Alla loro creazione si attiva un timer e allo scadere del timer la bomba notifica ai suoi
 * Observer la propria esplosione.
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 */
class Bomb extends Entity{

	private Point pos;
	private Point nextPos;
	private static Map mapRef;
	private static boolean bonusMoveBomb = false;
	private boolean moving = false;
	private Direction direction = Direction.NONE;
	private static int numberBomb = 1; //Massimo numero di bombe posizionabili
	private static int droppedBombs = 0;
	private static final int DELAY = 3000;  
	private Timer t;
	
	/**
	 * 
	 * @param newPos sono le coordinate del punto in cui verra' posizionata la bomba
	 * @param map e' il riferimento alla mappa di gioco
	 */
	
	Bomb(Point newPos, Map map) {
		pos = newPos;
		droppedBombs++;
		addObserver(map);
		if(mapRef == null) {
			mapRef = map;
		}
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mapRef != null) {//Controlliamo di non essere gia usciti dalla partita
					setChanged();
					notifyObservers();
				}
			}
		  };
		  t = new Timer(DELAY, taskPerformer);
		  t.setRepeats(false);
		  t.start();
	}
/**
 * 
 * @return le corrdinate della bomba
 */
	@Override
	Point getPos() {
		return pos;
	}
/**
 * Gestisce il movimento della bomba se la bomba si puo' muovere.
 * Se bonusMoveBomb è false allora la bomba non si puo' muovere e termina la procedura.
 * Se invece la bomba si puo' muovere ma non si e' ancora mossa allora imposta la variabile moving a true 
 * ma si muovera' solo alla successiva chiamata di questo metodo (per evitare un doppio movimento)
 * Se si puo' muovere ed e' in movimento allora gestisce il movimento della bomba nella direzione indicata (se non incontra ostacoli).
 * 
 * @param movement indica di quanto si deve spostare la bomba 
 * @param dirindica la direzione in cui si deve spostare la bomba
 */
	@Override
	void move(int movement, Direction dir) {
		if(!bonusMoveBomb) {
			return;
		}
		if(!moving) {
			moving = !moving;
			this.direction = dir;
			return;
		}
		nextPos = new Point(pos);
		switch(this.direction) {
		case RIGHT:
			nextPos.x += movement;
			break;
		case DOWN:
			nextPos.y += movement;
			break;
		case LEFT:
			nextPos.x -= movement;
			break;
		case UP:
			nextPos.y -= movement;
			break;
		default:
			break;
		}
		if(mapRef.canMove(nextPos, this)) {
			pos = nextPos;
		}
		else {
			this.direction = Direction.NONE;
			this.moving = false;
		}
		
	}
/**
 * Gestisce la rimozione della bomba dalla lista delle bombe nella mappa,
 * decrementa il valore di droppedBombs per poter posizionare una nuova bomba,
 * Rimuove gli Observer di questa bomba
 * 
 */
	@Override
	void destroy() {
		mapRef.removeFromArrayList(this);
		droppedBombs--;
		this.direction = Direction.NONE;
		this.moving = false;
		deleteObservers();		
	}

	/**
	 * Questo metodo permette la concatenazione delle esplosioni di piu' bombe,
	 * a causa di un fattore scatenante (nel nostro caso l'esplosione di una bomba se raggiunge
	 * un'altra bomba causa l'esplosione di quest'ultima).
	 */
	void dominoEffect() {
		t.stop();
		t.setInitialDelay(10);//Ritardiamo minimamente il segnale per permettere di completare il metodo che ha richiamato questo metodo
		t.start();
		
	}
	/**
	 * 
	 * @return il numero di bombe posizionate
	 */
	static int getDroppedBombs() {
		return droppedBombs;
	}
	
	/**
	 * 
	 * @return il massimo numero di bombe posizionabili
	 */
	static int getNumberBomb() {
		return numberBomb;
	}
	
	/**
	 * 
	 * @return indica se e' stato abilitato il movimento alle bombe
	 */
	static boolean getCanMove() {
		return bonusMoveBomb;
	}
	
	/**
	 * 
	 * @return la direzione in cui si sta' muovendo la bomba
	 */
	Direction getDirection() {
		return direction;
	}
	
	/**
	 * Abilita il movimento delle bombe
	 */
	static void setCanMove() {
		bonusMoveBomb = true;
		
	}
	
	/**
	 * @return una String mnemonica della classe di questo oggetto
	 */
	@Override
	public String toString() {
		return "BOMB";
	}

	/**
	 * 
	 * @return se la bomba e' in movimento o no
	 */
	boolean isMoving() {
		return moving;
	}

	/**
	 * Aumenta il massimo numero di bombe posizionabili di 1
	 */
	static void increaseNumberBomb() {
		numberBomb++;		
	}
	
	/**
	 * Il metodo e'utilizzato per ripristinare le variabili alla condizione iniziale cancellando ogni modifica fatta
	 * 
	 */
	static void resetStatic() {
		mapRef = null;
		bonusMoveBomb = false;
		numberBomb = 1;
		droppedBombs = 0;
	}
}
