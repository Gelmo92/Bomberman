package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Bonus extends Entity {

	private Point position;
	private static Map mapRef = null;
	private static final int DELAY = 20000;
	Timer t;
	
	enum BonusType {
		LIFE,
		MOVE_BOMB,
		NUMBER_BOMB,
		RATE;
		public static BonusType getRandom() {
	        return values()[(int)(Math.random() * (values().length -1)+1)];
	    }
	}
	
	private BonusType type;
	
	public Bonus(Point pos, Map map) {
		this.position = pos;
		if(mapRef == null) {
			mapRef = map;
		}
		this.type = generateRandomBonus();
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers();
							}
		  };
		  t = new Timer(DELAY, taskPerformer);
		  t.setRepeats(false);
		  t.start();
	}
	
	private static BonusType generateRandomBonus() {
		BonusType bonus = null;
		if(mapRef.myPlayer.getLife() < Player.MAX_LIFE) {
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

	@Override
	Point getPos() {
		return position;
	}

	@Override
	void move(int movement, Direction direction) {

	}

	@Override
	void destroy() {
		t.stop();
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
				mapRef.myPlayer.regen();
				break;
			case MOVE_BOMB:
				Bomb.setCanMove();
				break;
				
		}
		
	}
	
	public BonusType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "BONUS";
	}

	public static void resetStatic() {
		mapRef = null;
		
	}
}
