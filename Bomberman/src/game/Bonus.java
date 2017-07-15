package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * Gli oggetti della classe Bonus sono i bonus che vengono posizionati sulla mappa di gioco
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 */
public class Bonus extends Entity {

	private Point position;
	private static Map mapRef = null;
	private static final int DELAY = 20000;
	private Timer t;
	
	enum BonusType {
		LIFE,
		MOVE_BOMB,
		NUMBER_BOMB,
		RATE;
		
		/**
		 * 
		 * @return un valore randomico (privato del valore LIFE) tra i tipi di bonus che si possono creare 
		 */
		public static BonusType getRandom() {
	        return values()[(int)(Math.random() * (values().length -1)+1)];//Scelta randomica tra tutti i valori meno LIFE
	    }
	}
	
	private BonusType type;
	
	/**
	 * 
	 * @param pos sono le coordinate di dove viene posizionato il bonus
	 * @param map e' il riferimento alla mappa di gioco
	 */
	public Bonus(Point pos, Map map) {
		this.position = pos;
		addObserver(map);
		if(mapRef == null) {
			mapRef = map;
		}
		this.type = generateRandomBonus();
		ActionListener expiredPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers();
							}
		  };
		  t = new Timer(DELAY, expiredPerformer);
		  t.setRepeats(false);
		  t.start();
	}
	
	/**
	 * Il metodo genera randomicamente un tipo di bonus.
	 * Se il giocatore non ha il massimo  della vita allora si creara' un bonus di tipo LIFE
	 * Se Il tipo di bonus MOVE_BOMB e' gia' stato preso allora non verra' pi' creato
	 * 
	 * @return un BonusType randomico fra quelli che si possono ancora creare
	 */
	private static BonusType generateRandomBonus() {
		BonusType bonus = null;
		if(mapRef.getMyPlayer().getLife() < Player.MAX_LIFE) {
			bonus = BonusType.LIFE;
		}
		else {
			while (bonus == null) {
				bonus = BonusType.getRandom();
				if(bonus == BonusType.MOVE_BOMB && Bomb.getCanMove()) {
					bonus = null;
				}
			}
		}
		return bonus;
	}

	/**
	 * 
	 * @return la posizione del bonus
	 */
	@Override
	Point getPos() {
		return position;
	}

	/**
	 * Non necessario, se ne occupa Map
	 */
	@Override
	void move(int movement, Direction direction) {

	}

	/**
	 * Il metodo rimuove il buns dalla lista dei bonus della mappa di gioco,
	 * viene eliminato il bonus dagli oggetti osservati
	 */
	@Override
	void destroy() {
		t.stop();
		mapRef.removeFromArrayList(this);
		deleteObservers();
	}

	public void getBonus() {
		switch (type) {
			case RATE:
				Explosion.increaseRate();
				break;
			case NUMBER_BOMB:
				Bomb.increaseNumberBomb();
				break;
			case LIFE:
				mapRef.getMyPlayer().regen();
				break;
			case MOVE_BOMB:
				Bomb.setCanMove();
				break;
				
		}
		
	}
	
	/**
	 * 
	 * @return il tipo del bonus
	 */
	public BonusType getType() {
		return type;
	}

	/**
	 * @return una String mnemonica della classe di questo oggetto
	 */
	@Override
	public String toString() {
		return "BONUS";
	}

	/**
	 * Il metodo e'utilizzato per ripristinare le variabili alla condizione iniziale cancellando ogni modifica fatta
	 * 
	 */
	public static void resetStatic() {
		mapRef = null;
		
	}
}
