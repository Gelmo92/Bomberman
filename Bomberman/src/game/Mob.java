package game;

import java.awt.Point;
/**
 * La classe Mob rappresenta i mostri posizionati sulla mappa di gioco
 * I mostri possono muoversi e devono essere sconfitti dal giocatore
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 */
class Mob extends Entity{
	
	private Point position;
	private Point nextPos;
	private Direction direction = Direction.NONE;
	private boolean leftFoot = true;
	private static Map mapRef = null;
	
	/**
	 * Il metodo costruttore crea gli oggetti mob alla posizione iniziale e li rende osservabili
	 * 
	 * @param firstMobPos e' la posizione iniziale del mob
	 * @param map e' il riferimento alla mappa di gioco
	 */
	public Mob(Point firstMobPos, Map map) {
		position = firstMobPos;
		addObserver(map);
		if(mapRef == null) {
			mapRef = map;
		}
	}

	/**
	 * @return le coordinate del mob
	 */
	@Override
	Point getPos() {
		return position;
	}

	/**
	 * Il metodo gestisce il movimento in modo randomico e invoca il metodo per alternare il passo sinistro da quello destro
	 */
	@Override
	void move(int movement, Direction direction) {
		nextPos = new Point(position);
		Direction newDirection = Direction.getRandom();
		switch(newDirection) {
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
		this.direction = newDirection;
		setFoot();
		if(mapRef.canMove(nextPos, this)) {
			position = nextPos;
		}
	}
	
	/**
	 * 
	 * @return la direzione del mob
	 */
	public Direction getDir() {
		return this.direction;
	}
	
	/**
	 * 
	 * @return il valore per indicare se il mob ha fatto un passo sinistro o destro
	 */
	public boolean getFoot() {
		return this.leftFoot;
	}
	
	/**
	 * Il metodo alterna il valore di leftFoot
	 */
	private void setFoot() {
		leftFoot = !leftFoot;
	}
	
	@Override
	void destroy() {
		mapRef.removeFromArrayList(this);
		this.direction = Direction.DEAD;
		setChanged();
		notifyObservers();
		deleteObservers();
	}
	
	/**
	 * @return una String mnemonica della classe di questo oggetto
	 */
	@Override
	public String toString() {
		return "MOB";
	}

	/**
	 * Il metodo e'utilizzato per ripristinare le variabili alla condizione iniziale cancellando ogni modifica fatta
	 * 
	 */
	public static void resetStatic() {
		mapRef = null;
		
	}
}
