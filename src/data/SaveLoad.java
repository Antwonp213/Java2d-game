package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import data.DataStorage.EntityStorage;
import entities.Entity;
import main.GameWindow;

public class SaveLoad
{	
	GameWindow gp;
	
	public static boolean local;
	
	static int autoSaveTimer = 0;
	public static int autoSaveTimerLimit = 36000; //Changes based on options in title menu. Default, once every 10 minutes
	
	File saveFile;
	File userPref;
	
	public SaveLoad(GameWindow gp)
	{
		this.gp = gp;
	}
	
	public boolean checkSave(int fileNum)
	{
		getSave(fileNum);
		
		if(saveFile.exists())
		{
			return true;
		}
		
		return false;
	}
	
	public void deleteSave(int fileNum)
	{
		getSave(fileNum);
		
		if(saveFile.exists())
		{
			saveFile.delete();
		}
		else
		{
			System.err.println("Save does not exist for " + fileNum);
		}
	}
	
	public void save(int fileNum)
	{
		getSave(fileNum);
		
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile));
			
			DataStorage ds = new DataStorage();
			
			System.out.println("Saving Game");
			
			//SYSTEM stuff
			saveUserPref();
			//Player position
			ds.PlayerWorldX = gp.Player.worldX;
			ds.PlayerWorldY = gp.Player.worldY;
			switch(gp.Player.direction)
			{
			case "idleU":
			case "up":
				ds.playerDir = "idleU";
				break;
			case "idleD":
			case "down":
				ds.playerDir = "idleD";
				break;
			case "idleR":
			case "upR":
			case "downR":
			case "right":
				ds.playerDir = "idleR";
				break;
			case "idleL":
			case "upL":
			case "downL":
			case "left":
				ds.playerDir = "idleL";
				break;
			}
			//NPC position
			ds.NPC = new EntityStorage[gp.NPC.length];
			ds.obj = new EntityStorage[gp.obj.length];
			ds.Monster = new EntityStorage[gp.Monster.length];
			
			for(int i = 0; i < ds.NPC.length; i++)
			{
				if(gp.NPC[i] != null)
				{
					ds.NPC[i] =	ds.new EntityStorage();
					ds.NPC[i].name = gp.NPC[i].name;
					ds.NPC[i].waypoint = gp.NPC[i].waypoint;
					ds.NPC[i].wait = gp.NPC[i].wait;
					ds.NPC[i].lockWaypoint = gp.NPC[i].lockWaypoint;
					ds.NPC[i].onPath = gp.NPC[i].onPath;
					ds.NPC[i].worldX = gp.NPC[i].worldX;
					ds.NPC[i].worldY = gp.NPC[i].worldY;
					ds.NPC[i].direction = gp.NPC[i].direction;
				}
			}
			
			for(int i = 0; i < ds.obj.length; i++)
			{
				if(gp.obj[i] != null)
				{
					ds.obj[i] = ds.new EntityStorage();
					
					ds.obj[i].name = gp.obj[i].name;	
					ds.obj[i].worldX = gp.obj[i].worldX;
					ds.obj[i].worldY = gp.obj[i].worldY;
					
					ds.obj[i].spriteNum = gp.obj[i].spriteNum;
					ds.obj[i].collision = gp.obj[i].collision;
				}
			}
			
			for(int i = 0; i < ds.Monster.length; i++)
			{
				if(gp.Monster[i] != null)
				{
					ds.Monster[i] = ds.new EntityStorage();
					ds.Monster[i].worldX = gp.Monster[i].worldX;
					ds.Monster[i].worldY = gp.Monster[i].worldY;
					ds.Monster[i].direction = gp.Monster[i].direction;
				}
			}
			//Questline
			
			//Events
			
			//Player stats
			ds.playerStam = entities.Player.stamina;
			
			//Companion stats
			
			//Player inventory
			
			oos.writeObject(ds);
			
			oos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void load(int fileNum)
	{
		getSave(fileNum);
		
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile));
			
			DataStorage ds = (DataStorage) ois.readObject();
			
			//SYSTEM stuff
			loadUserPref();
			//Player position
			gp.Player.worldX = ds.PlayerWorldX;
			gp.Player.worldY = ds.PlayerWorldY;
			gp.Player.direction = ds.playerDir;
			
			//NPC position

			for(int i = 0; i < ds.NPC.length; i++)
			{
				if(ds.NPC[i] != null)
				{
					if(gp.NPC[i] == null)
					{
						GenerateEntity.recreateNPC(ds, i);
					}
					
					gp.NPC[i].worldX = ds.NPC[i].worldX;
					gp.NPC[i].worldY = ds.NPC[i].worldY;
					gp.NPC[i].direction = ds.NPC[i].direction;
					gp.NPC[i].waypoint = ds.NPC[i].waypoint;
					gp.NPC[i].wait = ds.NPC[i].wait;
					gp.NPC[i].lockWaypoint = ds.NPC[i].lockWaypoint;
					gp.NPC[i].onPath = ds.NPC[i].onPath;
				}
				else if(ds.NPC[i] == null && gp.NPC[i] != null)
				{
					gp.NPC[i] = null;
				}
			}
			
			for(int i = 0; i < ds.obj.length; i++)
			{
				if(ds.obj[i] != null)
				{
					if(gp.obj[i] == null)
					{
						GenerateEntity.recreateOBJ(ds, i);
					}
					
					gp.obj[i].worldX = ds.obj[i].worldX;
					gp.obj[i].worldY = ds.obj[i].worldY;
					gp.obj[i].spriteNum = ds.obj[i].spriteNum;
					gp.obj[i].collision = ds.obj[i].collision;
				}
				else if(ds.obj[i] == null && gp.obj[i] != null)
				{
					gp.obj[i] = null;
				}
			}

			for(int i = 0; i < ds.Monster.length; i++)
			{
				if(ds.Monster[i] != null)
				{
					if(gp.Monster[i] == null)
					{
						GenerateEntity.recreateMonster(ds, i);
					}
					gp.Monster[i].worldX = ds.Monster[i].worldX;
					gp.Monster[i].worldY = ds.Monster[i].worldY;
					gp.Monster[i].direction = ds.Monster[i].direction;
				}
				else if(ds.Monster[i] == null && gp.Monster[i] != null)
				{
					gp.Monster[i] = null;
				}
			}
			
			//Questline
			
			//Events
			
			//Player stats
			entities.Player.stamina = ds.playerStam;
			
			//Companion stats
			
			//Player inventory
			
			ois.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void autoSave(int fileNum)
	{
		if(gp.gameState == gp.gameState)
		{
			autoSaveTimer++;
			if(autoSaveTimer == autoSaveTimerLimit)
			{
				autoSaveTimer = 0;
				save(0);
			}
		}
		else
		{
			if(gp.gameState == gp.combatOverState)
			{
				autoSaveTimer = 0;
				save(0);
			}
		}
	}
	
	public void getSave(int fileNum)
	{
		if(!local)
		{
			if(fileNum == 0)
			{
				saveFile = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/My Games/2D JRPG/Auto_Save.dat");
			}
			else
			{
				saveFile = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/My Games/2D JRPG/Save" + fileNum + ".dat");
			}
			
			File directory = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/My Games/2D JRPG");
			
			if(!directory.exists())
			{
				directory.mkdirs();
			}
		}
		else
		{
			if(fileNum == 0)
			{
				saveFile = new File("SaveFiles/Auto_Save.dat");
			}
			else
			{
				saveFile = new File("SaveFiles/Save" + fileNum + ".dat");
			}
		}
	}
	
	public void reloadEntity(Entity[] entity, EntityStorage[] entityS)
	{
		for(int i = 0; i < entityS.length; i++)
		{
			if(entityS[i] != null)
			{
				if(entity[i] == null)
				{
					GenerateEntity.recreateEntity(entity, entityS, i);
				}
				entity[i].worldX = entityS[i].worldX;
				entity[i].worldY = entityS[i].worldY;
				entity[i].direction = entityS[i].direction;
			}
			else if(entityS[i] == null && entity[i] != null)
			{
				entity[i] = null;
			}
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//USER PREFERENCES
	public void loadUserPref()
	{
		userPref = new File("SaveFiles/userPref.dat");
		
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userPref));
			
			UserPreferences up = (UserPreferences) ois.readObject();
			
			local = up.local;
			gp.fullScreen = up.fullScreen;
			gp.ts.knobX = up.volumeKnob;
			gp.ts.MUTE = up.mute;
			gp.keyH.drawFPS = up.displayFPS;
			
			gp.DEBUG = up.debug;
			gp.combatEnabled = up.combatEnabled;
			gp.displayDebugMsg = up.displayDebugMsg;
			gp.collisionEnabled = up.collisionEnabled;
			
			ois.close();
		}
		catch(Exception e)
		{	
			if(!userPref.exists())
			{
				System.err.println("This file does not exist, creating standard prefs and saving.");
				
				try
				{
					userPref.createNewFile();
					System.out.println("User preferences created!");
				}
				catch(Exception e2)
				{
					System.err.println("Attempting to save to main directory... Not really good!");
					
					try
					{
						JOptionPane.showMessageDialog(gp, "The directory could not be made! Saving to main runnable.");
						userPref = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/My Games/2D JRPG/");
						userPref.mkdirs();
						userPref = new File("userPref.dat");
						userPref.createNewFile();
						JOptionPane.showMessageDialog(gp, "The file was saved! Horray!");
					}
					catch(Exception e3)
					{
						System.err.println("There is no god here...");
						e3.printStackTrace();
						System.exit(0);
					}
				}
				
				try
				{
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userPref));
					
					UserPreferences up = new UserPreferences();
					
					up.local = true;
					up.fullScreen = false;
					up.volumeKnob = 470;
					up.mute = false;
					up.displayFPS = false;
					
					up.debug = true;
					up.combatEnabled = true;
					up.collisionEnabled = true;
					up.displayDebugMsg = true;
					
					oos.writeObject(up);
					
					oos.close();
					
					loadUserPref();
					return;
				}
				catch(Exception e2)
				{
					System.err.println("There is no god here...");
					e2.printStackTrace();
					System.exit(0);
				}
			}
			else
			{
				System.err.println("This file does exist, but there's an error. Check stackTrace");
				e.printStackTrace();
			}
		}
	}
	
	public void saveUserPref()
	{
		userPref = new File("SaveFiles/userPref.dat");
		
		if(!userPref.exists())
		{
			try
			{
				userPref.createNewFile();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userPref));
			
			UserPreferences up = new UserPreferences();
			
			up.local = local;
			up.fullScreen = gp.fullScreen;
			up.volumeKnob = gp.ts.knobX;
			up.mute = gp.ts.getMute();
			up.displayFPS = gp.keyH.drawFPS;
						
			up.debug = gp.DEBUG;
			up.combatEnabled = gp.combatEnabled;
			up.collisionEnabled = gp.collisionEnabled;
			up.displayDebugMsg = gp.displayDebugMsg;
			
			oos.writeObject(up);
			
			oos.close();
		}
		catch(Exception e)
		{
			System.err.println("This file is corrupted.");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	//Only loads the most important information from save file
	public void loadTemp(int fileNum, DataStorage ds)
	{
		getSave(fileNum);
		
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile));
			
			ds = (DataStorage) ois.readObject();
			
			
			
			ois.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
