package game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

public class GameFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8213639328882558634L;

	public GameFrame(Map myMap) {
		MapView myMapView = new MapView(myMap);
		Controller myController = new Controller(myMap, myMapView);
		addKeyListener(myController);
		
		Container cp = getContentPane();
		 cp.setLayout(new BorderLayout());	
		 cp.add(myMapView, BorderLayout.CENTER);	
		 setDefaultCloseOperation(EXIT_ON_CLOSE);
		 setTitle("Bomberman");
		 setSize(1000, 1000);;
		 setVisible(true);	
	}
	
	
	public static void main(String[] args) {
		Map myMap = null;
		try {
			myMap = new Map();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GameFrame myGameFrame = new GameFrame(myMap);
	}

}
