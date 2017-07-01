package game;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import javax.swing.Timer;


class Map extends Observable implements Observer{
	
	ArrayList<Mob> myMobs;
	ArrayList<Wall> myWalls;
	ArrayList<Bomb> myBombs;
	ArrayList<Chest> myChests;
	ArrayList<Bonus> myBonus;
	ArrayList<Terrain> myTerrains;
	Player myPlayer;
	ArrayList<Explosion> myExplosion;
	static final Dimension dimension = new Dimension(MapView.CELL, MapView.CELL);
	private boolean playerAlive;
	Controller controllerRef;
	
	private static final String MAP_NAME = "map.txt";
	
	public Map() throws FileNotFoundException {
		resetAllStaticRef();
		Scanner mapScan = new Scanner(new File(MAP_NAME));
		int y = 0;
		myWalls = new ArrayList<Wall>();
		myMobs = new ArrayList<Mob>();
		myExplosion = new ArrayList<Explosion>();
		myBombs = new ArrayList<Bomb>();
		myChests = new ArrayList<Chest>();
		myBonus = new ArrayList<Bonus>();
		myTerrains = new ArrayList<Terrain>();
		
		while(mapScan.hasNextLine()) {
			String currentLine = mapScan.nextLine();
			for(int x = 0; x < currentLine.length(); x++) {
				switch (currentLine.charAt(x)) {
				case 'P':
					myPlayer = new Player(new Point(x*MapView.CELL, y*MapView.CELL), this);
					myPlayer.addObserver(this);
					playerAlive = true;
					break;
				case 'M':
					Mob newMob = new Mob(new Point(x*MapView.CELL, y*MapView.CELL), this);
					myMobs.add(newMob);
					newMob.addObserver(this);
					break;
				case 'W':
					myWalls.add(new Wall(new Point(x*MapView.CELL, y*MapView.CELL), false, false));
					break;
				case 'w':
					myWalls.add(new Wall(new Point(x*MapView.CELL, y*MapView.CELL), true, false));
					break;
				case 'p':
					myWalls.add(new Wall(new Point(x*MapView.CELL, y*MapView.CELL), false, true));
					break;
				case 'C':
					myChests.add(new Chest(new Point(x*MapView.CELL, y*MapView.CELL)));
				case '-':
					;
				default:
					;
				}
				myTerrains.add(new Terrain(new Point(x*MapView.CELL, y*MapView.CELL)));
			}
			y++;
		}
		mapScan.close();
	}
	
	private void resetAllStaticRef() {
		Bomb.resetStatic();
		Mob.resetStatic();
		Explosion.resetStatic();
		Bonus.resetStatic();
		
	}

	boolean canMove(Point nextPos, Entity obj) {
		
		return checkWalls(nextPos, obj) &&
			   checkBombs(nextPos, obj)&&
			   checkMobs(nextPos,obj)&&
			   checkExplosions(nextPos, obj)&&
			   checkBonus(nextPos, obj)&&
			   checkChests(nextPos, obj)&&
			   checkPlayer(nextPos, obj);
	}
		
	private boolean checkWalls(Point nextPos, Entity obj) {
		
		Iterator<Wall> wallIter = myWalls.iterator();
		while(wallIter.hasNext()) {
			if(wallIter.next().getPos().equals(nextPos)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkBombs(Point nextPos, Entity obj) {
		Bomb nextBomb = null;
		Iterator<Bomb> bombIter = myBombs.iterator();
		while(bombIter.hasNext()) {
			nextBomb = bombIter.next();
			if(nextBomb.getPos().equals(nextPos)) {
				switch(obj.toString()) {
					case "PLAYER":
						if(Bomb.getCanMove()) {
							nextBomb.move(MapView.CELL, ((Player) obj).getDir());
						}
						break;
					case "EXPLOSION":
						nextBomb.dominoEffect();
						break;
					default:
						;
				}
				return false;
			}
		}
		return true;
	}

	private boolean checkMobs(Point nextPos, Entity obj) {
		Mob nextMob = null;
		Iterator<Mob> mobIter = myMobs.iterator();
		while (mobIter.hasNext()) {
			nextMob = mobIter.next();
			if(nextMob.getPos().equals(nextPos)) {
				switch(obj.toString()) {
					case "PLAYER":
						myPlayer.harm();
						break;
					case "MOB":
						;
					case "BOMB":
						return false;
					case "EXPLOSION":
						myPlayer.addScore(nextMob);
						mobIter.remove();
						nextMob.destroy();
						break;
					default:
						;
				}
			}
		}
		return true;
	}

	private boolean checkExplosions(Point nextPos, Entity obj) {
		Point nextExplPoint = null;
		Iterator<Explosion> explIter = new ArrayList<Explosion>(myExplosion).iterator();
		Iterator<Point> propagationIter = null;
		while (explIter.hasNext()) {
			propagationIter = explIter.next().propagation.iterator();
			while(propagationIter.hasNext()) {
				nextExplPoint = propagationIter.next();
				if(nextExplPoint.equals(nextPos)) {
					switch(obj.toString()) {
						case "PLAYER":
							myPlayer.harm();
							return true;
						case "MOB":
							myPlayer.addScore((Mob)obj);
							myMobs.remove((Mob)obj);
							((Mob)obj).destroy();
							break;
						case "BOMB":
							((Bomb) obj).dominoEffect();
						default:
							return true;
					}
				}
			}
		}
		return true;
	}

	private boolean checkBonus(Point nextPos, Entity obj) {
		Bonus nextBonus = null;
		Iterator<Bonus> bonusIter = myBonus.iterator();
		while (bonusIter.hasNext()) {
			nextBonus = bonusIter.next();
			if(nextBonus.getPos().equals(nextPos)) {
				switch(obj.toString()) {
					case "PLAYER":
						nextBonus.getBonus();
						myPlayer.addScore(nextBonus);
					case "EXPLOSION":
						bonusIter.remove();
					case "MOB":
						;
					case "BOMB":
						;
					default:
						return true;
				}
			}
		}
	
		return true;
	}
	
	private boolean checkChests(Point nextPos, Entity obj) {
		Chest nextChest = null;
		Iterator<Chest> chestIter = myChests.iterator();
		while (chestIter.hasNext()) {
			nextChest = chestIter.next();
			if(nextChest.getPos().equals(nextPos)) {
				switch(obj.toString()) {
					case "EXPLOSION":
						myPlayer.addScore(nextChest);
						dropBonus(nextChest.getPos());
						chestIter.remove();
						return true;
					default:
						return false;	
				}
			}
		}
	
		return true;
	}
	
	private boolean checkPlayer(Point nextPos, Entity obj) {
		if(nextPos.equals(myPlayer.getPos())) {
			switch(obj.toString()) {
				case "BOMB":
					return false;
				case "MOB":
					;
				case "EXPLOSION":
					myPlayer.harm();
				default:
					return true;
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
			Bomb newBomb = new Bomb(myPlayer.getPos(),this);
			myBombs.add(newBomb);
			newBomb.addObserver(this);
			checkExplosions(newBomb.getPos(), newBomb);
		}
	}

	private void dropBonus(Point pos) {
		Bonus newBonus = new Bonus(pos, this);
		myBonus.add(newBonus);
		newBonus.addObserver(this);
	}
	@Override
	public void update(Observable obj, Object arg) {
		switch(obj.toString()) {
			case "BOMB":
				myBombs.remove((Bomb) obj);
				Explosion newExplosion = new Explosion(((Bomb) obj).getPos(), this);
				((Bomb) obj).destroy();
				myExplosion.add(newExplosion);
				newExplosion.addObserver(this);
				break;
			case "EXPLOSION":
				myExplosion.remove((Explosion) obj);
				((Explosion) obj).destroy();
				break;
			case "BONUS":
				myBonus.remove((Bonus)obj);
				((Bonus)obj).destroy();
				break;
			case "PLAYER":
				setChanged();
				notifyObservers(playerAlive);
				break;
			case "MOB":
				if(myMobs.isEmpty()) {
					setChanged();
					notifyObservers(playerAlive);
				}
					break;
			default:
		}
		/*
		if(obj instanceof Bomb) {
			System.out.println("SI1");
			myBombs.remove((Bomb) obj);
			Explosion newExplosion = new Explosion(((Bomb) obj).getPos(), this);
			((Bomb) obj).destroy();
			myExplosion.add(newExplosion);
			newExplosion.addObserver(this);
		}
		else if(obj instanceof Explosion){
			System.out.println("SI2");
			myExplosion.remove((Explosion) obj);
			((Explosion) obj).destroy();
		}
		else if(obj instanceof Bonus) {
			myBonus.remove((Bonus)obj);
			((Bonus)obj).destroy();
		}
		else if(obj instanceof Player) {
			setChanged();
			notifyObservers(playerAlive);
		}
		else if(obj instanceof Mob) {
			if(myMobs.isEmpty()) {
				System.out.println("SI3");
				setChanged();
				notifyObservers(playerAlive);
			}
		}
		*/
	}

	boolean canDestroy(Point nextPos) {
		Wall nextWall = null;
		Iterator<Wall> wallIter = myWalls.iterator();
		while(wallIter.hasNext()) {
			nextWall = wallIter.next();
			if(nextWall.getPos().equals(nextPos)) {
				if(nextWall.destroyable) {
					myPlayer.addScore(nextWall);
					nextWall.destroy();
					wallIter.remove();
					return true;
				}
			}
		}
		return false;
	}

	public void synchMobs(Timer t) {
		for(Mob next : myMobs) {
			t.addActionListener(next);
		}
		
	}

	public boolean isPlayerAlive() {
		return playerAlive;
	}

	public void setPlayerAlive(boolean playerAlive) {
		this.playerAlive = playerAlive;
	}
}
