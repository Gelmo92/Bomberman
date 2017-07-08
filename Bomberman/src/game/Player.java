package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

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
	
	
	public Player(Point firstPlayerPos, Map map) {
		position = firstPlayerPos;
		addObserver(map);
		this.life = MAX_LIFE;
		this.score = 0;
		if(mapRef == null) {
			mapRef = map;
		}
	}

	@Override
	Point getPos() {
		return position;
	}

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

	@Override
	void destroy() {
		mapRef.setPlayerAlive(false);
		this.direction = Direction.DEAD;
		setInvulnerable();
		setChanged();
		notifyObservers();
		deleteObservers();
	}

	public void regen() {
		if(this.life < MAX_LIFE) {
			life++;
		}
		
	}

	public int getLife() {
		return this.life;
	}
	
	public boolean getInvulnerable() {
		return invulnerable;
	}
	
	private void setInvulnerable() {
		invulnerable = !invulnerable;
	}

	public Direction getDir() {
		return this.direction;
	}
	
	public boolean getFoot() {
		return this.leftFoot;
	}
	private void setFoot() {
		leftFoot = !leftFoot;
	}

	public int getScore() {
		return this.score;
	}
	
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

	@Override
	public String toString() {
		return "PLAYER";
	}

	
}
