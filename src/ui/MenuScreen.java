package ui;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GameWindow;

public class MenuScreen extends UI
{	
	public MenuScreen(GameWindow gp)
	{
		super(gp);
	}
	
	
	boolean debounce = false;
	public void update()
	{
		if(gp.keyH.key.contains(gp.keyH.esc))
		{
			debounce = true;
		}
		
		if(debounce && !gp.keyH.key.contains(gp.keyH.esc))
		{
			debounce = false;
			gp.keyH.TAB = false;
			gp.keyH.MENU = false;
			gp.gameState = gp.playState;
		}
	}
	
	public void draw(Graphics2D g2)
	{
		g2.setColor(Color.white);
		g2.drawString("Menu haha", 600, 360);
	}
}
