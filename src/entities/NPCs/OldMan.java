package entities.NPCs;

import entities.Entity;
import entities.EntityInterface;
import entities.Player;
import main.GameWindow;

public class OldMan extends Entity implements EntityInterface
{
	Player Player;
	String Folder;

	public static final String npcName = "Old Man";
	
	public OldMan(GameWindow gp)
	{
		super(gp);
		
		Player = gp.Player;
		
		speed = 2; 
		spriteNum = 1;
		frameCount = 2;
		
		detector = true;
		
		name = npcName;
		
		Folder = "/NPCs/Oldman/";
		
		getImages(Folder);
		
		setSolidArea();
	} 
	
	boolean debounce = false;
	public void setAction()
	{	
		boolean interact = gp.keyH.key.contains(gp.keyH.e);
		
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
				if(interact)
				{
					debounce = true;
				}
				
				if(!interact && debounce)
				{
					debounce = false;
					gp.oo.addMessage("You're talking to an old man. Nice.");
					gp.gameState = gp.dialogueState;
				}
			}
		}
		else
		{
			super.setAction();	
		}
	}

	@Override
	public String getDialogue() 
	{
		
		return null;
	}
}

