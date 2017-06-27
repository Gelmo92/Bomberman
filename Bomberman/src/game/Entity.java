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
	}
	
	abstract Point getPos();
	abstract void move(int movement, Direction dir);
	abstract Point destroy();
}
