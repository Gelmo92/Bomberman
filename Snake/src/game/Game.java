package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Game extends Canvas implements Runnable, KeyListener
{
	
		public static final int WIDTH = 640;
		public static final int HEIGHT = 480;
		public static final String TITLE = "Snake V 1.2";
		public static boolean isRunning = false;
		public boolean paused = false;
		public boolean keyTyped = false;
		public int lastDir = 0;
		private Thread thread;
		public static Player player;
		public Fruit fruits;
		
		
	public Game()
	{
		
		Dimension dimension = new Dimension(Game.WIDTH,Game.HEIGHT);
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		
		//permetto al gioco di accorgersi della pressione dei tasti della tastiera
		addKeyListener(this);
		
		//istanzio il giocatore (la testa per lo meno)
		player = new Player(Game.WIDTH/2,Game.HEIGHT/2);
		
		//istanzio il primo frutto
		fruits = new Fruit();
	}
	
	public synchronized void start()
	{
		if(isRunning)
		{
			return;
		}
		
		isRunning = true;
		thread = new Thread(this);
		thread.start();
		
	}
	
	public synchronized void stop()
	{
		
		
		if(!isRunning)
		{
			return;
		}
		isRunning = false;
		try 
		{
			
			thread.join();
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void tick()
	{
		boolean intersected = false;
		if(!paused)
		{
			player.tick();
			intersected = fruits.tick(player.head);
			if(intersected){
				player.tail = new SnakeSection(player.tail.oldOldX,player.tail.oldOldY,player.tail);
			}
			keyTyped = false;
		}
		
	}
	
	private void render()
	{
		BufferStrategy bs = getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		player.render(g);
		fruits.render(g);
		
		g.dispose();
		bs.show();
	}
	
	@Override
	public void run() {

		requestFocus();
		
		int fps = 0;
		double timer = System.currentTimeMillis();
		long lastTime = System.nanoTime();
		double targetTick = 15.0;
		double delta = 0;
		double ns = 1000000000/targetTick;
		
		while(isRunning)
		{
			long now = System.nanoTime();
			delta +=(now - lastTime) / ns;
			lastTime = now;
			while(delta >=1)
			{
				tick();
				render();
				fps++;
				delta--;
			}
			
			if ((System.currentTimeMillis() - timer)>=1000)
			{
				//System.out.println(fps);
				fps = 0;
				timer += 1000;
			}
		}
		stop();
		
	}
	//questo non ci serve implementarlo
	@Override
	public void keyTyped(KeyEvent e) 
	{		
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT && !keyTyped)
		{
			if(!player.head.left && !paused)
			{
				player.head.up = false;
				player.head.down = false;
				player.head.right = true;
				lastDir = 1;
				keyTyped = true;
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT && !keyTyped)
		{
			if(!player.head.right && !paused)
			{
				player.head.up = false;
				player.head.down = false;
				player.head.left = true;
				lastDir = 2;
				keyTyped = true;
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_UP && !keyTyped)
		{
			if(!player.head.down && !paused)
			{
				player.head.right = false;
				player.head.left = false;
				player.head.up = true;
				lastDir = 3;
				keyTyped = true;
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN && !keyTyped)
		{
			if(!player.head.up && !paused)
			{
				player.head.right = false;
				player.head.left = false;
				player.head.down = true;
				lastDir = 4;
				keyTyped = true;
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_ESCAPE )
		{
			if (!paused && !keyTyped)
			{
				paused = true;
				player.head.right = false;
				player.head.left = false;
				player.head.up = false;
				player.head.down = false;
				keyTyped = true;
				
			}
			else if(paused && !keyTyped)
			{
				paused = false;
				if(lastDir == 1)
				{
					player.head.right = true;
				}
				if(lastDir == 2)
				{
					player.head.left = true;
				}
				if(lastDir == 3)
				{
					player.head.up = true;
				}
				if(lastDir == 4)
				{
					player.head.down = true;
				}
				keyTyped = true;
			}
			
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE )
		{
			keyTyped = false;
		}
		
	}
	
	public static void mainMethod() 
	{
		Game game = new Game();
		//crea la finestra di gioco
		JFrame frame = new JFrame(Game.TITLE);
		JTextField score = new JTextField("SCORE: " + Player.tail.counter);
		score.setBounds(10, 22, 414, 41);
		score.setBackground(Color.black);
		frame.add(score);
		frame.add(game);
		
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.toFront();
		game.keyTyped = false;
		game.start();
		
		try {
			game.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		frame.setVisible(false);
		frame.remove(game);
		
		frame.setTitle("Game Over");	
	
		JLabel lblMen = new JLabel("<html>GAME OVER <br>SCORE: "+ game.player.tail.counter + "</html>");
		lblMen.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblMen.setHorizontalAlignment(SwingConstants.CENTER);
		lblMen.setBounds(10, 22, 414, 41);
		frame.add(lblMen);
		frame.setResizable(false);
		frame.validate();;
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		
		
	}

	
	
		

}
