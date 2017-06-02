package game;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

class Map extends Observable{

	static ArrayList<Mob> myMobs;
	static ArrayList<Wall> myWalls;
	static Player myPlayer;
	static Dimension dimension = new Dimension(MapView.cell, MapView.cell);
	static boolean playerAlive;
	
	public Map() throws FileNotFoundException {
		Scanner mapScan = new Scanner(new File("map.txt"));
		int y = 0;
		myWalls = new ArrayList<Wall>();
		myMobs = new ArrayList<Mob>();
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
					myWalls.add(new Wall(new Point(x*MapView.cell, y*MapView.cell)));
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
		if(id == 1) {
			for(Mob next : myMobs) {
				Rectangle mob = new Rectangle(next.getPos(), dimension);
				if(nextPosition.intersects(mob)) {
					myPlayer.destroy();
					return false;
				}
			}
		}
		
		
		return true;
		
	}

}
