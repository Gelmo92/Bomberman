package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * La classe Player gestisce il giocatore nella mappa di gioco
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 */
class Player extends Entity {
	private Point position;
	private Point nextPos;
	private Map mapRef = null;
	private int life;
	private int score;
	public static final int MAX_LIFE = 3;
	private static final int DELAY = 3100;  
	private Timer t;
	private boolean invulnerable = false;
	private boolean leftFoot = true;
	
	private Direction direction = Direction.NONE;
	
	/**
	 * Il costruttore crea un oggetto di tipo Player.
	 * Inoltre aggiunge map come suo Observer.
	 * 
	 * @param firstPlayerPos e' la posizione iniziale del giocatore
	 * @param map e' il riferimento alla mappa di gioco
	 */
	public Player(Point firstPlayerPos, Map map) {
		position = firstPlayerPos;
		addObserver(map);
		this.life = MAX_LIFE;
		this.score = 0;
		if(mapRef == null) {
			mapRef = map;
		}
	}

	/**
	 * @return le coordinate del giocatore
	 */
	@Override
	Point getPos() {
		return position;
	}

	/**
	 * Il metodo gestice il movimento del giocatore
	 */
	@Override
	void move(int movement, Direction dir) {
		nextPos = new Point(position);
		switch(dir) {
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
		if(this.direction != Direction.DEAD) {
			this.direction = dir;
			setFoot();
		}
		if(mapRef.canMove(nextPos, this)) {
			position = nextPos;
		}
	}
	
	/**
	 * il metodo gestisce le ferite del giocatore
	 * Se il giocatore e' invulnerabile allora non subisce ferite, altrimenti perde una vita e attiva l'invulnerabilita'
	 * Se il giocatore perde l'ultima vita allora il metodo invoca la distruzione del giocatore
	 * 
	 * @see Player#invulnerable()
	 */
	void harm() {
		if(!this.invulnerable) {
			
			this.life--;
			if (this.life == 0) {
				this.destroy();
			}
			else if(this.life > 0){
				setInvulnerable();
				ActionListener invulnerabilityPerformer = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						setInvulnerable();
						mapRef.canMove(position, Player.this);//Controllo se alla fine dell'invulnerabilità devo rifare danno al player perchè potrebbe essere fermo su un esplosione ad esempio
									}
				  };
				  t = new Timer(DELAY, invulnerabilityPerformer);
				  t.setRepeats(false);
				  t.start();
			}
		}
		
	}

	/**
	 * Imposta la direzione del giocatore a DEAD, lo rende invulnerabile per evitare
	 * che la sua vita possa diventare negativa, notifica agli Observer la propria morte.
	 * Infine cancella i propri Observer.
	 */
	@Override
	void destroy() {
		mapRef.setPlayerAlive(false);
		this.direction = Direction.DEAD;
		setInvulnerable();
		setChanged();
		notifyObservers();
		deleteObservers();
	}

	/**
	 * il metodo cura una vita al giocatore aumentando di 1 il valore di life
	 */
	public void regen() {
		if(this.life < MAX_LIFE) {
			life++;
		}
		
	}

	/**
	 * 
	 * @return il numero di vite del giocatore
	 */
	public int getLife() {
		return this.life;
	}
	
	/**
	 * 
	 * @return il valore che rappresenta l'invulnerabilita o no del giocatore
	 */
	public boolean getInvulnerable() {
		return invulnerable;
	}
	
	/**
	 * il metodo alterna il valore di invulnerable 
	 */
	void setInvulnerable() {
		invulnerable = !invulnerable;
	}

	/**
	 * 
	 * @return la direzione del giocatore
	 */
	public Direction getDir() {
		return this.direction;
	}
	
	/**
	 * 
	 * @return il valore che rappresenta il passo sinistro o quello destro
	 */
	public boolean getFoot() {
		return this.leftFoot;
	}
	
	/**
	 * il metodo alterna il passo sinistro a quello destro
	 */
	private void setFoot() {
		leftFoot = !leftFoot;
	}

	/**
	 * 
	 * @return il valore del punteggio del giocatore
	 */
	public int getScore() {
		return this.score;
	}
	
	/**
	 * Il metodo mantiene aggiornato il punteggio del giocatore
	 * Ogni volta che il giocatore raccoglie un bonus o distrugge un muro o  una cassa o un mostro
	 * il punteggio viene incrementato 
	 * 
	 * @param obj e' l'oggetto che causa l'incremento del punteggio
	 */
	public void addScore(Entity obj) {
		switch(obj.toString()) {
			case "BONUS":
				this.score += 100;
				break;
			case "CHEST":
				this.score += 50;
				break;
			case "MOB":
				this.score += 200;
				break;
			case "WALL":
				this.score += 50;
				break;
		}
	}

	/**
	 * @return una String mnemonica della classe di questo oggetto
	 */
	@Override
	public String toString() {
		return "PLAYER";
	}

	
}
