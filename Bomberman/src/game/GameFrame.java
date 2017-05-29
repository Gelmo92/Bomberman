package game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

public class GameFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8213639328882558634L;

	public GameFrame(Map myMap, Player myPlayer, ArrayList<Mob> myMobs) {
		MapView myMapView = new MapView(myMap, myPlayer, myMobs);
		myMap.addObserver(myMapView);
		myPlayer.addObserver(myMapView);
		for(int nMob = 0; nMob < Map.nMobs; nMob++) {
			myMobs.iterator().next().addObserver(myMapView);
		}
		Container cp = getContentPane();
		 cp.setLayout(new BorderLayout());	
		 cp.add(myMapView, BorderLayout.CENTER);	
		 setTitle("Bomberman");
		 setSize(1000, 1000);;
		 setVisible(true);	
	}
	
	
	public static void main(String[] args) {
		Map myMap = new Map();
		Player myPlayer = new Player(myMap.firstPlayerPos);
		ArrayList<Mob> myMobs = null;
		Iterator<Point> firstMobPos = myMap.firstMobsPos.iterator();
		for(int nMob = 0; nMob < Map.nMobs; nMob++) {
			myMobs.add(new Mob(firstMobPos.next()));
		}
		GameFrame myGameFrame = new GameFrame(myMap, myPlayer, myMobs);
	}

}
