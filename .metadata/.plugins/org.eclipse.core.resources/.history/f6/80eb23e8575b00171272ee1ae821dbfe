package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

class Player extends Entity {
	private Point position;
	private Point nextPos;
	private Map mapRef;
	private int life;
	private final static int MAX_LIFE = 3;
	private static final int DELAY = 3000;  //milliseconds
	Timer t;
	private boolean invulnerable = false;
	private boolean leftFoot = true;
	
	private Direction direction = Direction.NONE;
	
	public Player(Point firstPlayerPos, Map mapRef) {
		position = firstPlayerPos;
		this.life = MAX_LIFE;
		this.mapRef = mapRef;
		
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
		if(mapRef.canMove(nextPos, this)) {
			position = nextPos;
			this.direction = dir;
			setFoot();
		}
	}
	
	void harm() {
		if(!this.invulnerable) {
			this.life--;
			if (this.life == 0) {
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
}
