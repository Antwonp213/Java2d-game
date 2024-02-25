package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.Player;
import main.GameWindow;

public class OBJ_Treasure_Chest extends Entity
{
//	GameWindow gp;

	public static final String OBJName = "Treasure Chest";
	
	public int chestID;
	
	public OBJ_Treasure_Chest(GameWindow gp)
	{
		super(gp);
		name = "Treasure Chest";
		collision = true;
		Down1 = setup("/Objects/Treasure Chest");
		
		interactable = true;
		
		solidArea.x = 0;
		solidArea.y = 40;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 80;
		solidArea.height = 40;
	}
	
	
	boolean setE = false;
	boolean gotLoot = false;
	@Override
	public void update()
	{
		if(checkInteraction(gp.Player) && (gp.Player.direction == "idleU" || gp.Player.direction == "up"))
		{
			if(gp.keyH.key.contains(gp.keyH.e))
			{
				setE = true;
			}
		}
		
		if(setE && !gp.keyH.key.contains(gp.keyH.e) && !gotLoot)
		{
			setE = false;
			
			giveLoot(chestID);
			
			gotLoot = true;
			
			System.out.println("Do something brah");
		}
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		BufferedImage image = null;
		
		image = returnImage(image);
		
		screenX = worldX - gp.Player.worldX + Player.screenX;
		screenY = worldY - gp.Player.worldY + Player.screenY;
		
		if(worldX + gp.tileSize > gp.Player.worldX - Player.screenX &&
		   worldX - gp.tileSize < gp.Player.worldX + Player.screenX &&
		   worldY + gp.tileSize > gp.Player.worldY - Player.screenY &&
		   worldY - gp.tileSize < gp.Player.worldY + Player.screenY)
		{
			g2.drawImage(image, screenX, screenY, null);
		}
	}
	
	public void giveLoot(int chest)
	{
		switch(chest)
		{
		default:
			gp.Player.gold += 1000;
			gp.oo.addMessage("You gained 1000 Gold");
			break;
		}
	}
}
