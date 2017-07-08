package game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")

public class GameFrame extends JFrame implements Observer{

	public static final int WIDTH = 1000;
	public static final int HEIGHT = 800;
	private Menu myMenu;
	private Controller myController;

	public GameFrame(Map myMap, Menu menu) throws IOException {
		myMenu = menu;
		MapView myMapView = new MapView(myMap);
		myController = new Controller(myMap, myMapView);
		myController.addObserver(this);
		addKeyListener(myController);
		
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());	
		cp.add(myMapView, BorderLayout.CENTER);	
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Bomberman");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);	
		addWindowListener(new WindowAdapter()
	        {
	            @Override
	            public void windowClosing(WindowEvent e)
	            {
	                myController.stopT();
	            	myMenu.reset();
	            }
	        });
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof Boolean) {
			Thread gameOver = new Thread(new Runnable(){
		        public void run(){
		        	if(!(boolean)arg) {
						JOptionPane.showMessageDialog(new JFrame(),
							    "Sei morto!",
							    "GAME OVER",
							    JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(new JFrame(),
							    "Complimenti hai vinto!",
							    "GAME OVER",
							    JOptionPane.INFORMATION_MESSAGE);
						
					}
		        	myController.stopT();
					myMenu.reset();
					GameFrame.this.dispose();
		        }
		    });
		  gameOver.start();
			
			
		}
		
		
	}
}
