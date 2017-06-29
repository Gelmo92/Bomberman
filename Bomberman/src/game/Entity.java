package game;

import java.awt.Point;
import java.util.Observable;

import game.Bonus.BonusType;

abstract class Entity extends Observable{
	
	enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT,
		NONE,
		DEAD;
		public static Direction getRandom() {
	        return values()[(int) (Math.random() * (values().length -2))];
	    }
	}
	
	abstract Point getPos();
	abstract void move(int movement, Direction dir);
	abstract Point destroy();
}
