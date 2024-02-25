package data;

import data.DataStorage.EntityStorage;
import entities.Entity;
import entities.NPCs.Merchant;
import entities.NPCs.MinibossGuard;
import entities.NPCs.OldMan;
import main.GameWindow;
import main.Main;
import object.OBJ_Door;
import object.OBJ_Gold_Coin;
import object.OBJ_Health_Potion;
import object.OBJ_Key;
import object.OBJ_Treasure_Chest;

public class GenerateEntity 
{	
	private static final GameWindow gp = Main.gamePanel;
	
	public static void recreateOBJ(DataStorage ds, int idx)
	{
		System.out.println("Creating NPC: " + ds.obj[idx].name + " at idx: " + idx);
		switch(ds.obj[idx].name)
		{
		case object.OBJ_Health_Potion.OBJName:
			gp.obj[idx] = new OBJ_Health_Potion(gp);
			break;
		case object.OBJ_Door.OBJName:
			gp.obj[idx] = new OBJ_Door(gp);
			break;
		case object.OBJ_Key.OBJName:
			gp.obj[idx] = new OBJ_Key(gp);
			break;
		case object.OBJ_Treasure_Chest.OBJName:
			gp.obj[idx] = new OBJ_Treasure_Chest(gp);
			break;
		case object.OBJ_Gold_Coin.OBJName:
			gp.obj[idx] = new OBJ_Gold_Coin(gp);
			break;
		}
	}
	
	public static void recreateNPC(DataStorage ds, int idx)
	{
		System.out.println("Creating NPC: " + ds.NPC[idx].name);
		
		switch(ds.NPC[idx].name)
		{
		case entities.NPCs.OldMan.npcName:
			gp.NPC[idx] = new OldMan(Main.gamePanel);
			break;
		case entities.NPCs.Merchant.npcName:
			gp.NPC[idx] = new Merchant(Main.gamePanel);
			break;
		case entities.NPCs.MinibossGuard.npcName:
			gp.NPC[idx] = new MinibossGuard(Main.gamePanel);
			break;
		}
	}
	
	public static void recreateMonster(DataStorage ds, int idx)
	{
		
	}
	
	public static void recreateEntity(Entity[] entity, EntityStorage[] entityS, int idx)
	{
		
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void createEntity(Entity[] entity, String name, int idx)
	{
		switch(name)
		{
		case entities.NPCs.OldMan.npcName:
			entity[idx] = new OldMan(Main.gamePanel);
			break;
		case entities.NPCs.Merchant.npcName:
			entity[idx] = new Merchant(Main.gamePanel);
			break;
		case entities.NPCs.MinibossGuard.npcName:
			entity[idx] = new MinibossGuard(Main.gamePanel);
			break;
		}
	}
}
