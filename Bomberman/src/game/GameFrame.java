package game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

public class GameFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8213639328882558634L;
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	private Menu myMenu;

	public GameFrame(Map myMap, Menu menu) {
		myMenu = menu;
		MapView myMapView = new MapView(myMap);
		Controller myController = new Controller(myMap, myMapView);
		addKeyListener(myController);
		
		Container cp = getContentPane();
		 cp.setLayout(new BorderLayout());	
		 cp.add(myMapView, BorderLayout.CENTER);	
		 this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		 setTitle("Bomberman");
		 setSize(1000, 1000);;
		 setVisible(true);	
		 addWindowListener(new WindowAdapter()
	        {
	            @Override
	            public void windowClosing(WindowEvent e)
	            {
	                myMenu.reset();
	            }
	        });
	}
}
