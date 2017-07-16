package game;

import java.awt.Point;
import java.util.Observable;
/**
 * la classe Entity e' una classe astratta per tutti gli oggetti che saranno utilizzati per il modello (esclusi Map e Terrain)
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
		static Direction getRandom() {
	        return values()[(int) (Math.random() * (values().length -2))];//Scelta randomica tra tutti i valori meno NONE e DEAD
	    }
	}
	
	/**
	 * 
	 * @return la posizione di questa entita'.
	 */
	abstract Point getPos();
	
	/**
	 * 
	 * @param movement la distanza a cui muoversi
	 * @param dir la direzione in cui muoversi
	 */
	abstract void move(int movement, Direction dir);
	
	/**
	 * Distrugge l'entita'.
	 */
	abstract void destroy();
	
	@Override
	public abstract String toString();
}
