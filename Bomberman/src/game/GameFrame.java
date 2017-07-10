package game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")

public class GameFrame extends JFrame implements Observer{

	public static final int WIDTH = 1000;
	public static final int HEIGHT = 720;
	private Menu menuRef;
	private Map myMap;
	private Controller myController;

	public GameFrame(Menu menu) throws FileNotFoundException, IOException{
		menuRef = menu;
		myMap = new Map();
		MapView myMapView = new MapView(myMap);
		myController = new Controller(myMap, myMapView);
		myController.addObserver(this);
		addKeyListener(myController);//myController gestirà la pressione dei tasti della tastiera
		
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());	
		cp.add(myMapView, BorderLayout.CENTER);	
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Bomberman");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);	
		addWindowListener(new WindowAdapter()//Gestiamo la chiusura della finestra da parte dell'utente
	        {
	            @Override
	            public void windowClosing(WindowEvent e)
	            {
	                myController.stopT();
	                myController.deleteObservers();
	            	menu.reset();
	            }
	        });
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof Boolean) {//Non gestisce i tick del timer, ma solo il caso in cui il Controller richieda di terminare la partita dal suo metodo "update"
			Thread gameOver = new Thread(new Runnable(){//Creiamo il frame di game over in un nuovo thread per non bloccare l'esecuzione del gioco, fino alla chiusura del suddetto frame
		        public void run(){
		        	if(!(boolean)arg) {
						JOptionPane.showMessageDialog(new JFrame(),
							    "Sei morto!",
							    "GAME OVER",
							    JOptionPane.INFORMATION_MESSAGE);//Questo metodo crea un frame di dialogo "bloccante"
					}
					else {
						JOptionPane.showMessageDialog(new JFrame(),
							    "Complimenti hai vinto!",
							    "GAME OVER",
							    JOptionPane.INFORMATION_MESSAGE);//Questo metodo crea un frame di dialogo "bloccante"
						
					}
		        	myController.stopT();
		        	myController.deleteObservers();
					menuRef.reset();
					GameFrame.this.dispose();
		        }
		    });
		  gameOver.start();
			
			
		}
		
		
	}
}
