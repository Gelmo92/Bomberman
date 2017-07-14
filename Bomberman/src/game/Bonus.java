package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

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
		public static BonusType getRandom() {
	        return values()[(int)(Math.random() * (values().length -1)+1)];//Scelta randomica tra tutti i valori meno LIFE
	    }
	}
	
	private BonusType type;
	
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
