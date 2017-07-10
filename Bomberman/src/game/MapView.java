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

/**
 * La classe MapView si occupa della rappresentazione grafica della partita in corso.
 * Per ogni partita viene creato un solo oggetto MapView.
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 *
 */
class MapView extends JPanel implements Observer{

	private static final long serialVersionUID = -3861927029592271534L;
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
	private BufferedImage playerToRenderImg = null;
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
	private Player playerToRender = null;
	private JLabel lifeAndScoreLabel;
	public final static int CELL = 40;
	
	/**
	 * Genera le immagini da mostrare a schermo partendo da diversi file immagine.
	 * Mostra inoltre vita del giocatore e punteggio della partita in corso.
	 * Ridisegna tutti gli elementi ad ogni tick del timer del Controller.
	 * 
	 * @param myMap indica l'oggetto di tipo Map che contiene i riferimenti agli oggetti
	 * da disegnare
	 * @throws IOException nel caso in cui il nome di una skin da generare sia errato
	 */
	MapView(Map map) throws IOException {
		super(new BorderLayout());
		loadImages();
		this.mapRef = map;
		playerToRender = mapRef.getMyPlayer();//Evitiamo di riscrivere troppe volte mapRef.getMyPlayer()
		lifeAndScoreLabel = new JLabel();
		lifeAndScoreLabel.setFont(new Font("Verdana",1,25));
		this.add(lifeAndScoreLabel);
		this.setBackground(Color.black);
		repaint();
	}
	
	/**
	 * Carica le immagini delle skin in memoria partendo da diversi file immagine.
	 * 
	 * @throws IOException nel caso in cui il nome di una skin da generare sia errato
	 */
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

	/**
	 * Gestisce la chiamata di notifyObservers() causata dal Controller,
	 * unico oggetto che osserva, distinguendo tra tick del timer del Controller
	 * e movimento del giocatore.
	 */
	@Override
	public void update(Observable arg0, Object arg) {
		if(arg instanceof ActionEvent || arg instanceof Boolean) {
			playerMovement = false;
		}
		else if (arg instanceof KeyEvent) {
			playerMovement = true;
		}
		repaint();
	}
	
	/**
	 * Ridisegna sul JPanel che implementa tutti gli elementi di Map
	 * che fanno attualmente parte della partita.
	 */
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
				BufferedImage mobImg = null;//Una immagine diversa per ogni Mob
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
		
		for(Wall next : mapRef.getMyWalls()) {//Non puo' essere vuoto, a causa dei muri non distruttibili
			if(next.getDestroyable()) {//Muro distruttibile
				g.drawImage(destructibleWallImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
			}
			else if(next.getPerimetral()) {//Muro perimetrale
				g.drawImage(perimetralWallImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
			}
			else {//Muro non perimetrale e non distruttibile
				g.drawImage(indestructibleWallImg, next.getPos().x, next.getPos().y, CELL, CELL, null);
			}
		}
		
		if(!mapRef.getMyExplosion().isEmpty()) {
			if(!playerMovement) {//Non causiamo il cambiamento dell'immagine con il movimento del giocatore
				if(flame) {//Causiamo il cambiamento dell'immagine da un tick all'altro
					explosionToRender = explosionImg1;
				}
				else {
					explosionToRender = explosionImg2;
				}
				flame = !flame;//Permette di alternare le immagini da scegliere
			}
			for(Explosion next : mapRef.getMyExplosion()) {//Stessa immagine per tutte le esplosioni
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
		
		Direction playerDir = playerToRender.getDir();
		switch(playerDir) {
			case RIGHT:
				if(playerToRender.getFoot()) {
					playerToRenderImg = playerRight2Img;
				}
				else {
					playerToRenderImg = playerRight1Img;
				}
				break;
			case DOWN:
				if(playerToRender.getFoot()) {
					playerToRenderImg = playerFront2Img;
				}
				else {
					playerToRenderImg = playerFront1Img;
				}
				break;
			case LEFT:
				if(playerToRender.getFoot()) {
					playerToRenderImg = playerLeft2Img;
				}
				else {
					playerToRenderImg = playerLeft1Img;
				}
				break;
			case UP:
				if(playerToRender.getFoot()) {
					playerToRenderImg = playerBack2Img;
				}
				else {
					playerToRenderImg = playerBack1Img;
				}
				break;
			case NONE:
				playerToRenderImg = playerFrontNoneImg;
				break;
			case DEAD:
				playerToRenderImg = playerDeadImg;
				break;
		}
		if(playerToRender.getInvulnerable()) {//Causa l'effetto visivo per accorgersi di aver preso un danno
			if(!invulnerableRender) {
				invulnerableRender = true;
			}
			else {
				g.drawImage(playerToRenderImg, playerToRender.getPos().x, playerToRender.getPos().y, CELL, CELL, null);
				invulnerableRender = false;
			}
		}
		else {
			g.drawImage(playerToRenderImg, playerToRender.getPos().x, playerToRender.getPos().y, CELL, CELL, null);
		}
		lifeAndScoreLabel.setText("<html><font color='red'>LIFE: " + playerToRender.getLife() + "</font><br><br><font color='white'>SCORE: " + playerToRender.getScore() + "</font></html>");
		lifeAndScoreLabel.setLocation(675, -275);
		lifeAndScoreLabel.setVisible(true);
	}

}
