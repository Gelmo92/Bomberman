package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.Bonus.BonusType;
import game.Entity.Direction;


@SuppressWarnings("serial")

class MapView extends JPanel implements Observer{

	private Map mapRef = null;
	private BufferedImage playerFrontNoneImg = null;
	private BufferedImage playerFront1Img = null;
	private BufferedImage playerFront2Img = null;
	private BufferedImage playerBack1Img = null;
	private BufferedImage playerBack2Img = null;
	private BufferedImage playerDeadImg = null;
	private BufferedImage playerLeft1Img = null;
	private BufferedImage playerLeft2Img = null;
	private BufferedImage playerRight1Img = null;
	private BufferedImage playerRight2Img = null;
	private BufferedImage mobFront1Img = null;
	private BufferedImage mobFront2Img = null;
	private BufferedImage mobBack1Img = null;
	private BufferedImage mobBack2Img = null;
	private BufferedImage mobLeft1Img = null;
	private BufferedImage mobLeft2Img = null;
	private BufferedImage mobRight1Img = null;
	private BufferedImage mobRight2Img = null;
	private BufferedImage destructibleWallImg = null;
	private BufferedImage indestructibleWallImg = null;
	private BufferedImage perimetralWallImg = null;
	private BufferedImage chestImg = null;
	private BufferedImage bonusMoveBombImg = null;
	private BufferedImage bonusNumberBombImg = null;
	private BufferedImage bonusLifeImg = null;
	private BufferedImage bonusRateImg = null;
	private BufferedImage bombImg = null;
	private BufferedImage explosionImg1 = null;
	private BufferedImage explosionImg2 = null;
	private BufferedImage explosionToRender = null;
	private BufferedImage grassImg = null;
	private BufferedImage burntImg = null;
	private boolean invulnerableRender = true;
	private boolean flame = true;
	private boolean playerMovement = false;
	private Player playerRender = null;
	private JLabel lifeAndScoreLabel;
	
	public final static int CELL = 40;
	
	public MapView(Map myMap) throws IOException {
		super(new BorderLayout());
		loadImages();
		this.mapRef = myMap;
		lifeAndScoreLabel = new JLabel();
		lifeAndScoreLabel.setFont(new Font("Verdana",1,25));
		this.add(lifeAndScoreLabel);
		this.setBackground(Color.black);
		repaint();
	}
	
	private void loadImages() throws IOException {
		playerFrontNoneImg = ImageIO.read(new File("pg_front_start.png"));
		playerFront1Img = ImageIO.read(new File("pg_front_1.png"));
		playerFront2Img = ImageIO.read(new File("pg_front_2.png"));
		playerBack1Img = ImageIO.read(new File("pg_back_1.png"));
		playerBack2Img = ImageIO.read(new File("pg_back_2.png"));
		playerDeadImg = ImageIO.read(new File("pg_dead.png"));
		playerLeft1Img = ImageIO.read(new File("pg_left_1.png"));
		playerLeft2Img = ImageIO.read(new File("pg_left_2.png"));
		playerRight1Img = ImageIO.read(new File("pg_right_1.png"));
		playerRight2Img = ImageIO.read(new File("pg_right_2.png"));
		mobFront1Img = ImageIO.read(new File("Mob_front_1.png"));
		mobFront2Img = ImageIO.read(new File("Mob_front_2.png"));
		mobBack1Img = ImageIO.read(new File("Mob_back_1.png"));
		mobBack2Img = ImageIO.read(new File("Mob_back_2.png"));
		mobLeft1Img = ImageIO.read(new File("Mob_left_1.png"));
		mobLeft2Img = ImageIO.read(new File("Mob_left_2.png"));
		mobRight1Img = ImageIO.read(new File("Mob_right_1.png"));
		mobRight2Img = ImageIO.read(new File("Mob_right_2.png"));
		destructibleWallImg = ImageIO.read(new File("muro_distruttibile_2.jpg"));
		indestructibleWallImg = ImageIO.read(new File("muro_non_distruttibile_2.jpg"));
		perimetralWallImg = ImageIO.read(new File("muro_perimetrale_3.jpg"));
		chestImg = ImageIO.read(new File("chest_4.png"));
		bonusMoveBombImg = ImageIO.read(new File("bonus_move_bomb.png"));
		bonusNumberBombImg = ImageIO.read(new File("bonus_number_bomb.png"));
		bonusLifeImg = ImageIO.read(new File("bonus_life.png"));
		bonusRateImg = ImageIO.read(new File("bonus_rate.png"));
		bombImg = ImageIO.read(new File("bomb.png"));
		explosionImg1 = ImageIO.read(new File("flame_1.png"));
		explosionImg2 = ImageIO.read(new File("flame_2.png"));
		grassImg = ImageIO.read(new File("grass_3.png"));
		burntImg = ImageIO.read(new File("burnt_3.png"));
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof ActionEvent) {
			playerMovement = false;
		}
		else if (arg1 instanceof KeyEvent) {
			playerMovement = true;
		}
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for(Terrain next : mapRef.getMyTerrains()) {
			if(next.getBurnt()) {
				g.drawImage(burntImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
			}
			else {
				g.drawImage(grassImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
			}
			
		}
		if(!mapRef.getMyChests().isEmpty()) {
			g.setColor(Color.gray);
			for(Chest next : mapRef.getMyChests()) {
				g.fillRect(next.getPos().x, next.getPos().y, CELL, CELL);
			}
		}
		
		if(!mapRef.getMyBombs().isEmpty()) {
			for(Bomb next : mapRef.getMyBombs()) {
				g.drawImage(bombImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
			}
		}
		
		if(!mapRef.getMyMobs().isEmpty()) {
			for(Mob next : mapRef.getMyMobs()) {
				BufferedImage mobImg = null;
				Direction mobDir = next.getDir();
				switch(mobDir) {
					case RIGHT:
						if(next.getFoot()) {
							mobImg = mobRight2Img;
						}
						else {
							mobImg = mobRight1Img;
						}
						break;
					case DOWN:
						if(next.getFoot()) {
							mobImg = mobFront2Img;
						}
						else {
							mobImg = mobFront1Img;
						}
						break;
					case LEFT:
						if(next.getFoot()) {
							mobImg = mobLeft2Img;
						}
						else {
							mobImg = mobLeft1Img;
						}
						break;
					case UP:
						if(next.getFoot()) {
							mobImg = mobBack2Img;
						}
						else {
							mobImg = mobBack1Img;
						}
						break;
					default:
						break;
				}
				g.drawImage(mobImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
			}
		}
		
		for(Wall next : mapRef.getMyWalls()) {
			if(next.getDestroyable()) {
				g.drawImage(destructibleWallImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
			}
			else if(next.getPerimetral()) {
				g.drawImage(perimetralWallImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
			}
			else {
				g.drawImage(indestructibleWallImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
			}
		}
		
		if(!mapRef.getMyExplosion().isEmpty()) {
			if(!playerMovement) {
				if(flame) {
					explosionToRender = explosionImg1;
				}
				else {
					explosionToRender = explosionImg2;
				}
				flame = !flame;
			}
			for(Explosion next : mapRef.getMyExplosion()) {
				for(Point nextPoint : next.getPropagation()){
					g.drawImage(explosionToRender, nextPoint.x, nextPoint.y, CELL, CELL, null);
				}
			}
		}
		
		if(!mapRef.getMyChests().isEmpty()) {
			for(Chest next : mapRef.getMyChests()) {
				g.drawImage(chestImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
			}
		}
		
		if(!mapRef.getMyBonus().isEmpty()) {
			for(Bonus next : mapRef.getMyBonus()) {
				if(next.getType() == BonusType.LIFE) {
					g.drawImage(bonusLifeImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
				}
				else if(next.getType() == BonusType.MOVE_BOMB) {
					g.drawImage(bonusMoveBombImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
				}
				else if(next.getType() == BonusType.NUMBER_BOMB) {
					g.drawImage(bonusNumberBombImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
				}
				else if(next.getType() == BonusType.RATE) {
					g.drawImage(bonusRateImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
				}
			}
		}
		BufferedImage playerImg = null;
		playerRender = mapRef.getMyPlayer();
		Direction playerDir = playerRender.getDir();
		switch(playerDir) {
			case RIGHT:
				if(playerRender.getFoot()) {
					playerImg = playerRight2Img;
				}
				else {
					playerImg = playerRight1Img;
				}
				break;
			case DOWN:
				if(playerRender.getFoot()) {
					playerImg = playerFront2Img;
				}
				else {
					playerImg = playerFront1Img;
				}
				break;
			case LEFT:
				if(playerRender.getFoot()) {
					playerImg = playerLeft2Img;
				}
				else {
					playerImg = playerLeft1Img;
				}
				break;
			case UP:
				if(playerRender.getFoot()) {
					playerImg = playerBack2Img;
				}
				else {
					playerImg = playerBack1Img;
				}
				break;
			case NONE:
				playerImg = playerFrontNoneImg;
				break;
			case DEAD:
				playerImg = playerDeadImg;
				break;
		}
		if(playerRender.getInvulnerable()) {
			if(!invulnerableRender) {
				invulnerableRender = true;
			}
			else {
				g.drawImage(playerImg, playerRender.getPos().x, playerRender.getPos().y, CELL, CELL, null);
				invulnerableRender = false;
			}
		}
		else {
			g.drawImage(playerImg, playerRender.getPos().x, playerRender.getPos().y, CELL, CELL, null);
		}
		lifeAndScoreLabel.setText("<html><font color='red'>LIFE: " + playerRender.getLife() + "</font><br><br><font color='white'>SCORE: " + playerRender.getScore() + "</font></html>");
		lifeAndScoreLabel.setLocation(650, -300);
		lifeAndScoreLabel.setVisible(true);
	}

}
