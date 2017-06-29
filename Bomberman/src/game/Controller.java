package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.Timer;

import game.Entity.Direction;

class Controller implements KeyListener{

	private Map myMap;
	private MapView myMapView;
	private final static int DELAY = 500;
	public static Timer t;
	static ActionListener taskPerformer;
	//private static boolean move = true;
	
	public Controller(Map myMap, MapView myMapView) {
		this.myMap = myMap;
		this.myMapView = myMapView;
		 	taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*ArrayList<Mob> mobsList = new ArrayList<Mob>(Map.myMobs);
				for(Mob next : mobsList) {
					next.move(MapView.cell, Direction.NONE);
				}*/
				myMapView.update(myMap, e);
				//Controller.move = true;
				
			}
		  };
		  t = new Timer(DELAY, taskPerformer);
		  myMap.synchMobs();
		  t.start();
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
			myMap.myPlayer.move(MapView.cell, Direction.RIGHT);
			break;
		case KeyEvent.VK_DOWN: 
			myMap.myPlayer.move(MapView.cell, Direction.DOWN);
			break;
		case KeyEvent.VK_LEFT: 
			myMap.myPlayer.move(MapView.cell, Direction.LEFT);
			break;
		case KeyEvent.VK_UP: 
			myMap.myPlayer.move(MapView.cell, Direction.UP);
			break;
		case KeyEvent.VK_SPACE:
			myMap.dropBomb();
			//myMap.myBombs.get(myMap.myBombs.size()-1).addObserver(myMapView);
			break;
		}
		myMapView.update(myMap, e);
		//move = false;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	static void gameOver() {
		//t.removeActionListener(taskPerformer);
		t.stop();
	}

}
