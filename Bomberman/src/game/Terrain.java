package game;

import java.awt.Point;

public class Terrain {

	private Point position;
	
	public Terrain(Point pos) {
		this.position = pos;
	}
	
	Point getPos() {
		return position;
	}
}
