package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

class Player extends Entity {
	private Point position;
	private Point nextPos;
	private static Map mapRef = null;
	private int life;
	private int score;
	public final static int MAX_LIFE = 3;
	private static final int DELAY = 3100;  //milliseconds
	Timer t;
	private boolean invulnerable = false;
	private boolean leftFoot = true;
	
	private Direction direction = Direction.NONE;
	
	public Player(Point firstPlayerPos, Map map) {
		position = firstPlayerPos;
		this.life = MAX_LIFE;
		this.score = 0;
		if(mapRef == null) {
			mapRef = map;
		}
	}

	@Override
	Point getPos() {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	void move(int movement, Direction dir) {
		nextPos = new Point(position);
		switch(dir) {
		case RIGHT:
			nextPos.x += movement;//*MapView.cell;
			break;
		case DOWN:
			nextPos.y += movement;//*MapView.cell;
			break;
		case LEFT:
			nextPos.x -= movement;//*MapView.cell;
			break;
		case UP:
			nextPos.y -= movement;//*MapView.cell;
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
				this.direction = Direction.DEAD;
				this.destroy();
			}
			else {
				setInvulnerable();
				ActionListener taskPerformer = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						setInvulnerable();
						mapRef.canMove(position, mapRef.myPlayer);
									}
				  };
				  t = new Timer(DELAY, taskPerformer);
				  t.setRepeats(false);
				  t.start();
			}
		}
		
	}

	@Override
	Point destroy() {
		Map.playerAlive = false;
		setChanged();
		notifyObservers();
		deleteObservers();
		return null;
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
		if(invulnerable) {
			invulnerable = false;
		}
		else {
			invulnerable = true;
		}
	}

	public Direction getDir() {
		return this.direction;
	}
	
	public boolean getFoot() {
		return this.leftFoot;
	}
	private void setFoot() {
		if(leftFoot) {
			leftFoot = false;
		}
		else {
			leftFoot = true;
		}
	}

	public int getScore() {
		return this.score;
	}
	
	public void addScore(Entity obj) {
		if(obj instanceof Wall) {
			this.score += 50;
		}
		else if(obj instanceof Mob) {
			this.score += 200;
		}
		else if(obj instanceof Bonus) {
			this.score += 100;
		}
		else if(obj instanceof Chest) {
			this.score += 50;
		}
	}
	
}
