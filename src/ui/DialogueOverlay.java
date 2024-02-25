package ui;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GameWindow;

public class DialogueOverlay extends UI
{
	GameWindow gp;
	public DialogueOverlay(GameWindow gp)
	{
		super(gp);
		this.gp = gp;
	}
	
	public void update()
	{
		
	}
	
	public void draw(Graphics2D g2)
	{
		gp.drawPlayScreen(g2);
		
		g2.setColor(Color.black);
		g2.fillRoundRect(0, 500, gp.screenWidth - 5, 220, 5, 5);
		g2.setColor(new Color(200, 200, 200));
		g2.drawRoundRect(0, 500, gp.screenWidth - 5, 220, 5, 5);
	}
}
