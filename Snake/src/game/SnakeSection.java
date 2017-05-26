package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.math.*;

public class SnakeSection extends Rectangle 
{
	private static final long serialVersionUID = 1L;
	public Rectangle section;
	static int width = 16;
	static int height = 16;
	public int x;
	public int oldX = x;
	public int oldOldX = oldX;
	public int y;
	public int oldY = y;
	public int oldOldY = oldY;
	public SnakeSection next = null;
	public int counter = 1;
	
	public static boolean right;
	public static boolean left;
	public static boolean up;
	public static boolean down;
	private int speed = 16;
	public boolean currentDirection = right;
	public boolean canMove = true;
	
	public SnakeSection(int x, int y, SnakeSection oldTail)
	{
		this.x = x;
		this.y = y;
		setBounds(x, y, width, height);
		if(oldTail != null)
		{
			oldTail.next = this;
			this.counter = oldTail.counter + 1;
		}
		Player.tail = this;
		
		//System.out.println(this.counter);
		
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.green);
		//g.fillRect(this.x, this.y, this.width, this.height);
		g.fillRoundRect(this.x , this.y, this.width, this.height, 300, 300);
		//g.drawOval(this.x, this.y, this.width, this.height);
		if(this.next != null)
		{
			this.next.render(g);
		}
	}
	
	public void tick()
	{	
		this.oldOldX = this.oldX;
		this.oldOldY = this.oldY;
		
		this.oldX = this.x;
		this.oldY = this.y;
		if(right)
		{
			this.x += speed;
		}
		if(left)
		{
			this.x -= speed;
		}
		if(up)
		{
			this.y -= speed;
		}
		if(down)
		{
			this.y += speed;
		}
		if(this.x < 0)
		{
			this.x = (Game.WIDTH)-16;
		}
		if(this.y < 0)
		{
			this.y = (Game.HEIGHT)-16;
		}
		if(this.x > (Game.WIDTH)-16)
		{
			this.x = 0;
		}
		if(this.y > (Game.HEIGHT)-16)
		{
			this.y = 0;
		}
	}
	
	public void tick (int x, int y)
	{
		/*if(this.counter > 4)
		{
			if(this.intersects(Player.head.x, Player.head.y, Player.head.width, Player.head.height))
			{
				System.out.println("HAI PERSO");
				Game.isRunning = false;
			}
		}*/
		this.oldOldX = this.oldX;
		this.oldOldY = this.oldY;
		
		this.oldX = this.x;
		this.oldY = this.y;
		
		this.x = x;
		this.y = y;
		//System.out.println(this.counter + " " + this.x + " " +this.y);
		if(this.next != null)
		{
			this.next.tick(this.oldX,this.oldY);
		}
	}
}
