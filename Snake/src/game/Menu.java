package game;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Menu implements ActionListener
{
	private JFrame menu;
	private Game game;
	
	public Menu() 
	{
		menu = new JFrame("Snake Menu");
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setSize(Game.WIDTH, Game.HEIGHT);
		menu.setResizable(false);
		menu.setLocationRelativeTo(null);
		menu = setUpMenu(menu);
		menu.setVisible(true);
		
		
	}
	
	private JFrame setUpMenu(JFrame menu)
	{
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, Game.WIDTH, Game.HEIGHT);
		JButton startGame = new JButton("START");
		startGame.setFont(new Font("Tahoma", Font.BOLD, 16));
		JLabel Author = new JLabel("Snake Rebuild by Gelmo92 V 1.2");
		Author.setFont(new Font("Tahoma", Font.BOLD, 14));
		startGame.addActionListener(this);
		panel.add(startGame);
		startGame.setBounds(Game.WIDTH/2 - 130/2 , Game.HEIGHT/2 - 60/2 , 130, 60);
		panel.add(Author);
		Author.setBounds(Game.WIDTH/2 - 500/5 ,Game.HEIGHT - 60*2 , 500, 60);
		menu.add(panel);
		return menu;
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
		JButton pressedButton = (JButton) e.getSource();
		if (pressedButton.getText() == "START") 
		{
		    menu.setVisible(false);
			Thread thread = new Thread() 
		    {
		        public void run() 
		        {
		        	Game.mainMethod();;
		        }
		    };
		    thread.start();
		}

	}

}
