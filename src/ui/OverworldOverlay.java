package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import main.GameWindow;

public class OverworldOverlay extends UI
{
	public OverworldOverlay(GameWindow gp)
	{
		super(gp);
	}
	
	//Player
	public void drawStaminaBar(Graphics2D g2, int opacity)
	{
		//Calculate stamina bar position and width as a percentage in 80
		int Opacity = opacity * 3 > 255 ? 255 : (opacity * 3);
		int StaminaBar = (int) (entities.Player.stamina * (double) gp.tileSize/gp.Player.maxStamina);
		int centerX = entities.Player.screenX;
		int centerY = entities.Player.screenY - 20;
		//Stamina bar
		g2.setColor(new Color(0, 0, 0, Opacity));
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(centerX - 2, centerY - 2, gp.tileSize + 3, 15);
		g2.setColor(new Color(0, 0, 0, (int) (100 / ((double) 255/Opacity))));
		g2.fillRect(centerX, centerY, gp.tileSize, 12);
		g2.setColor(new Color(64, 150, 45, Opacity));
		g2.fillRect(centerX, centerY, StaminaBar, 12);
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//DEBUG
	public class Message 
	{
		int msgTimer;
		String msg;
		
		public Message(String msg)
		{
			this.msg = msg;
			
			msgTimer = (5 * 60);
		}
	}
	
	ArrayList<Message> overlayMsg = new ArrayList<Message>();
	
	public void drawPath(Graphics2D g2)
	{
		g2.setColor(new Color(100, 100, 100, 150));
		
		for(int i = 0; i < gp.pFinder.pathList.size(); i++)
		{
			int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
			int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
			int screenX = worldX - gp.Player.worldX + entities.Player.screenX;
			int screenY = worldY - gp.Player.worldY + entities.Player.screenY;
			
			g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
		}
	}
	
	public void showCoordinates(Graphics2D g2)
	{
		g2.setColor(Color.white);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		int x = gp.Player.worldX + gp.Player.solidArea.x;
		int y = gp.Player.worldY + gp.Player.solidArea.y;
		g2.drawString("Coordinate: (" + x + ", " + y + ")", 20, 20);
		g2.drawString("Tile: (" + x/gp.tileSize + ", " + y/gp.tileSize + ")", 50, 40);
		
//		g2.fillRect(gp.Player.screenY, gp.Player.screenY, 5, 5);
	}
	
	public void addMessage(String message)
	{
		Message msg = new Message(message);
		System.out.println("Adding message");
		
		overlayMsg.add(msg);

//		printList(debugMsg);
	}
	
	public void displayMessage(Graphics2D g2)
	{
		if(!overlayMsg.isEmpty())
		{
			for(int i = 0; i < overlayMsg.size(); i++)
			{
				overlayMsg.get(i).msgTimer--;
				if(overlayMsg.get(i).msgTimer == 0)
				{
					overlayMsg.remove(i);
					
//					printList(debugMsg);
					
					break;
				}
				
				int opacity = overlayMsg.get(i).msgTimer * 3 > 255 ? 255 : overlayMsg.get(i).msgTimer * 3;
				
				g2.setColor(new Color(255, 255, 255, opacity));
				g2.setFont(new Font("Times New Roman", Font.PLAIN, 20));
				
				int msgX = 50;
				int msgY = 20 + (i * 40);
				
				if(gp.keyH.COORDINATE)
				{
					msgY = 60 + (i * 40);
					
					g2.drawString(overlayMsg.get(i).msg, msgX, msgY);
				}
				else
				{
					g2.drawString(overlayMsg.get(i).msg, msgX, msgY);
				}
			}
		}
	}
	
	public void printList(ArrayList<Message> list)
	{
		if(!list.isEmpty())
		{
			System.out.print("[");
			for(int i = 0; i < list.size(); i++)
			{
				if(i == overlayMsg.size() - 1)
				{
					System.out.print(list.get(i).msg + "]");
				}
				else
				{
					System.out.print(list.get(i).msg + ", ");	
				}
			}
			System.out.println("");
		}
		else
		{
			System.out.println("[]");
		}
	}
}
