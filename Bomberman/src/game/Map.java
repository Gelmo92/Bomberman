package game;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;


class Map extends Observable implements Observer{

	static ArrayList<Mob> myMobs;
	static ArrayList<Wall> myWalls;
	static ArrayList<Bomb> myBombs;
	static ArrayList<Chest> myChests;
	static ArrayList<Bonus> myBonus;
	static ArrayList<Terrain> myTerrain;
	static Player myPlayer;
	static ArrayList<Explosion> myExplosion;
	static Dimension dimension = new Dimension(MapView.cell, MapView.cell);
	static boolean playerAlive;
	
	
	
	
	public Map() throws FileNotFoundException {
		Scanner mapScan = new Scanner(new File("map.txt"));
		int y = 0;
		myWalls = new ArrayList<Wall>();
		myMobs = new ArrayList<Mob>();
		myExplosion = new ArrayList<Explosion>();
		myBombs = new ArrayList<Bomb>();
		myChests = new ArrayList<Chest>();
		myBonus = new ArrayList<Bonus>();
		myTerrain = new ArrayList<Terrain>();
		while(mapScan.hasNextLine()) {
			String currentLine = mapScan.nextLine();
			for(int x = 0; x < currentLine.length(); x++) {
				switch (currentLine.charAt(x)) {
				case 'P':
					myPlayer = new Player(new Point(x*MapView.cell, y*MapView.cell), this);
					myTerrain.add(new Terrain(new Point(myPlayer.getPos())));
					playerAlive = true;
					System.out.println("Giocatore creato in posizione " + x +", " + y);
					break;
				case 'M':
					myMobs.add(new Mob(new Point(x*MapView.cell, y*MapView.cell), this));
					myTerrain.add(new Terrain(new Point(myMobs.get(myMobs.size()-1).getPos())));
					System.out.println("Mob creato in posizione " + x +", " + y);
					break;
				case 'W':
					myWalls.add(new Wall(new Point(x*MapView.cell, y*MapView.cell), false, false));
					System.out.println("Muro creato in posizione " + x +", " + y);
					break;
				case 'w':
					myWalls.add(new Wall(new Point(x*MapView.cell, y*MapView.cell), true, false));
					System.out.println("Muro creato in posizione " + x +", " + y);
					break;
				case 'p':
					myWalls.add(new Wall(new Point(x*MapView.cell, y*MapView.cell), false, true));
					System.out.println("Muro creato in posizione " + x +", " + y);
					break;
				case 'C':
					myChests.add(new Chest(new Point(x*MapView.cell, y*MapView.cell)));
					System.out.println("c " + myChests.get(myChests.size()-1).getPos());
					myTerrain.add(new Terrain(new Point(myChests.get(myChests.size()-1).getPos())));
					System.out.println("t " + myTerrain.get(myTerrain.size()-1).getPos());
				case '-':
					myTerrain.add(new Terrain(new Point(x*MapView.cell, y*MapView.cell)));
					break;
				default:
					System.out.print(currentLine.charAt(x));
					break;
				}
			}
			y++;
		}
		mapScan.close();
		setChanged();
		notifyObservers();
	}
	
	boolean canMove(Point nextPos, Object obj) {
		Rectangle nextPosition = new Rectangle(nextPos, dimension);
		for(Wall next : myWalls) {
			Rectangle wall = new Rectangle(next.getPos(), dimension);
			if(nextPosition.intersects(wall)) {
				return false;
			}
		}
		
		if(obj instanceof Mob || obj instanceof Explosion) {
			if(nextPosition.intersects(new Rectangle(myPlayer.getPos(), dimension))) {
				myPlayer.harm();
			}
		}
		
		if(myBombs != null) {
			for(Bomb next : myBombs) {
				Rectangle bomb = new Rectangle(next.getPos(), dimension);
				if(nextPosition.intersects(bomb)) {
					if(obj instanceof Player || obj instanceof Mob) {
						return false;
					}
					else {
						next.dominoEffect();
						return false;
					}
				}
			}
		}
		
		Chest chestToDestroy = null;
		for(Chest next : myChests) {
			Rectangle chest = new Rectangle(next.getPos(), dimension);
			if(nextPosition.intersects(chest)) {
				if(obj instanceof Player || obj instanceof Mob) {
					return false;
				}
				else {
					chestToDestroy = next;
				}
			}
		}
		if(chestToDestroy != null) {
			dropBonus(chestToDestroy.destroy());
			myChests.remove(chestToDestroy);
			return false;
		}
		Bonus bonusToDestroy = null;
		for(Bonus next : myBonus) {
			Rectangle bonus = new Rectangle(next.getPos(), dimension);
			if(nextPosition.intersects(bonus)) {
				if(obj instanceof Mob || obj instanceof Bomb) {
					return true;
				}
				else {
					bonusToDestroy = next;
				}
			}
		}
		if(bonusToDestroy != null) {
			if (obj instanceof Explosion) {
				myBonus.remove(bonusToDestroy);
				return true;
			}
			else if (obj instanceof Player) {
				bonusToDestroy.getBonus();
				myBonus.remove(bonusToDestroy);
				return true;
			}
			
		}
		if(obj instanceof Player || obj instanceof Mob) {
			for(Explosion next : myExplosion) {
				for(Point nextP : next.propagation) {
					if(nextPosition.intersects(new Rectangle(nextP, dimension))) {
						if(obj instanceof Player) {
							myPlayer.harm();
						}
						else {
							Mob toDestroy = null;
							for(Mob nextM : myMobs) {
								if(nextM.nextPos.equals(nextPos)) {
									toDestroy = nextM;
								}
							}
							if(toDestroy != null) {
								toDestroy.destroy();
								myMobs.remove(toDestroy);
							}
							
						}
						return false;
					}
				}
			}
		}
		if(obj instanceof Player || obj instanceof Explosion) {
			Mob toDestroy = null;
			for(Mob next : myMobs) {
				Rectangle mob = new Rectangle(next.getPos(), dimension);
				if(nextPosition.intersects(mob)) {
					if(obj instanceof Player) {
						myPlayer.harm();
						return false;
					}
					else {
						toDestroy = next;
					}
				}
			}
			if(toDestroy != null) {
				toDestroy.destroy();
				myMobs.remove(toDestroy);
				//return false;
			}
		}
		
		
		return true;
		
	}
	private boolean canDropBomb(Point pos) {
		if (Bomb.getDroppedBombs() >= Bomb.getNumberBomb()) {
			return false;
		}
		for(Bomb nextBomb : myBombs) {
			if(pos.equals(nextBomb.getPos())) {
				return false;
			}
		}
		return true;
	}
	void dropBomb() {
		if(canDropBomb(myPlayer.getPos())) {
			Bomb newBomb = new Bomb(myPlayer.getPos());
			myBombs.add(newBomb);
			newBomb.addObserver(this);
			setChanged();
			notifyObservers();
		}
	}

	private void dropBonus(Point pos) {
		Bonus newBonus = new Bonus(pos);
		myBonus.add(newBonus);
		newBonus.addObserver(this);
	}
	@Override
	public void update(Observable obj, Object arg) {
		if(obj instanceof Bomb) {
			Explosion newExplosion = new Explosion(((Bomb) obj).destroy(), this);
			myExplosion.add(newExplosion);
			newExplosion.addObserver(this);
			myBombs.remove((Bomb) obj);
		}
		else if(obj instanceof Explosion){
			myExplosion.remove((Explosion) obj);
			((Explosion) obj).destroy();
		}
		else if(obj instanceof Bonus) {
			myBonus.remove((Bonus)obj);
			((Bonus)obj).destroy();
		}
		
	}

	static boolean canDestroy(Point nextPos) {
		for(Wall next : myWalls) {
			if(next.getPos().equals(nextPos)) {
				if(next.destroyable) {
					myWalls.remove(next);
					myTerrain.add(new Terrain(next.destroy(), true));
					return true;
				}
			}
		}
		return false;
	}

}
