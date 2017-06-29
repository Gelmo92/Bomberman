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
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	public Menu() 
	{
		menu = new JFrame("Bomberman Menu");
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setSize(WIDTH, HEIGHT);
		menu.setResizable(false);
		menu.setLocationRelativeTo(null);
		menu = setUpMenu(menu);
		menu.setVisible(true);
		
		
	}
	
	private JFrame setUpMenu(JFrame menu)
	{
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, WIDTH, HEIGHT);
		JButton startGame = new JButton("START");
		startGame.setFont(new Font("Tahoma", Font.BOLD, 16));
		JLabel Author = new JLabel("Bomberman by Gelmotto-Pidello");
		Author.setFont(new Font("Tahoma", Font.BOLD, 14));
		startGame.addActionListener(this);
		panel.add(startGame);
		startGame.setBounds(WIDTH/2 - 130/2 , HEIGHT/2 - 60/2 , 130, 60);
		panel.add(Author);
		Author.setBounds(WIDTH/2 - 500/5 ,HEIGHT - 60*2 , 500, 60);
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
		}
		
	}
	public void reset() {
		this.menu.setVisible(true);
	}
}
