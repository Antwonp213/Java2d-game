package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.Player;
import main.GameWindow;

public class OBJ_Gold_Coin extends Entity
{
	public final static String OBJName = "Gold Coin";
	
	public BufferedImage[] spin;
	
	GameWindow gp;
	public OBJ_Gold_Coin(GameWindow gp)
	{
		super(gp);
		this.gp = gp;
		
		name = "Gold Coin";
		
		spin = new BufferedImage[12];
		
		for(int i = 0; i < spin.length; i++)
		{
			if(i < 10)
			{
				spin[i] = setup("/Objects/GoldCoin/Frame_0" + i);
			}
			else
			{
				spin[i] = setup("/Objects/GoldCoin/Frame_" + i);
			}
		}
		
		collision = false;
		
		solidArea.x = 30;
		solidArea.y = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 19;
		solidArea.height = 19;
		
//		solidArea.x = 25;
//		solidArea.y = 25;
//		solidAreaDefaultX = solidArea.x;
//		solidAreaDefaultY = solidArea.y;
//		solidArea.width = 29;
//		solidArea.height = 29;
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		BufferedImage image = null;
		
		screenX = worldX - gp.Player.worldX + Player.screenX;
		screenY = worldY - gp.Player.worldY + Player.screenY;
		
		if(worldX + gp.tileSize > gp.Player.worldX - Player.screenX &&
		   worldX - gp.tileSize < gp.Player.worldX + Player.screenX &&
		   worldY + gp.tileSize > gp.Player.worldY - Player.screenY &&
		   worldY - gp.tileSize < gp.Player.worldY + Player.screenY)
		{
			g2.drawImage(returnImage(image), screenX, screenY, null);			
		}
	}
	
	int frameCount = 0;
	int idx = 0;
	@Override
	public BufferedImage returnImage(BufferedImage image)
	{
		frameCount++;
		if(frameCount >= 5)
		{
			frameCount = 0;
			idx++;
			
			if(idx > spin.length - 1)
			{
				idx = 0;
			}
		}
		
		return spin[idx];
	}
}
