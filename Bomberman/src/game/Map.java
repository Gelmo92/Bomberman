package game;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import game.Entity.Direction;

/**
 * La classe Map contiene i riferimenti a tutte le entita' del gioco.
 * Per ogni partita viene creato un solo oggetto Map.
 * Gestisce le interazioni
 * tra gli oggetti che crea.
 * 
 * @author Yuri Gelmotto
 * @author Riccardo Pidello
 */
class Map extends Observable implements Observer{
	
	private ArrayList<Mob> myMobs;
	private ArrayList<Wall> myWalls;
	private ArrayList<Bomb> myBombs;
	private ArrayList<Chest> myChests;
	private ArrayList<Bonus> myBonus;
	private ArrayList<Terrain> myTerrains;
	private Player myPlayer;
	private ArrayList<Explosion> myExplosion;
	private boolean playerAlive;
	private Controller controllerRef;
	private static final String MAP_NAME = "map.txt";
	private boolean gameOver;
	
	/**
	 * Genera oggetti partendo da un file .txt specificato in MAP_NAME e ne salva i riferimenti
	 * in ArrayList privati;
	 * 
	 * @throws FileNotFoundException se non trova il file con nome ed estensione specificati in MAP_NAME
	 */
	Map() throws FileNotFoundException {
		resetAllStaticRef();//Ci assicuriamo che i bonus ottenuti in una partita precedente non condizionino una partita successiva
		Scanner mapScan = new Scanner(new File(MAP_NAME));
		int y = 0;
		gameOver = false;
		myBombs = new ArrayList<Bomb>();
		myBonus = new ArrayList<Bonus>();
		myChests = new ArrayList<Chest>();
		myExplosion = new ArrayList<Explosion>();
		myMobs = new ArrayList<Mob>();
		myPlayer = null;
		myTerrains = new ArrayList<Terrain>();
		myWalls = new ArrayList<Wall>();
		while(mapScan.hasNextLine()) {
			String currentLine = mapScan.nextLine();
			for(int x = 0; x < currentLine.length(); x++) {
				switch (currentLine.charAt(x)) {
				case 'P':
					myPlayer = new Player(new Point(x*MapView.CELL, y*MapView.CELL), this);
					playerAlive = true;
					break;
				case 'M':
					myMobs.add(new Mob(new Point(x*MapView.CELL, y*MapView.CELL), this));
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
					myChests.add(new Chest(new Point(x*MapView.CELL, y*MapView.CELL), this));
					break;
				case '-':
					break;
				default:
					break; //Qualunque carattere per cui non sia stato specificato un case non potrà produrre errori
				}
				myTerrains.add(new Terrain(new Point(x*MapView.CELL, y*MapView.CELL)));
			}
			y++;
		}
		mapScan.close();
	}
	
	/**
	 * Richiama il metodo resetStatic() di tutte le classi del gioco che
	 * possiedono campi statici modificabili durante una partita.
	 */
	private void resetAllStaticRef() {
		Bomb.resetStatic();
		Mob.resetStatic();
		Explosion.resetStatic();
		Bonus.resetStatic();
		Chest.resetStatic();
		
	}
	
	/**
	 * Permette un singolo movimento a tutte le entita' diverse da Player che 
	 * possono e/o devono tentarlo.
	 */
	void moveEntities() {
		Iterator<Entity> iter = new ArrayList<Entity>(myMobs).iterator();
		while(iter.hasNext()) {
			iter.next().move(MapView.CELL, Direction.NONE);
		}
		if(Bomb.getCanMove()) {
			iter = new ArrayList<Entity>(myBombs).iterator();
			Bomb nextBomb;
			while(iter.hasNext()) {
				nextBomb = (Bomb) iter.next();
				if(nextBomb.isMoving()) {
					nextBomb.move(MapView.CELL, nextBomb.getDirection());
				}
			}
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Permette all'oggetto di tipo Player di tentare un singolo movimento in
	 * direzione dir.
	 * 
	 * @param dir indica la direzione in cui Player deve cercare di muoversi
	 */
	void movePlayer(Direction dir) {
		myPlayer.move(MapView.CELL, dir);
		setChanged();
		notifyObservers(true);// Semplice implementazione per distinguere il movimento di Player in MapView
	}
	
	/**
	 * Verifica la possibilita' di un oggetto di tipo Entity di muoversi
	 * in una certa posizione.
	 * 
	 * @param nextPos indica la posizione in cui si vorrebbe muovere obj
	 * @param obj indica l'oggetto di tipo Entity che sta cercando di muoversi
	 * @return true se l'entita' obj si puo' muovere in posizione nextPos, false altrimenti
	 * @see Map#checkBombs(Point, Entity)
	 * @see Map#checkBonus(Point, Entity)
	 * @see Map#checkChests(Point, Entity)
	 * @see Map#checkExplosions(Point, Entity)
	 * @see Map#checkMobs(Point, Entity)
	 * @see Map#checkPlayer(Point, Entity)
	 * @see Map#checkWalls(Point)
	 */
	boolean canMove(Point nextPos, Entity obj) {
		
		return checkBombs(nextPos, obj)&&
				checkBonus(nextPos, obj)&&
				checkChests(nextPos, obj)&&
				checkExplosions(nextPos, obj)&&
				checkMobs(nextPos,obj)&&
				checkPlayer(nextPos, obj)&&
				checkWalls(nextPos);
	}
	
	/**
	 * Verifica se nella posizione indicata sia gia presente 
	 * un oggetto di tipo Bomb. Se presente gestisce l'interazione
	 * tra il suddetto oggetto e obj
	 * 
	 * @param nextPos indica la posizione da controllare 
	 * @param obj obj indica l'oggetto di tipo Entity che sta cercando di muoversi
	 * @return true se non risulta alcun oggetto di tipo Bomb
	 * in quella posizione, false altrimenti
	 */
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
						return false;
					case "EXPLOSION":
						nextBomb.dominoEffect();
					case "MOB":
						;
					case "BOMB":
						;
					default:
						return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Verifica se nella posizione indicata sia gia presente 
	 * un oggetto di tipo Bonus. Se presente gestisce l'interazione
	 * tra il suddetto oggetto e obj
	 * 
	 * @param nextPos indica la posizione da controllare 
	 * @param obj obj indica l'oggetto di tipo Entity che sta cercando di muoversi
	 * @return true di default
	 */
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
						nextBonus.destroy();
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
	
	/**
	 * Verifica se nella posizione indicata sia gia presente 
	 * un oggetto di tipo Chest. Se presente gestisce l'interazione
	 * tra il suddetto oggetto e obj
	 * 
	 * @param nextPos indica la posizione da controllare 
	 * @param obj obj indica l'oggetto di tipo Entity che sta cercando di muoversi
	 * @return true se non risulta alcun oggetto di tipo Chest
	 * in quella posizione, false altrimenti
	 */
	private boolean checkChests(Point nextPos, Entity obj) {
		Chest nextChest = null;
		Iterator<Chest> chestIter = myChests.iterator();
		while (chestIter.hasNext()) {
			nextChest = chestIter.next();
			if(nextChest.getPos().equals(nextPos)) {
				switch(obj.toString()) {
					case "EXPLOSION":
						myPlayer.addScore(nextChest);
						nextChest.destroy();
					case "PLAYER":
						;
					case "MOB":
						;
					case "BOMB":
						;
					default:
						return false;	
				}
			}
		}
	
		return true;
	}
	
	/**
	 * Verifica se nella posizione indicata sia gia presente 
	 * un oggetto di tipo Explosion. Se presente gestisce l'interazione
	 * tra il suddetto oggetto e obj
	 * 
	 * @param nextPos indica la posizione da controllare 
	 * @param obj obj indica l'oggetto di tipo Entity che sta cercando di muoversi
	 * @return true di default
	 */
	private boolean checkExplosions(Point nextPos, Entity obj) {
		Point nextExplPoint = null;
		Iterator<Explosion> explIter = new ArrayList<Explosion>(myExplosion).iterator();
		Iterator<Point> propagationIter = null;
		while (explIter.hasNext()) {
			propagationIter = explIter.next().getPropagation().iterator();
			while(propagationIter.hasNext()) {
				nextExplPoint = propagationIter.next();
				if(nextExplPoint.equals(nextPos)) {
					switch(obj.toString()) {
						case "PLAYER":
							myPlayer.harm();
							return true;
						case "MOB":
							myPlayer.addScore((Mob)obj);
							((Mob)obj).destroy();
							return true;
						case "BOMB":
							((Bomb) obj).dominoEffect();
						case "EXPLOSION":
							;
						default:
							return true;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Verifica se nella posizione indicata sia gia presente 
	 * un oggetto di tipo Mob. Se presente gestisce l'interazione
	 * tra il suddetto oggetto e obj
	 * 
	 * @param nextPos indica la posizione da controllare 
	 * @param obj obj indica l'oggetto di tipo Entity che sta cercando di muoversi
	 * @return true se non risulta alcun oggetto di tipo Mob
	 * in quella posizione o se obj si puo' comunque muovere nella stessa posizione
	 * di tale oggetto, false altrimenti
	 */
	private boolean checkMobs(Point nextPos, Entity obj) {
		Mob nextMob = null;
		Iterator<Mob> mobIter = new ArrayList<Mob>(myMobs).iterator();
		while (mobIter.hasNext()) {
			nextMob = mobIter.next();
			if(nextMob.getPos().equals(nextPos)) {
				switch(obj.toString()) {
					case "PLAYER":
						myPlayer.harm();
						return true;
					case "MOB":
						;
					case "BOMB":
						return false;
					case "EXPLOSION":
						myPlayer.addScore(nextMob);
						nextMob.destroy();
					default:
						return true;
				}
			}
		}
		return true;
	}
	
	/**
	 * Verifica se nella posizione indicata sia gia presente 
	 * un oggetto di tipo Player. Se presente gestisce l'interazione
	 * tra il suddetto oggetto e obj
	 * 
	 * @param nextPos indica la posizione da controllare 
	 * @param obj obj indica l'oggetto di tipo Entity che sta cercando di muoversi
	 * @return true se non risulta alcun oggetto di tipo Player
	 * in quella posizione o se obj si puo' comunque muovere nella stessa posizione
	 * di tale oggetto, false altrimenti
	 */
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
	
	/**
	 * Verifica se nella posizione indicata sia gia presente 
	 * un oggetto di tipo Wall
	 * 
	 * @param nextPos indica la posizione da controllare 
	 * @return true se non risulta alcun oggetto di tipo Wall
	 * in quella posizione, false altrimenti
	 */
	private boolean checkWalls(Point nextPos) {
		
		Iterator<Wall> wallIter = myWalls.iterator();
		while(wallIter.hasNext()) {
			if(wallIter.next().getPos().equals(nextPos)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Verifica se nella posizione indicata sia presente 
	 * un oggetto di tipo Wall.
	 * Se presente e distruttibile, lo distrugge.
	 * 
	 * @param pos indica la posizione da controllare
	 * @return true se risulta un oggetto di tipo Wall in quella posizione ed e'
	 * distruttibile, false altrimenti
	 */
	boolean canDestroyWall(Point pos) {
		Wall nextWall = null;
		Iterator<Wall> wallIter = myWalls.iterator();
		while(wallIter.hasNext()) {
			nextWall = wallIter.next();
			if(nextWall.getPos().equals(pos)) {
				if(nextWall.getDestroyable()) {
					myPlayer.addScore(nextWall);
					wallIter.remove();//Dobbiamo rimuoverlo tramite l'iteratore per non causare una ConcurrentModificationException
					nextWall.destroy();
					return true;
				}
				else {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * Verifica se puo' essere generato un oggetto di tipo Bomb
	 * in posizione pos
	 * 
	 * @param pos indica la posizione in cui si sta cercando di generare un oggetto
	 * di tipo Bomb
	 * @return true se non risulta alcun oggetto di tipo Bomb
	 * in quella posizione e se non si e' raggiunto il limite massimo di oggetti
	 * di tipo Bomb, false altrimenti
	 */
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
	
	/**
	 * Genera un nuovo oggetto di tipo Bomb nella stessa posizione dell'oggetto
	 * di tipo Player
	 */
	void dropBomb() {
		if(canDropBomb(myPlayer.getPos())) {
			Bomb newBomb = new Bomb(myPlayer.getPos(),this);
			myBombs.add(newBomb);
			checkExplosions(newBomb.getPos(), newBomb);
		}
	}

	/**
	 * Genera un nuovo oggetto di tipo Bonus in posizione pos
	 * @param pos indica la posizione in cui generare il nuovo oggetto di tipo Bonus
	 */
	void dropBonus(Point pos) {
		Bonus newBonus = new Bonus(pos, this);
		myBonus.add(newBonus);
	}
	
	/**
	 * Gestisce la chiamata di notifyObservers() di uno degli oggetti
	 * di tipo Entity che osserva.
	 */
	@Override
	public void update(Observable obj, Object arg) {
		switch(obj.toString()) {
			case "BOMB":
				((Bomb) obj).destroy();
				Explosion newExplosion = new Explosion(((Bomb) obj).getPos(), this);
				myExplosion.add(newExplosion);
				break;
			case "EXPLOSION":
				((Explosion) obj).destroy();
				break;
			case "BONUS":
				((Bonus)obj).destroy();
				break;
			case "PLAYER":
				if(!gameOver) {//Evitiamo la notifica vittoria e poi notifica morte player
					gameOver = !gameOver;
					setChanged();
					notifyObservers();
					controllerRef.gameOver(playerAlive);//Sconfitta
				}
				break;
			case "MOB":
				if(myMobs.isEmpty() && !gameOver) {//Evitiamo la notifica morte player e poi notifica morte dell'ultimo mob
					gameOver = !gameOver;
					setChanged();
					notifyObservers();
					controllerRef.gameOver(playerAlive);//Vittoria
				}
				break;
			default:
				break;
		}
	}
	
	/**
	 * Rimuove l'oggetto obj, che ha Entity come superclasse, dall'ArrayList
	 * che lo contiene
	 * 
	 * @param obj indica l'oggetto di tipo Entity da rimuovere dal relativo ArrayList
	 */
	void removeFromArrayList(Entity obj) {
		switch(obj.toString()) {
			case "BOMB":
				myBombs.remove((Bomb) obj);
				break;
			case "BONUS":
				myBonus.remove((Bonus)obj);
				break;
			case "CHEST":
				myChests.remove((Chest)obj);
				break;
			case "EXPLOSION":
				myExplosion.remove((Explosion) obj);
				break;
			case "MOB":
				myMobs.remove((Mob)obj);
				break;
		}
	}

	/**
	 * 
	 * @return l'ArrayList degli oggetti di tipo Mob
	 */
	ArrayList<Bomb> getMyBombs() {
		return myBombs;
	}
	
	/**
	 * 
	 * @return l'ArrayList degli oggetti di tipo Bonus
	 */
	ArrayList<Bonus> getMyBonus() {
		return myBonus;
	}
	
	/**
	 * 
	 * @return l'ArrayList degli oggetti di tipo Chest
	 */
	ArrayList<Chest> getMyChests() {
		return myChests;
	}
	
	/**
	 * 
	 * @return l'ArrayList degli oggetti di tipo Explosion
	 */
	ArrayList<Explosion> getMyExplosion() {
		return myExplosion;
	}
	
	/**
	 * 
	 * @return l'ArrayList degli oggetti di tipo Mob
	 */
	ArrayList<Mob> getMyMobs() {
		return myMobs;
	}
	
	/**
	 * 
	 * @return l'oggetto di tipo Player
	 */
	Player getMyPlayer() {
		return myPlayer;
	}
	
	/**
	 * 
	 * @return l'ArrayList degli oggetti di tipo Terrain
	 */
	ArrayList<Terrain> getMyTerrains() {
		return myTerrains;
	}
	
	/**
	 * 
	 * @return l'ArrayList degli oggetti di tipo Wall
	 */
	ArrayList<Wall> getMyWalls() {
		return myWalls;
	}
	
	/**
	 * 
	 * @return true se il Player e' ancora vivo, false altrimenti
	 */
	boolean getPlayerAlive() {
		return playerAlive;
	}

	/**
	 * 
	 * @param playerAlive indica il valore da assegnare alla variabile privata
	 * omonima di questo oggetto
	 */
	void setPlayerAlive(boolean playerAlive) {
		this.playerAlive = playerAlive;
	}

	/**
	 * 
	 * @param controller il controller associato a questa mappa
	 */
	void setControllerRef(Controller controller) {
		controllerRef = controller;
	}

}
