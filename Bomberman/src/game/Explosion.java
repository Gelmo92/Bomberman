package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;
/**
 * La classe Explosion gestisce le esplosioni degli oggetti di tipo Bomb
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 */
class Explosion extends Entity {

	private ArrayList<Point> propagation;
	private final Point firstPosition;
	private static final int DELAY = 3000;  
	private static Map mapRef = null;
	private static int explosionRate = 1;
	private Timer t;
	
	/**
	 * Il costruttore invoca la propagazione della esplosione in tutte le direzioni tramite il metodo burn
	 * 
	 * @param firstPosition e' il punto in cui si trovava l'oggetto di tipo Bomb che e' esploso
	 * @param map e' il riferimento alla mappa di gioco
	 * @see Explosion#burn(int)
	 */
	Explosion(Point firstPosition, Map map) {
		propagation = new ArrayList<Point>();
		addObserver(map);
		this.firstPosition = firstPosition;
		if(mapRef == null) {
			mapRef = map;
		}
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mapRef !=null) {//Controlliamo di non essere gia usciti dalla partita
					setChanged();
					notifyObservers();
				}
							}
		  };
		  t = new Timer(DELAY, taskPerformer);
		  t.setRepeats(false);
		  t.start();
		  burn(explosionRate);
	}

	/**
	 * Il metodo gestisce la propagazione in tutte le direzioni invocando il metodo move
	 * 
	 * @param explosionRate e' il valore di quanto si puo' espandere l'esplosione per ogni direzione
	 * @see Explosion#move(int, Direction)
	 */
	private void burn(int explosionRate) {
		move(explosionRate,Direction.NONE);
		move(explosionRate,Direction.UP);
		move(explosionRate,Direction.DOWN);
		move(explosionRate,Direction.LEFT);
		move(explosionRate,Direction.RIGHT);
		
	}

	/**
	 * Non utilizzato.
	 */
	@Override
	Point getPos() {
		return null;
	}
	
	/**
	 * Il metodo gestisce il moviemnto (la propagazione) in una delle direzioni 
	 * 
	 * @param movement e' il valore massimo di quanto si dovrebbe muovere l'esplosione (se non incontra ostacoli)
	 * @param direction e' la direzione in cui si muove l'esplosione
	 */
	@Override
	void move(int movement, Direction direction) {
		Point pos = new Point(this.firstPosition);
		for(int i = 0; movement > i; i++) {
			Point nextPos = new Point(pos);
			switch(direction) {
			case NONE:
				break;
			case RIGHT:
				nextPos.x += MapView.CELL;
				break;
			case DOWN:
				nextPos.y += MapView.CELL;
				break;
			case LEFT:
				nextPos.x -= MapView.CELL;
				break;
			case UP:
				nextPos.y -= MapView.CELL;
				break;
			default:
				break;
			}
			
			for(Terrain next : mapRef.getMyTerrains()) {
				if(nextPos.equals(next.getPos())) {
					next.setBurnt();//Bruciamo il terreno
					break;
				}
			}
			if(mapRef.canMove(nextPos, this)) {
				propagation.add(nextPos);//Avanziamo con la propagazione
				pos = nextPos;
			}
			else {
				if(mapRef.canDestroyWall(nextPos)) {
					propagation.add(nextPos);//Aggiungiamo la posizione del muro distrutto
				}
				return;
			}
		}

	}

	/**
	 * Il metodo rimuove l'oggeto dalla lista di esplosioni dalla mappa,
	 * rimuove gli Observer di questo oggetto.
	 */
	@Override
	void destroy() {
		mapRef.removeFromArrayList(this);
		deleteObservers();
	}

	/**
	 * Il metodo incrementa la propagazione massima delle esplosioni successive
	 */
	static void increaseRate() {
		explosionRate++;
	}

	/**
	 * 
	 * @return il massimo valore per cui si puo' espandere una esplosione
	 */
	static int getExplosionRate() {
		return explosionRate;
	}
	
	/**
	 * 
	 * @return l'insieme dei punti che sono interessati da questa esplosione
	 */
	ArrayList<Point> getPropagation() {
		return propagation;
	}

	/**
	 * 
	 * @return una String mnemonica della classe di questo oggetto
	 */
	@Override
	public String toString() {
		return "EXPLOSION";
	}

	/**
	 * Il metodo e'utilizzato per ripristinare le variabili statiche alle condizioni iniziali cancellando ogni modifica fatta
	 * 
	 */
	static void resetStatic() {
		mapRef = null;
		explosionRate = 1;
		
	}
}
