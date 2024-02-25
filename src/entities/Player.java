package entities;

import java.awt.Rectangle;
import java.util.Random;

import main.GameWindow;
import main.KeyHandler;
import main.Sound;

public class Player extends Entity
{
	KeyHandler keyH;
	public static int stamina = 100;
	public static int screenX;
	public static int screenY;
	public int maxStamina = 100;
	
	
	//Inventory stuff
	Inventory inv = new Inventory();
	//Temp variables
	public int invKeys = 0;
	public int invPotions = 0;
	public int gold = 0;
	
	//Combat Variables
	int threshold = 1;
	
	public Player(GameWindow gp, KeyHandler keyH)
	{
		super(gp);
		this.keyH = keyH;
		
		getPlayerImage();
		
		//Collision Rectangle
		solidArea = new Rectangle();
		
		setSolidArea();
		
		screenX = (gp.screenWidth/2) - (gp.tileSize/2);
		screenY = (gp.screenHeight/2) - (gp.tileSize/2);

		stamina = 100;
		worldX = 23 * gp.tileSize;
		worldY = 12 * gp.tileSize;
//		worldX = 23 * gp.tileSize;
//		worldY = 21 * gp.tileSize - 20;
		speed = 4;
	}
	
	public void getPlayerImage()
	{
		String Folder = "/Player/up_walk";
		Up1 = spSetup(Folder + "1");
		Up2 = spSetup(Folder + "2");
		
		Folder = "/Player/down_walk";
		Down1 = spSetup(Folder + "1");
		Down2 = spSetup(Folder + "2");
		
		Folder = "/Player/left_walk";
		Left1 = spSetup(Folder + "1");
		Left2 = spSetup(Folder + "2");
		
		Folder = "/Player/right_walk";
		Right1 = spSetup(Folder + "1");
		Right2 = spSetup(Folder + "2");
		
		Folder = "/Player/";
		UpIdle = spSetup(Folder + "up_stand");
		DownIdle = spSetup(Folder + "down_stand");
		RightIdle = spSetup(Folder + "right_stand");
		LeftIdle = spSetup(Folder + "left_stand");
	}

	public void update()
	{
		int prevX = worldX;
		int prevY = worldY; //Used for checking if a battle occurs
//		setSolidArea(direction); //Set solidArea based on sprite image
		
		spriteCounter++;	
		if(spriteCounter >= 20)
		{
			spriteNum++;
			if(spriteNum >= 3)
			{
				spriteNum = 1;
			}
			spriteCounter = 0;
		}
		
		if(onPath)
		{
			onPath = false;
//			searchPath(10, 12);
		}
		if(true)
		{	
			//Set player orientation in relation to key pressed
			if(keyH.UP && keyH.LEFT)
			{
				direction = "upL";
			}
			else if(keyH.UP && keyH.RIGHT)
			{
				direction = "upR";
			}
			else if(keyH.DOWN && keyH.LEFT)
			{
				direction = "downL";
			}
			else if(keyH.DOWN && keyH.RIGHT)
			{
				direction = "downR";
			}
			else if(keyH.UP)
			{
				direction = "up";
			}
			else if(keyH.DOWN)
			{
				direction = "down";
			}
			else if(keyH.LEFT)
			{
				direction = "left";
			}
			else if(keyH.RIGHT)
			{
				direction = "right";
			}
			else if((keyH.RIGHT && keyH.LEFT) || (keyH.UP && keyH.DOWN))
			{
				direction = "none";
			}
			
			if(keyH.SHIFT && !(direction == "idleD" || direction == "idleR" || direction == "idleL" || direction == "idleU"))
			{
				//Consume stamina
				speed = stamina > 0 ? 8 : 4;
				stamina -= stamina > 0 ? 1 : 0;
			}
			else
			{
				//Regenerate stamina overtime
				stamina += stamina < maxStamina ? 1 : 0;
				speed = 4;
			}
		}
		
		
		
		//Check for collisions with incoming tile
		collisionOn = false;
		gp.cChecker.checkTile(this);
		
		int objIndex = gp.cChecker.checkObject(this);
		if(objIndex != -1)
		{
			pickUpObject(objIndex);
		}
		int idx = gp.cChecker.checkEntity(this, gp.NPC);
		if(idx != -1)
		{
			collisionOn = true;
		}
		idx = gp.cChecker.checkEntity(this, gp.Monster);
		if(idx != -1)
		{
			collisionOn = true;
		}
		
		if(!gp.collisionEnabled)
		{
			collisionOn = false;
		}
		
		if(collisionOn == false)
		{
			switch(direction)
			{
			case "up": worldY -= speed; break;
			case "down": worldY += speed; break;
			case "right": worldX += speed; break;
			case "left": worldX -= speed; break;
			case "upL": worldY -= speed; worldX -= speed; break;
			case "downL": worldY += speed; worldX -= speed; break;
			case "upR": worldY -= speed; worldX += speed; break;
			case "downR": worldY += speed; worldX += speed; break;
			case "none": break;
			}
		} 
		
		if(!(keyH.RIGHT || keyH.LEFT || keyH.UP || keyH.DOWN)) //None of the key flags are true
		{
			//Find which Idle frame to put the Player in.
			if(direction == "downR" || direction ==  "upR")
			{
				direction = "idleR";
			}
			else if(direction == "downL" || direction == "upL")
			{
				direction = "idleL";
			}
			else if(direction == "right")
			{
				direction = "idleR";
			}
			else if(direction == "left")
			{
				direction = "idleL";
			}
			else if(direction == "down")
			{
				direction = "idleD";
			}
			else if(direction == "up")
			{
				direction = "idleU";
			}
		}
		
		//Check if the player moved, and calculate the chance of entering a battle
		if(((prevX != worldX) || (prevY != worldY)) && gp.combatEnabled)
		{
			//Calculate battle chance, value threshold goes higher
			Random battleChance = new Random();
			int chance = battleChance.nextInt(250000) + 1; //From 1 to 500,000
			
			if(chance < threshold) //0.1% chance intitially, increases linearly per step
			{
				//Seems to never trigger above 1000...
				threshold = 1;
				if(gp.combatEnabled)
				{
					gp.gameState = gp.combatTransitionState;	
				}
			}
			else
			{
				if(keyH.SHIFT == false)
				{
					threshold++;
				}
				else
				{
					threshold += 2; //Double the speed, double the accumulation. yee haw
				}
			}
		}
	}
	
	public void pickUpObject(int i)
	{
		String objectName = gp.obj[i].name;
			
		switch(objectName)
		{
		case "Key":
			gp.playSE(Sound.ItemPickup);
			invKeys++;
			gp.obj[i] = null;
			break;
		case "Door":
			if(invKeys > 0)
			{
				gp.playSE(Sound.DoorOpen);
				invKeys--;
				gp.obj[i].spriteNum = 2;
				gp.obj[i].collision = false;
			}
			else
			{
				if(gp.obj[i].collision == true)
				{
					this.collisionOn = true;	
				}
			}
			break;
		case "Health Potion":
			gp.playSE(Sound.ItemPickup);
			invPotions++;
			gp.obj[i] = null;
			break;
		case "Treasure Chest":
			this.collisionOn = true;
			break;
		case "Gold Coin":
			gp.obj[i] = null;
			gold += 100;
			gp.oo.addMessage("You got 100 gold");
			gp.playSE(Sound.MoneyPickup);
			break;
		}
			//In the future, set flag to reorder object list, and then call to method that does it.
	}	
	
	public class Inventory
	{
		public int gold;
		
		public void addItem(int itemID)
		{
			
		}
		
		public void removeItem(int itemID)
		{
			
		}
	}
}
