package game;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
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
					break;
				case 'M':
					myMobs.add(new Mob(new Point(x*MapView.cell, y*MapView.cell), this));
					myTerrain.add(new Terrain(new Point(myMobs.get(myMobs.size()-1).getPos())));
					break;
				case 'W':
					myWalls.add(new Wall(new Point(x*MapView.cell, y*MapView.cell), false, false));
					break;
				case 'w':
					myWalls.add(new Wall(new Point(x*MapView.cell, y*MapView.cell), true, false));
					break;
				case 'p':
					myWalls.add(new Wall(new Point(x*MapView.cell, y*MapView.cell), false, true));
					break;
				case 'C':
					myChests.add(new Chest(new Point(x*MapView.cell, y*MapView.cell)));
					myTerrain.add(new Terrain(new Point(myChests.get(myChests.size()-1).getPos()),true));
					break;
				case '-':
					myTerrain.add(new Terrain(new Point(x*MapView.cell, y*MapView.cell)));
					break;
				default:
					System.out.print(currentLine.charAt(x));//IMPLEMENTARE ERRORE
					break;
				}
			}
			y++;
		}
		mapScan.close();
		setChanged();
		notifyObservers();
	}
	
	synchronized boolean canMove(Point nextPos, Object obj) {
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
					if(obj instanceof Player) {
						if(Bomb.getCanMove()) {
							next.move(MapView.cell, ((Player) obj).getDir());
						}
						return false;
					}
					else if(obj instanceof Mob) {
						return false;
					}
					else if(obj instanceof Explosion){
						next.dominoEffect();
						return false;
					}
					else if(obj instanceof Bomb) {
						return false;
					}
				}
			}
		}
		
		Chest chestToDestroy = null;
		for(Chest next : myChests) {
			Rectangle chest = new Rectangle(next.getPos(), dimension);
			if(nextPosition.intersects(chest)) {
				if(obj instanceof Player || obj instanceof Mob || obj instanceof Bomb) {
					return false;
				}
				else {
					chestToDestroy = next;
				}
			}
		}
		if(chestToDestroy != null) {
			myPlayer.addScore(chestToDestroy);
			dropBonus(chestToDestroy.destroy());
			myChests.remove(chestToDestroy);
			return true;
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
				myPlayer.addScore(bonusToDestroy);
				myBonus.remove(bonusToDestroy);
				return true;
			}
			
		}
		if(obj instanceof Player || obj instanceof Mob || obj instanceof Bomb) {
			for(Explosion next : myExplosion) {
				for(Point nextP : next.propagation) {
					if(nextPosition.intersects(new Rectangle(nextP, dimension))) {
						if(obj instanceof Player) {
							myPlayer.harm();
							return true;
						}
						else if(obj instanceof Bomb) {
							((Bomb) obj).dominoEffect();
							return true;
						}
						else if(obj instanceof Mob){
							Mob toDestroy = null;
							for(Mob nextM : myMobs) {
								if(nextM.nextPos.equals(nextPos)) {
									toDestroy = nextM;
								}
							}
							if(toDestroy != null) {
								myPlayer.addScore(toDestroy);
								toDestroy.destroy();
								myMobs.remove(toDestroy);
							}
							
						}
						return false;
					}
				}
			}
		}
		
			Mob toDestroy = null;
			Iterator<Mob> mobIter = myMobs.iterator();
			while (mobIter.hasNext()) {
				toDestroy = mobIter.next();
				Rectangle mob = new Rectangle(toDestroy.getPos(), dimension);
				if(nextPosition.intersects(mob)) {
					if(obj instanceof Player) {
						myPlayer.harm();
						return false;
					}
					else if(obj instanceof Bomb) {
						return false;
					}
					else if(obj instanceof Mob) {
						return false;
					}
					else if(obj instanceof Explosion){
						myPlayer.addScore(toDestroy);
						toDestroy.destroy();
						mobIter.remove();
					}
				}
			}
			/*for(Mob next : myMobs) {
				Rectangle mob = new Rectangle(next.getPos(), dimension);
				if(nextPosition.intersects(mob)) {
					if(obj instanceof Player) {
						myPlayer.harm();
						return false;
					}
					else if(obj instanceof Bomb) {
						return false;
					}
					else if(obj instanceof Mob) {
						return false;
					}
					else if(obj instanceof Explosion){
						toDestroy = next;
					}
				}
			
			if(toDestroy != null) {
				myPlayer.addScore(toDestroy);
				toDestroy.destroy();
				myMobs.remove(toDestroy);
				//return false;
			}
		}*/
		
		
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
			Bomb newBomb = new Bomb(myPlayer.getPos(),this);
			myBombs.add(newBomb);
			newBomb.addObserver(this);
			setChanged();
			notifyObservers();
		}
	}

	private void dropBonus(Point pos) {
		Bonus newBonus = new Bonus(pos, this);
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
					myPlayer.addScore(next);
					myWalls.remove(next);
					myTerrain.add(new Terrain(next.destroy(), true));
					return true;
				}
			}
		}
		return false;
	}

	public void synchMobs() {
		for(Mob next : myMobs) {
			Controller.t.addActionListener(next);
		}
		
	}

}
