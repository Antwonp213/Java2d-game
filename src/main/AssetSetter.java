package main;

import entities.NPCs.Merchant;
import entities.NPCs.MinibossGuard;
import entities.NPCs.OldMan;
import entities.NPCs.PathFinderSlave;
import object.OBJ_Door;
import object.OBJ_Gold_Coin;
import object.OBJ_Health_Potion;
import object.OBJ_Key;
import object.OBJ_Treasure_Chest;

public class AssetSetter 
{	
	GameWindow gp;
	
	public AssetSetter(GameWindow gp)
	{
		this.gp = gp;
	}
	
	public void setObject()
	{
		gp.obj[0] = new OBJ_Health_Potion(gp);
		gp.obj[0].worldX = tile(23);
		gp.obj[0].worldY = tile(7);
		
		gp.obj[1] = new OBJ_Health_Potion(gp);
		gp.obj[1].worldX = tile(23);
		gp.obj[1].worldY = tile(40);
		
		gp.obj[2] = new OBJ_Key(gp);
		gp.obj[2].worldX = tile(37);
		gp.obj[2].worldY = tile(9);
		
		gp.obj[3] = new OBJ_Door(gp);
		gp.obj[3].worldX = tile(12);
		gp.obj[3].worldY = tile(12);
		
		gp.obj[4] = new OBJ_Treasure_Chest(gp);
		gp.obj[4].worldX = tile(12);
		gp.obj[4].worldY = tile(9);
		
		gp.obj[5] = new OBJ_Gold_Coin(gp);
		gp.obj[5].worldX = tile(38);
		gp.obj[5].worldY = tile(10);
		
		gp.obj[6] = new OBJ_Treasure_Chest(gp);
		gp.obj[6].worldX = tile(30);
		gp.obj[6].worldY = tile(29);
	}
	
	public void setNPC()
	{
		gp.NPC[0] = new OldMan(gp);
		gp.NPC[0].worldX = tile(22);
		gp.NPC[0].worldY = tile(12);
		
		gp.NPC[1] = new Merchant(gp);
		gp.NPC[1].worldX = tile(23);
		gp.NPC[1].worldY = tile(14);
		
		gp.NPC[2] = new MinibossGuard(gp);
		gp.NPC[2].worldX = tile(12) - 20;
		gp.NPC[2].worldY = tile(13) - 30;
		
		gp.NPC[3] = new PathFinderSlave(gp);
		gp.NPC[3].worldX = tile(23);
		gp.NPC[3].worldY = tile(40);
	}
	
	public void setMonster()
	{
		//Guaranteed events if the monster catches up to you.
	}
	
	//Use this to clear the Arrays based on the world
	public void clearArrays(int minIDX, int maxIDX)
	{
		for(int i = minIDX; i < maxIDX; i++)
		{
			gp.obj[i] = null;
			gp.NPC[i] = null;
			gp.Monster[i] = null;
		}
	}
	
	public int tile(int pos)
	{
		return pos * gp.tileSize;
	}
}
