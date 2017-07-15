package game;

import java.awt.Point;
import java.util.Observable;
/**
 * la classe Entity e' una classe astratta per tutti gli oggetti che saranno utilizzati per il modello (escluso Map)
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 */
abstract class Entity extends Observable{
	
	enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT,
		NONE,
		DEAD;
		
		/**
		 * Il metodo genera un movimento casuale 
		 * 
		 * @return un valore randomico tra i valori di Direction escludendo il valore NONE e il valore DEAD
		 */
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
