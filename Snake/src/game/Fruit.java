package game;

import java.awt.Rectangle;
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;

public class Fruit extends Rectangle
{
	public Rectangle section;
	static int width = 16;
	static int height = 16;
	public int x;
	public int y;
	Random random = new Random();
	
	public Fruit()
	{
		
		this.x = random.nextInt(Game.WIDTH)-width-4;
		this.y = random.nextInt(Game.HEIGHT)-height-4;
		if(this.x < 0)
		{
			this.x = 4;
		}
		if(this.y < 0)
		{
			this.y = 4;
		}
		setBounds(x, y, width, height);
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(this.x, this.y, this.width, this.height);
	}
	
	public boolean tick(SnakeSection head)
	{
		if(this.intersects(head.x, head.y, head.width, head.height))
		{
			this.x = random.nextInt(Game.WIDTH)-width-4;
			this.y = random.nextInt(Game.HEIGHT)-height-4;
			if(this.x < 0)
			{
				this.x = 4;
			}
			if(this.y < 0)
			{
				this.y = 4;
			}
			setBounds(x, y, width, height);
			return true;
			
		}
		return false;
	}





}
