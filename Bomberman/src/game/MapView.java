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
	private BufferedImage playerImg = null;
	private BufferedImage mobImg = null;
	private BufferedImage destructibleWallImg = null;
	private BufferedImage indestructibleWallImg = null;
	private BufferedImage perimetralWallImg = null;
	private BufferedImage chestImg = null;
	private BufferedImage bombImg = null;
	private BufferedImage explosionImg1 = null;
	private BufferedImage explosionImg2 = null;
	private boolean flame = true;
	
	public final static int cell = 40;
	
	public MapView(Map myMap) {
		super(new BorderLayout());
		loadImages();
		this.myMap = myMap;
		myMap.addObserver(this);
		myMap.myPlayer.addObserver(this);
		for(Mob next : myMap.myMobs) {
			next.addObserver(this);
		}
		repaint();
	}
	
	private void loadImages() {
		try {
			backgroundImg = ImageIO.read(new File("background.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			playerImg = ImageIO.read(new File("bomberman1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		try {
			mobImg = ImageIO.read(new File(""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		try {
			destructibleWallImg = ImageIO.read(new File("muro_distruttibile.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			indestructibleWallImg = ImageIO.read(new File("muro_non_distruttibile.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			perimetralWallImg = ImageIO.read(new File("muro_perimetrale.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		try {
			chestImg = ImageIO.read(new File(""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		try {
			bombImg = ImageIO.read(new File("bomb.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			explosionImg1 = ImageIO.read(new File("flame_1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			explosionImg2 = ImageIO.read(new File("flame_2.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		g.setColor(Color.green);
		for(Terrain next : Map.myTerrain) {
			g.fillRect(next.getPos().x, next.getPos().y, cell, cell);
		}
		if(myMap.myChests != null) {
			g.setColor(Color.gray);
			for(Chest next : myMap.myChests) {
				g.fillRect(next.getPos().x, next.getPos().y, cell, cell);
			}
		}
		//g.drawImage(backgroundImg, 0, 0, getWidth(), getWidth(), null);
		if(myMap.myBombs.size() != 0) {
			for(Bomb next : myMap.myBombs) {
				g.drawImage(bombImg, next.getPos().x, next.getPos().y, cell, cell, null);
			}
		}
		if(Map.playerAlive) {
			//g.fillRect(myMap.myPlayer.getPos().x, myMap.myPlayer.getPos().y, cell, cell);
			g.drawImage(playerImg, myMap.myPlayer.getPos().x, myMap.myPlayer.getPos().y, cell, cell, null);
			
		}
		g.setColor(Color.magenta);
		for(Mob next : myMap.myMobs) {
			g.fillRect(next.getPos().x, next.getPos().y, cell, cell);
		}
		
		
		for(Wall next : myMap.myWalls) {
			if(next.destroyable) {
				g.drawImage(destructibleWallImg, next.getPos().x, next.getPos().y, cell, cell, null);
			}
			else if(next.perimetry) {
				g.drawImage(perimetralWallImg, next.getPos().x, next.getPos().y, cell, cell, null);
			}
			else {
				g.drawImage(indestructibleWallImg, next.getPos().x, next.getPos().y, cell, cell, null);
			}
		}
		
		if(myMap.myExplosion.size() != 0) {
			BufferedImage explosionToRender = null;
			if(flame) {
				explosionToRender = explosionImg1;
				flame = false;
			}
			else {
				explosionToRender = explosionImg2;
				flame = true;
			}
			for(Explosion next : myMap.myExplosion) {
				for(Point nextPoint : next.propagation){
					g.drawImage(explosionToRender, nextPoint.x, nextPoint.y, cell, cell, null);
				}
			}
		}
		if(myMap.myBonus.size() != 0) {
			g.setColor(Color.blue);
			for(Bonus next : myMap.myBonus) {
				g.fillRect(next.getPos().x, next.getPos().y, cell, cell);
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
