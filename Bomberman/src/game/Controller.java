package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

class Controller implements KeyListener{

	private Map myMap;
	private MapView myMapView;
	//private static boolean move = true;
	
	public Controller(Map myMap, MapView myMapView) {
		this.myMap = myMap;
		this.myMapView = myMapView;
		int delay = 500; //milliseconds
		  ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(Mob next : myMap.myMobs) {
					next.move(MapView.cell, 0);
				}
				myMapView.update(myMap, e);
				//Controller.move = true;
				
			}
		  };
		  new Timer(delay, taskPerformer).start();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(/*!move ||*/ !Map.playerAlive) return;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_RIGHT: 
			myMap.myPlayer.move(MapView.cell, 1);
			break;
		case KeyEvent.VK_DOWN: 
			myMap.myPlayer.move(MapView.cell, 2);
			break;
		case KeyEvent.VK_LEFT: 
			myMap.myPlayer.move(MapView.cell, 3);
			break;
		case KeyEvent.VK_UP: 
			myMap.myPlayer.move(MapView.cell, 4);
			break;
		case KeyEvent.VK_SPACE:
			myMap.dropBomb();
			myMap.myBombs.get(myMap.myBombs.size()-1).addObserver(myMapView);
			break;
		}
		myMapView.update(myMap, e);
		//move = false;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	

}
