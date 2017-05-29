package game;

import java.awt.Point;
import java.util.Observable;

abstract class Entity extends Observable{
	
	abstract Point getPos();
	abstract void move(Point newPosition);
	abstract void destroy();
}
