package game;

import java.awt.Point;

public class Terrain {

	private Point position;
	private boolean burnt;
	
	public Terrain(Point pos) {
		this.position = pos;
		burnt = false;
	}
	
	public Terrain(Point pos, boolean burnt) {
		this.position = pos;
		this.burnt = burnt;
	}
	
	Point getPos() {
		return position;
	}
	
	boolean getBurnt() {
		return burnt;
	}
	void setBurnt() {
		this.burnt = true;
	}
}
