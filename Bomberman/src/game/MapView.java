package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;


class MapView extends Canvas implements Observer{

	
	private static final long serialVersionUID = -5792337655989052257L;

	private Map myMap = null;
	
	public final static int cell = 40;
	
	public MapView(Map myMap) {
		super();
		this.myMap = myMap;
		myMap.addObserver(this);
		myMap.myPlayer.addObserver(this);
		for(Mob next : myMap.myMobs) {
			next.addObserver(this);
		}
		repaint();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1!=null) {
			repaint();
		}
		
		
	}
	
	@Override
	public void paint(Graphics g) {
		//disegna a schermo
		g.setColor(Color.green);
		if(Map.playerAlive) {
			g.fillRect(myMap.myPlayer.getPos().x, myMap.myPlayer.getPos().y, cell, cell);
		}
		g.setColor(Color.magenta);
		g.fillRect(myMap.myMobs.get(0).getPos().x, myMap.myMobs.get(0).getPos().y, cell, cell);
		g.setColor(Color.black);
		for(Wall next : myMap.myWalls) {
			g.fillRect(next.getPos().x, next.getPos().y, cell, cell);
		}
	}

}
