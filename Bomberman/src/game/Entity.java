package game;

import java.awt.Point;
import java.util.Observable;

abstract class Entity extends Observable{
	
	abstract Point getPos();
	abstract void move(int movement, int direction);
	abstract void destroy();
}
