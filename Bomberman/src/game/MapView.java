package game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


class MapView extends JPanel implements Observer{

	
	private static final long serialVersionUID = -5792337655989052257L;

	private Map myMap = null;
	private BufferedImage backgroundImg = null;
	
	public final static int cell = 40;
	
	public MapView(Map myMap) {
		super(new BorderLayout());
		try {
			backgroundImg = ImageIO.read(new File("background.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.myMap = myMap;
		myMap.addObserver(this);
		myMap.myPlayer.addObserver(this);
		for(Mob next : myMap.myMobs) {
			next.addObserver(this);
		}
		repaint();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1!=null) {
			repaint();
		}
		
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImg, 0, 0, getWidth(), getWidth(), null);
		if(myMap.myBombs != null) {
			g.setColor(Color.red);
			for(Bomb next : myMap.myBombs) {
				g.fillRect(next.getPos().x, next.getPos().y, cell, cell);
			}
		}
		if(Map.playerAlive) {
			//g.fillRect(myMap.myPlayer.getPos().x, myMap.myPlayer.getPos().y, cell, cell);
			try {
				g.drawImage(ImageIO.read(new File("bomberman1.png")), myMap.myPlayer.getPos().x, myMap.myPlayer.getPos().y, cell, cell, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		g.setColor(Color.magenta);
		for(Mob next : myMap.myMobs) {
			g.fillRect(next.getPos().x, next.getPos().y, cell, cell);
		}
		
		
		g.setColor(Color.gray);
		for(Wall next : myMap.myWalls) {
			if(next.destroyable)
				try {
					g.drawImage(ImageIO.read(new File("muro_distruttibile.png")), next.getPos().x, next.getPos().y, cell, cell, null);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			else if(next.perimetry)
				try {
					g.drawImage(ImageIO.read(new File("muro_perimetrale.png")), next.getPos().x, next.getPos().y, cell, cell, null);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			else
				try {
					g.drawImage(ImageIO.read(new File("muro_non_distruttibile.png")), next.getPos().x, next.getPos().y, cell, cell, null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		if(myMap.myExplosion.size() != 0) {
			g.setColor(Color.yellow);
			for(Explosion next : myMap.myExplosion) {
				for(Point nextPoint : next.propagation){
					g.fillOval(nextPoint.x, nextPoint.y, cell, cell);
				}
				
			}
		}
	}
	
	/*@Override
	public void paint(Graphics g) {
		//disegna a schermo
		g.setColor(Color.green);
		if(Map.playerAlive) {
			g.fillRect(myMap.myPlayer.getPos().x, myMap.myPlayer.getPos().y, cell, cell);
		}
		g.setColor(Color.magenta);
		g.fillRect(myMap.myMobs.get(0).getPos().x, myMap.myMobs.get(0).getPos().y, cell, cell);
		g.setColor(Color.black);
		for(Wall next : myMap.myWalls) {
			g.fillRect(next.getPos().x, next.getPos().y, cell, cell);
		}
	}*/

}
