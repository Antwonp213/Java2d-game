package entities.NPCs;

import entities.Entity;
import entities.EntityInterface;
import main.GameWindow;
import main.KeyHandler;

public class PathFinderSlave extends Entity implements EntityInterface
{
	GameWindow gp;
	
	public static final String npcName = "Pathfinder Slave";
	
	public PathFinderSlave(GameWindow gp) 
	{
		super(gp);
		
		this. gp = gp;
		
		speed = 0; 
		spriteNum = 1;
		frameCount = 2;
		
		detector = true;
		
		name = npcName;
		
		Folder = "/NPCs/Guard/";
		
		getImages(Folder);
		
		setSolidArea();
	}
	
	@Override
	public String getDialogue() 
	{
		return null;
	}
	
	boolean up; boolean down; boolean left; boolean right;
	public void setAction()
	{
		searchPath(36, 39);
		
		if(gp.keyH.key.contains(KeyHandler.up))
		{
			up = true;
		}
		else if(!gp.keyH.key.contains(KeyHandler.up) && up)
		{
			worldY -= 80;
			up = false;
		}
		
		if(gp.keyH.key.contains(KeyHandler.down))
		{
			down = true;
		}
		else if(!gp.keyH.key.contains(KeyHandler.down) && down)
		{
			worldY += 80;
			down = false;
		}
		
		if(gp.keyH.key.contains(KeyHandler.left))
		{
			left = true;
		}
		else if(!gp.keyH.key.contains(KeyHandler.left) && left)
		{
			worldX -= 80;
			left = false;
		}
		
		if(gp.keyH.key.contains(KeyHandler.right))
		{
			right = true;
		}
		else if(!gp.keyH.key.contains(KeyHandler.right) && right)
		{
			worldX += 80;
			right = false;
		}	
	}
}
