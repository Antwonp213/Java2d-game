package entities.NPCs;

import java.awt.Graphics2D;

import javax.imageio.ImageIO;

import entities.Entity;
import entities.EntityInterface;
import entities.Player;
import main.GameWindow;

public class Merchant extends Entity implements EntityInterface
{
	Player Player;
	String Folder;
	
	public static final String npcName = "Merchant";
	
	public Merchant(GameWindow gp)
	{
		super(gp);
		
		Player = gp.Player;
		
		speed = 3; 
		spriteNum = 1;
		frameCount = 2;
		
		detector = true;
		
		name = npcName;
		
		Folder = "/NPCs/Merchant/";
		
		getImages(Folder);
		
		setSolidArea();
		
		ico = new icon();
		
		try
		{
			ico.icon = ImageIO.read(getClass().getResourceAsStream(Folder + "icon.png"));
		}
		catch(Exception e)
		{
			System.err.println("Image does not exist for Icon");
			System.exit(0);
		}
		
		ico.iconPresent = true;
	}

	@Override
	public String getDialogue() 
	{
		
		return null;
	}

	private boolean setE = false;
	private boolean skipW = false;
	int goalCol; int goalRow;
	@Override
	public void setAction() 
	{
		if(checkRadius(Player, 3))
		{	
			if(Player.worldX < (worldX - gp.tileSize/2))
			{
				direction = "idleL";
			}
			else if(Player.worldX > (worldX + gp.tileSize/2))
			{
				direction = "idleR";
			}
			else if(Player.worldY > worldY)
			{
				direction = "idleD";
			}
			else if(Player.worldY < (worldY + gp.tileSize * 2))
			{
				direction = "idleU";
			}
			
			if(checkInteraction(Player))
			{
				if(gp.keyH.key.contains(gp.keyH.e))
				{
					setE = true;
				}
				
				if(setE && !gp.keyH.key.contains(gp.keyH.e))
				{
					gp.oo.addMessage("You interacted with the merchant");
					gp.gameState = gp.merchantState;
					setE = false;
				}
			}
		}
//		else
//		{
//			//TODO: Make waypoints and go to them.
//			if(!lockWaypoint)
//			{
//				System.out.println("Waypoint set to " + waypoint);
//				//Set waypoint 
//				switch(waypoint)
//				{
//				case 0:
//					goalCol = 23; goalRow = 12;
//					skipW = false;
//					break;
//				case 1:
//					goalCol = 23; goalRow = 21;
//					skipW = true;
//					break;
//				case 2:
//					goalCol = 15; goalRow = 21;
//					skipW = false;
//					break;
//				case 3:
//					goalCol = 23; goalRow = 21;
//					skipW = true;
//					break;
//				case 4:
//					goalCol = 23; goalRow = 40;
//					skipW = false;
//					break;
//				case 5:
//					goalCol = 36; goalRow = 39;
//					skipW = false;
//					break;
//				case 6:
//					goalCol = 36; goalRow = 18;
//					skipW = true;
//					break;
//				case 7:
//					goalCol = 38; goalRow = 18;
//					skipW = true;
//					break;
//				case 8:
//					goalCol = 38; goalRow = 10;
//					skipW = false;
//					break;
//				case 9:
//					goalCol = 38; goalRow = 12;
//					skipW = true;
//					break;
//				}
//				
//				onPath = true;
//				lockWaypoint = true;
//			}
//			else
//			{
//				if(onPath)
//				{
//					searchPathTest(goalCol, goalRow);
//				}
//				else
//				{
//					wait++;
//					if(wait <= 60)
//					{
//						if(skipW)
//						{
//							wait = 900;
//						}
//						
//						direction = "idleD";
//						return;
//					}
//					
//					wait = 0;
//					
//					waypoint++;
//					if(waypoint == 10)
//					{
//						waypoint = 0;
//					}
//					
//					lockWaypoint = false;
//				}
//			}
//			
//		}
	}
	
	public void playSpecialAnim(Graphics2D g2)
	{
		
	}
}
