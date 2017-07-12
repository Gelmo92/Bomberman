package game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Questa classe crea e imposta il JFrame che conterra' la riproduzione
 * grafica della partita.
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 *
 */
class GameFrame extends JFrame{

	private static final long serialVersionUID = 2782192563128178112L;
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 720;
	private Menu menuRef;
	private Controller myController;

	/**
	 * Crea un oggetto di tipo Map, uno di tipo Controller e uno di tipo MapView
	 * (Modello, Controllo e Vista).
	 * Genera un oggetto Container per contenere il pannello grafico di MapView.
	 * 
	 * @param menu il menu iniziale che crea questo oggetto
	 * @throws FileNotFoundException se non esiste il file per creare Map
	 * @throws IOException se non esiste una immagine che dovrebbe essere
	 * caricata da MapView
	 */
	GameFrame(Menu menu) throws FileNotFoundException, IOException{
		menuRef = menu;
		Map myMap = new Map();
		MapView myMapView = new MapView(myMap);
		myController = new Controller(myMap, myMapView, this);
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

	/**
	 * Informa l'utente della fine della partita e del suo esito.
	 * Ferma il timer del Controller e reimposta il menu.
	 * 
	 * @param playerAlive true se l'utente ha vinto, false altrimenti
	 */
	void gameOver(boolean playerAlive) {
		Thread gameOver = new Thread(new Runnable(){//Creiamo il frame di game over in un nuovo thread per non bloccare l'esecuzione del gioco, fino alla chiusura del suddetto frame
	        public void run(){
	        	if(!(boolean)playerAlive) {
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
