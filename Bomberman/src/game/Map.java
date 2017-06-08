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
	static Player myPlayer;
	static ArrayList<Explosion> myExplosion;
	static Dimension dimension = new Dimension(MapView.cell, MapView.cell);
	static boolean playerAlive;
	private int explosionRate;
	
	public Map() throws FileNotFoundException {
		Scanner mapScan = new Scanner(new File("map.txt"));
		int y = 0;
		explosionRate = 4;
		myWalls = new ArrayList<Wall>();
		myMobs = new ArrayList<Mob>();
		myExplosion = new ArrayList<Explosion>();
		myBombs = new ArrayList<Bomb>();
		while(mapScan.hasNextLine()) {
			String currentLine = mapScan.nextLine();
			for(int x = 0; x < currentLine.length(); x++) {
				switch (currentLine.charAt(x)) {
				case 'P':
					myPlayer = new Player(new Point(x*MapView.cell, y*MapView.cell));
					playerAlive = true;
					System.out.println("Giocatore creato in posizione " + x +", " + y);
					break;
				case 'M':
					myMobs.add(new Mob(new Point(x*MapView.cell, y*MapView.cell)));
					System.out.println("Mob creato in posizione " + x +", " + y);
					break;
				case 'W':
					myWalls.add(new Wall(new Point(x*MapView.cell, y*MapView.cell), false));
					System.out.println("Muro creato in posizione " + x +", " + y);
					break;
				case 'w':
					myWalls.add(new Wall(new Point(x*MapView.cell, y*MapView.cell), true));
					System.out.println("Muro creato in posizione " + x +", " + y);
					break;
				case '-':
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
	
	static boolean canMove(Point nextPos, int id) {
		Rectangle nextPosition = new Rectangle(nextPos, dimension);
		for(Wall next : myWalls) {
			Rectangle wall = new Rectangle(next.getPos(), dimension);
			if(nextPosition.intersects(wall)) {
				return false;
			}
		}
		if(myBombs != null) {
			for(Bomb next : myBombs) {
				Rectangle bomb = new Rectangle(next.getPos(), dimension);
				if(nextPosition.intersects(bomb)) {
					if(id == 1 || id == 2) {
						return false;
					}
					else {
						next.dominoEffect();
						return true;
					}
				}
			}
		}
		if(id == 1 || id == 2) {
			for(Explosion next : myExplosion) {
				for(Point nextP : next.propagation) {
					if(nextPosition.intersects(new Rectangle(nextP, dimension))) {
						if(id == 1) {
							myPlayer.destroy();
						}
						else {
							Mob toDestroy = null;
							for(Mob nextM : myMobs) {
								if(nextM.nextPos.equals(nextPos)) {
									System.out.println("Trovato " + nextPos);
									toDestroy = nextM;
								}
							}
							toDestroy.destroy();
							myMobs.remove(toDestroy);
						}
						return false;
					}
				}
			}
		}
		if(id == 1 || id == 3) {
			Mob toDestroy = null;
			for(Mob next : myMobs) {
				Rectangle mob = new Rectangle(next.getPos(), dimension);
				if(nextPosition.intersects(mob)) {
					if(id == 1) {
						myPlayer.destroy();
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
				return false;
			}
		}
		if(id == 2 || id == 3) {
			
			if(nextPosition.intersects(new Rectangle(myPlayer.getPos(), dimension))) {
				myPlayer.destroy();
			}
		}
		
		return true;
		
	}

	void dropBomb() {
		Bomb newBomb = new Bomb(myPlayer.getPos());
		myBombs.add(newBomb);
		newBomb.addObserver(this);
		setChanged();
		notifyObservers();
	}

	@Override
	public void update(Observable obj, Object arg) {
		if(obj instanceof Bomb) {
			Explosion newExplosion = new Explosion(((Bomb) obj).destroy(), explosionRate);
			myExplosion.add(newExplosion);
			newExplosion.addObserver(this);
			myBombs.remove((Bomb) obj);
		}
		else {
			myExplosion.remove((Explosion) obj);
			((Explosion) obj).destroy();
		}
		
	}

	static boolean canDestroy(Point nextPos) {
		for(Wall next : myWalls) {
			if(next.getPos().equals(nextPos)) {
				if(next.destroyable) {
					myWalls.remove(next);
					next.destroy();
					return true;
				}
			}
		}
		return false;
	}

}
