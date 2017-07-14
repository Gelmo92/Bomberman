package game;

import java.awt.Point;
import java.util.Observable;

abstract class Entity extends Observable{
	
	enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT,
		NONE,
		DEAD;
		public static Direction getRandom() {
	        return values()[(int) (Math.random() * (values().length -2))];//Scelta randomica tra tutti i valori meno NONE e DEAD
	    }
	}
	
	abstract Point getPos();
	abstract void move(int movement, Direction dir);
	abstract void destroy();
	
	@Override
	public abstract String toString();
}
