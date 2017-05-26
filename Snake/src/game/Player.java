package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends Rectangle
{
	static public SnakeSection head;
	static public SnakeSection tail = null;
	
	
	
	public Player(int x, int y)
	{
		this.head = new SnakeSection(x,y,null);
	}
	
	public void tick()
	{		
		head.tick();
		if(head.next != null)
		{	
				SnakeSection temp = head.next;
				while (temp != null){
					if(head.x == temp.x && head.y== temp.y)
					{
						//System.out.println("HAI PERSO " + head.x + " " + head.y + " " + temp.x + " " + temp.y + " " + temp.counter);
						Game.isRunning = false;
					}
					temp = temp.next;
				}
		
			head.next.tick(head.oldX,head.oldY);
			
			temp = head.next;
			while (temp != null){
				if(head.x == temp.x && head.y== temp.y)
				{
					//System.out.println("HAI PERSO " + head.x + " " + head.y + " " + temp.x + " " + temp.y + " " + temp.counter);
					Game.isRunning = false;
				}
				temp = temp.next;
			}
		}
		if(!Game.isRunning)
		{
			System.out.println("GAME OVER");
			System.out.println("SCORE : " + tail.counter);
		}
		
	}
	
	public void render(Graphics g)
	{
		head.render(g);
	}
	
	/*public void renderBorder(Graphics g)
	{
		g.setColor(Color.red);
		g.drawRoundRect(head.x, head.y, head.width, head.height, 0, 0);
	}*/
}
