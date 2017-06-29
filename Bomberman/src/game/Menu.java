package game;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Menu implements ActionListener
{
	private JFrame menu;
	private GameFrame gameFrame;
	private Map myMap = null;
	
	public Menu() 
	{
		menu = new JFrame("Bomberman Menu");
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setSize(GameFrame.WIDTH, GameFrame.HEIGHT);
		menu.setResizable(false);
		menu.setLocationRelativeTo(null);
		menu = setUpMenu(menu);
		menu.setVisible(true);
		
		
	}
	
	private JFrame setUpMenu(JFrame menu)
	{
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);
		JButton startGame = new JButton("START");
		startGame.setFont(new Font("Tahoma", Font.BOLD, 16));
		JLabel Author = new JLabel("Bomberman by Gelmotto-Pidello");
		Author.setFont(new Font("Tahoma", Font.BOLD, 14));
		startGame.addActionListener(this);
		panel.add(startGame);
		startGame.setBounds(GameFrame.WIDTH/2 - 130/2 , GameFrame.HEIGHT/2 - 60/2 , 130, 60);
		panel.add(Author);
		Author.setBounds(GameFrame.WIDTH/2 - 500/5 ,GameFrame.HEIGHT - 60*2 , 500, 60);
		menu.add(panel);
		return menu;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton pressedButton = (JButton) e.getSource();
		if (pressedButton.getText() == "START") 
		{
		    menu.setVisible(false);
		    
			try {
				myMap = new Map();
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			gameFrame = new GameFrame(myMap, this);
			/*Thread thread = new Thread() 
		    {
		        public void run() 
		        {
		        	Game.mainMethod();;
		        }
		    };
		    thread.start();*/
		}
		
	}
	public void reset() {
		this.menu.setVisible(true);
	}
}
