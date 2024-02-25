package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GameWindow;
import main.UtilityTool;

public class Entity 
{	
	public ArrayList<String> dialogue = new ArrayList<String>();
	public String Folder;
	
	public GameWindow gp;
	protected Random rand = new Random();
	//MOVABLE ENTITIES, IE PLAYER, NPCS, & MONSTERS
	public int waypoint = 0; public int wait;
	public boolean lockWaypoint = false;
	public boolean onPath = false;
	public boolean follow = false;
	public boolean interaction = false;	
	public boolean interactable = false;
	public boolean detect = false;
	public boolean detector = false;
	//VARIABLES FOR CAMERA
	public int screenX, screenY, worldX, worldY, speed;
	public int spriteNum = 1; 
	public int spriteCounter = 0;
	public int frameCount; //Set this in NPC class, VERY important. Limit of 6.
	public int actionLocker; //Locks the action of the NPC for however many frames
	//X: X-coordinate, Y: Y-coordinate, Speed: How fast x & y change, spriteNum: What frame we are on the sprite, spriteCounter: How many times we change x & y before changing frames
	public BufferedImage Up1, Up2, UpIdle;
	public BufferedImage Down1, Down2, DownIdle;
	public BufferedImage Right1, Right2, RightIdle;
	public BufferedImage Left1, Left2, LeftIdle;
	public BufferedImage Pose1, Pose2, Pose3;
	public String direction = "down";
	
	//VARIABLES FOR COLLISION
	public Rectangle solidArea = new Rectangle(20, 40, 40, 40);
	public boolean collisionOn = false;
	public int solidAreaDefaultX = solidArea.x;
	public int solidAreaDefaultY = solidArea.y;
	
	//IMMOBILE ENTITES, IE OBJECTS
	public icon ico;
	public BufferedImage image, image2, image3; //Use other images if needed. Probably not though.
	public String name;
	public boolean collision = false;
	
	public Entity(GameWindow gp)
	{
		this.gp = gp;
	}
	
	public BufferedImage setup(String imagePath)
	{
		UtilityTool uTool = new UtilityTool();
		BufferedImage scaledImage = null;
		
		try
		{
			scaledImage = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			scaledImage = uTool.scaleImage(scaledImage, gp.tileSize, gp.tileSize);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return scaledImage;
	}
	
	public BufferedImage spSetup(String imagePath)
	{
		UtilityTool uTool = new UtilityTool();
		BufferedImage scaledImage = null;
		
		try
		{
			scaledImage = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			int width = scaledImage.getWidth() * 3;
			int height = scaledImage.getHeight() * 3;
			scaledImage = uTool.scaleImage(scaledImage, width, height);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return scaledImage;
	}
	
	public void setSolidArea()
	{	
		int offsetX = Up1.getWidth()/4;
		int offsetY = Up1.getHeight()/2;
		
		solidArea.width = Up1.getWidth() - (offsetX * 2);
		solidArea.height= Up1.getHeight() - offsetY;
		
		if(Up1.getWidth() != Up1.getHeight())
		{
			offsetX += Math.abs((gp.tileSize - Up1.getWidth())/2);
			offsetY += Math.abs((gp.tileSize - Up1.getHeight())/2);
		}
		
		solidArea.x = offsetX;
		solidArea.y = offsetY;
		
		solidAreaDefaultX = offsetX;
		solidAreaDefaultY = offsetY;
	}
	
	//NPC & Monster specific methods
	public void draw(Graphics2D g2)
	{
		BufferedImage image = null;
		
		image = returnImage(image);
		
		if(image.getWidth() != image.getHeight())
		{
			if(direction == "idleU" || direction == "idleD" || direction == "idleR" || direction == "idleL")
			{
				screenX = worldX - gp.Player.worldX + Player.screenX + Math.abs((gp.tileSize - image.getWidth())/2);
				screenY = worldY - gp.Player.worldY + Player.screenY + Math.abs((gp.tileSize - image.getHeight())/2);
			}
			else
			{
				screenX = worldX - gp.Player.worldX + Player.screenX + Math.abs((gp.tileSize - image.getWidth())/2);
				screenY = worldY - gp.Player.worldY + Player.screenY + Math.abs((gp.tileSize - image.getHeight())/2) + 6;
			}
		}
		else
		{
			screenX = worldX - gp.Player.worldX + Player.screenX;
			screenY = worldY - gp.Player.worldY + Player.screenY;
		}
		
		if(worldX + gp.tileSize > gp.Player.worldX - Player.screenX &&
		   worldX - gp.tileSize < gp.Player.worldX + Player.screenX &&
		   worldY + gp.tileSize > gp.Player.worldY - Player.screenY &&
		   worldY - gp.tileSize < gp.Player.worldY + Player.screenY)
		{
			g2.drawImage(image, screenX, screenY, null);
			
			if(ico != null)
			{
				g2.drawImage(this.ico.icon, worldX - gp.Player.worldX + Player.screenX + ico.icon.getWidth()/2, screenY - (ico.bobY + 50), null);
				
				if(ico.bobDir)
				{
					
					ico.bobCounter++;
					if(ico.bobCounter >= 3)
					{
						ico.bobCounter = 0;
						ico.bobY++;
					}
					
					if(ico.bobY >= 7)
					{
						ico.bobDir = false;
					}
				}
				else
				{
					ico.bobCounter++;
					if(ico.bobCounter >= 3)
					{
						ico.bobCounter = 0;
						ico.bobY--;
					}
					
					if(ico.bobY <= 0)
					{
						ico.bobDir = true;
					}
				}
			}
			
			g2.setColor(Color.LIGHT_GRAY);
			if(entityCenter != null)
			{
				g2.fillRect(entityCenter.x - gp.Player.worldX + Player.screenX, entityCenter.y - gp.Player.worldY + Player.screenY, entityCenter.width, entityCenter.height);
			}
			g2.setColor(Color.blue);
			if(nextTileCenter != null)
			{
				g2.fillRect(nextTileCenter.x - gp.Player.worldX + Player.screenX, nextTileCenter.y - gp.Player.worldY + Player.screenY, nextTileCenter.width, nextTileCenter.height);
			}
			
//			g2.setColor(new Color(116, 35, 186));
//			g2.fillRect(screenX, screenY, image.getWidth(), image.getHeight());
			
			//Uncomment below to get collision box of NPC
//			g2.setColor(Color.magenta);
//			
//			if(image.getWidth() != image.getHeight())
//			{
//				g2.fillRect(screenX + solidArea.x - Math.abs((gp.tileSize - Up1.getWidth())/2), screenY + solidArea.y - Math.abs((gp.tileSize - Up1.getHeight())/2), solidArea.width, solidArea.height);
//			}
//			else
//			{
//				g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
//			}
//			
//			//Aboslute center of screen
//			g2.setColor(new Color(150, 150, 150, 150));
//			g2.fillRect(gp.screenWidth/2 - 5, gp.screenHeight/2 - 5, 10, 10);
//			g2.setColor(Color.black);
//			g2.fillRect(gp.screenWidth/2 - 1, gp.screenHeight/2 - 1, 2, 2);
//			
//			g2.setColor(new Color(245, 66, 188));
//			for(int i = 0; i < gp.maxScreenColumn; i++)
//			{
//				for(int j = 0; j < gp.maxScreenRow; j++)
//				{
//					g2.drawRect(i * 80, j * 80, 80, 80);
//				}
//			}
//			
//			g2.setColor(new Color(196, 96, 36));
//			g2.drawLine(0, gp.screenHeight/2, gp.screenWidth, gp.screenHeight/2);
//			g2.drawLine(gp.screenWidth/2, 0, gp.screenWidth/2, gp.screenHeight);
		}

//		if(detector)
//		{
//			g2.setColor(new Color(150, 150, 150, 150));
//			
//			checkDetection(gp.Player, 3);
//			
//			for(int i = 0; i < detectionTiles.length; i++)
//			{
//				g2.fillRect(detectionTiles[i].x - gp.Player.worldX + entities.Player.screenX, detectionTiles[i].y - gp.Player.worldY + entities.Player.screenY, 80, 80);
//			}
//		}
//		
		if(detector && name == "Merchant")
		{
			g2.setColor(new Color(100, 100, 100, 80));
			
			checkRadius(gp.Player, 3);
			
			g2.fillRect(detectionRadius.x - gp.Player.worldX + entities.Player.screenX, detectionRadius.y - gp.Player.worldY + entities.Player.screenY, detectionRadius.width, detectionRadius.height);
			g2.setColor(Color.magenta);
			g2.fillRect(detectionRadius.x - gp.Player.worldX + entities.Player.screenX - 5, detectionRadius.y - gp.Player.worldY + entities.Player.screenY - 5, 10, 10);
		}
	}
	
	public void getImages(String Folder) 
	{
		//Up
		Up1 = spSetup(Folder + "up_walk1");
		Up2 = spSetup(Folder + "up_walk2");
		//Down
		Down1 = spSetup(Folder + "down_walk1");
		Down2 = spSetup(Folder + "down_walk2");
		//Left
		Left1 = spSetup(Folder + "left_walk1");
		Left2 = spSetup(Folder + "left_walk2");
		//Right
		Right1 = spSetup(Folder + "right_walk1");
		Right2 = spSetup(Folder + "right_walk2");
		
		UpIdle = spSetup(Folder + "up_stand");
		DownIdle = spSetup(Folder + "down_stand");
		RightIdle = spSetup(Folder + "right_stand");
		LeftIdle = spSetup(Folder + "left_stand");
	}
	
	public BufferedImage returnImage(BufferedImage image)
	{
		switch(direction)
		{
		case "up":
			switch(spriteNum)
			{
			case 1:
				return Up1;
			case 2:
				return Up2;
			}
			break;
		case "idle":
		case "down":
			switch(spriteNum)
			{
			case 1:
				return Down1;
			case 2:
				return Down2;
			}
			break;
		case "upL":
		case "downL":
		case "left":
			switch(spriteNum)
			{
			case 1:
				return Left1;
			case 2:
				return Left2;
			}
			break;
		case "upR":
		case "downR":
		case "right":
			switch(spriteNum)
			{
			case 1:
				return Right1;
			case 2:
				return Right2;
			}
			break;
			//Idle Frames
			case "idleD":
				return DownIdle;
			case "idleU": 
				return UpIdle;
			case "idleR":
				return RightIdle;
			case "idleL":
				return LeftIdle;
		}
		return Down2; //Error, there is no image for this object. Or something like that.
	}
	
	public boolean checkCollision()
	{
		collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkPlayer(this);
		gp.cChecker.checkEntity(this, gp.NPC);
		gp.cChecker.checkEntity(this, gp.Monster);
		//boolean playerContact = gp.cChecker.checkPlayer(this);
		
		return collisionOn;
	}
	
	public void setAction() 
	{
		//Generic action
		if(collisionOn)
		{
			actionLocker = 60;
		}
		
		actionLocker++;
		if(actionLocker >= 60)
		{
			int i = rand.nextInt(100) + 1;
			
			if(i <= 25)
			{
				if(i <= 13)
				{
					direction = "up";
				}
				else
				{
					direction = "idleU";
				}
			}
			else if(i > 25 && i < 50)
			{
				if(i <= 38)
				{
					direction = "down";
				}
				else
				{
					direction = "idleD";
				}
			}
			else if(i > 50 && i < 75)
			{
				if(i <= 68)
				{
					direction = "left";
				}
				else
				{
					direction = "idleL";
				}
			}
			else
			{
				if(i <= 88)
				{
					direction = "right";
				}
				else
				{
					direction = "idleR";
				}
			}				
			actionLocker = 0;
		}
	}
	
	public void update()
	{
		setAction();
		checkCollision();
		
		int idx = gp.cChecker.checkEntity(this, gp.NPC);
		if(idx != -1)
		{
			//this.collisionOn = true;
		}
		
		idx = gp.cChecker.checkEntity(this, gp.Monster);
		if(idx != -1)
		{
			this.collisionOn = true;
		}
		
		idx = gp.cChecker.checkObject(this);
		if(idx != -1)
		{
			if(gp.obj[idx].name == "Door" || gp.obj[idx].name == "Treasure Chest")
			{
				this.collisionOn = true;
			}
		}
		
		if(collisionOn == false)
		{
			switch(direction)
			{
			case "up":
				worldY -= speed; 
				break;
			case "down": 
				worldY += speed; 
				break;
			case "left": 
				worldX -= speed; 
				break;
			case "right": 
				worldX += speed; 
				break;
			case "idle":
				break;
			}
		}
		else
		{
			setAction();
		}
		
		interaction = false;
		
		if(interactable)
		{
			interaction = checkInteraction(gp.Player);
		}
		
		detect = false;
		
		if(detector)
		{
			detect = checkDetection(gp.Player, 3);
		}
		
		spriteCounter++;
		if(spriteCounter > 12 && !(direction == "idle"))
		{
			if(spriteNum < frameCount) //Keeps incrementing the frame we draw until we hit the max, as specified in each class.
			{
				spriteNum++;
				spriteCounter = 0;
			}
			else
			{
				spriteNum = 1;
				spriteCounter = 0;
			}
		}
	}
	
	Rectangle entityCenter;
	Rectangle nextTileCenter;
	
	public void searchPathTest(int goalCol, int goalRow)
	{
		//TODO: Follow center of tile instead of just the edges
		int startCol = (worldX + solidArea.x)/gp.tileSize;
		int startRow = (worldY + solidArea.y)/gp.tileSize;
		
		gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, this);
		
		if(gp.pFinder.search())
		{
			int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
			int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;
			
			nextTileCenter = new Rectangle(nextX - 5 + 40, nextY - 5 + 40, 10, 10);
			entityCenter = new Rectangle(worldX + solidArea.x + (solidArea.width/2) - 5, worldY + (solidArea.y + solidArea.height)/2 + 5, 10, 10);
			
//			System.out.print("Tile: ");
//			printCoor(nextTileCenter.x, nextTileCenter.y);
//			System.out.print("Entity: ");
//			printCoor(entityCenter.x, entityCenter.y);
//			System.out.print("Difference: ");
//			printCoor(Math.abs(nextTileCenter.x - entityCenter.x), Math.abs(nextTileCenter.y - entityCenter.y));
			
			boolean xBound = entityCenter.x >= nextTileCenter.x - 5 && entityCenter.x <= nextTileCenter.x + 5;
			boolean yBound = entityCenter.y >= nextTileCenter.y - 5 && entityCenter.y <= nextTileCenter.y + 5;
			boolean higherX = entityCenter.x > nextTileCenter.x;
			boolean higherY = entityCenter.y > nextTileCenter.y;
			
			if(xBound || yBound)
			{
				if(xBound)
				{
					if(higherY)
					{
						direction = "up";
					}
					else
					{
						direction = "down";
					}
				}
				else
				{
					if(higherX)
					{
						direction = "left";
					}
					else
					{
						direction = "right";
					}
				}
			}
			else
			{
				System.out.println("Ahhhh what do I doooo");
			}
			
			
//			else if(higherX && higherY)
//			{
//				direction = "up";
//				if(checkCollision())
//				{
//					direction = "left";
//				}
//			}
//			else if(!higherX && higherY)
//			{
//				direction = "up";
//				if(checkCollision())
//				{
//					direction = "right";
//				}
//			}
//			else if(higherX && !higherY)
//			{
//				direction = "down";
//				if(checkCollision())
//				{
//					direction = "left";
//				}
//			}
//			else if(!higherX && !higherY)
//			{
//				direction = "down";
//				if(checkCollision())
//				{
//					direction = "right";
//				}
//			}
			
			//TODO: Calculate absolute center of tile and go there
			
//			if(!this.follow)
//			{
//				int nextCol = gp.pFinder.pathList.get(0).col;
//				int nextRow = gp.pFinder.pathList.get(0).row;
//				
//				if(nextCol == goalCol && nextRow == goalRow)
//				{
//					onPath = false;
//				}
//				
//			}
		}
		else
		{
			//We hit the last tile, yay! Keep going until we hit it.
			entityCenter = new Rectangle(worldX + solidArea.x + (solidArea.width/2) - 5, worldY + (solidArea.y + solidArea.height)/2 + 5, 10, 10);
			if(entityCenter.intersects(nextTileCenter))
			{
				System.out.println("End of path");
				onPath = false;
				return;
			}	
		}
	}
	
	public void searchPath(int goalCol, int goalRow)
	{
		//TODO: Follow center of tile instead of just the edges
		int startCol = (worldX + solidArea.x)/gp.tileSize;
		int startRow = (worldY + solidArea.y)/gp.tileSize;
		
		gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, this);
		
		if(gp.pFinder.search())
		{
			int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
			int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;
			
//			System.out.println("Next point: (" + nextX + ", " + nextY + ")");
			
			int entityLeftX = worldX + solidArea.x;
			int entityRightX = worldX + solidArea.x + solidArea.width;
			int entityTopY = worldY + solidArea.y;
			int entityBottomY = worldY + solidArea.y + solidArea.height;
			
			if(entityTopY > nextY && entityLeftX >= nextX && entityRightX < nextX + gp.tileSize)
			{
				direction = "up";
			}
			else if(entityTopY < nextY && entityLeftX >= nextX && entityRightX < nextX + gp.tileSize)
			{
				direction = "down";
			}
			else if(entityTopY >= nextY && entityBottomY < nextY + gp.tileSize)
			{
				if(entityLeftX > nextX)
				{
					direction = "left";
				}
				else
				{
					direction = "right";
				}
			}
			else if(entityTopY > nextY && entityLeftX > nextX)
			{
				direction = "up";
				checkCollision();
				if(collisionOn)
				{
					direction = "left";
				}
			}
			else if(entityTopY > nextY && entityLeftX < nextX)
			{
				direction = "up";
				checkCollision();
				if(collisionOn)
				{
					direction = "right";
				}
			}
			else if(entityTopY < nextY && entityLeftX > nextX)
			{
				direction = "down";
				checkCollision();
				if(collisionOn)
				{
					direction = "left";
				}
			}
			else if(entityTopY < nextY && entityLeftX < nextX)
			{
				direction = "down";
				if(collisionOn)
				{
					direction = "right";
				}
			}
			
			//TODO: Calculate absolute center of tile and go there
			if(!this.follow)
			{
				int nextCol = gp.pFinder.pathList.get(0).col;
				int nextRow = gp.pFinder.pathList.get(0).row;
				
				if(nextCol == goalCol && nextRow == goalRow)
				{
					onPath = false;
				}
				
			}
		}
	}
	
	public boolean checkInteraction(Entity player)
	{
		Rectangle[] adjTile = new Rectangle[4];
		//Up tile
		adjTile[0] = new Rectangle(worldX, worldY - gp.tileSize, gp.tileSize, gp.tileSize);
		//Down tile
		adjTile[1] = new Rectangle(worldX, worldY + gp.tileSize, gp.tileSize, gp.tileSize);
		//Right tile
		adjTile[2] = new Rectangle(worldX - gp.tileSize, worldY, gp.tileSize, gp.tileSize);
		//Left left
		adjTile[3] = new Rectangle(worldX + gp.tileSize, worldY, gp.tileSize, gp.tileSize);
		
		Rectangle Player = new Rectangle (player.solidArea.x + player.worldX, player.solidArea.y + player.worldY, player.solidArea.width, player.solidArea.height);
		
		switch(player.direction) 
		{
		case "idleU":
		case "up":
			if(adjTile[1].intersects(Player))
			{
				return true;
			}
			break;
			
		case "idleD":
		case "down":
			if(adjTile[0].intersects(Player))
			{
				return true;
			}
			break;
			
		case "upR":
		case "downR":
		case "idleR":
		case "right":
			if(adjTile[2].intersects(Player))
			{
				return true;
			}
			break;
			
		case "upL":
		case "downL":
		case "idleL":
		case "left":
			if(adjTile[3].intersects(Player))
			{
				return true;
			}
			break;
		}
		
		return false;
	}
	
	Rectangle[] detectionTiles;
	public boolean checkDetection(Entity player, int radius)
	{
		//R = 1, then 1 tile each side for 4
		//R = 2, then 4 tiles each side for 16
		//R = 3, then 9 tiles each side for 36
		//R = 4, then 16 tiles each side for 64
		detectionTiles = new Rectangle[radius * radius];
		
		//TODO: Change direction of cone pending on direction ambulating
		//Basic formulae stuff, wow. This is for the DOWN direction
		
		switch(direction)
		{
		case "idleU":
		case "up":
			for(int row = 0; row < radius; row++)
			{
				int x; int y;
				for(int tile = 0; tile < 1 + (row * 2); tile++)
				{
					x = worldX - (row * gp.tileSize) + (tile * gp.tileSize);
					y = worldY - ((row + 1) * gp.tileSize);
											
					detectionTiles[tile + (row * row)] = new Rectangle(x, y, gp.tileSize, gp.tileSize);
				}
			}
			break;
		case "idleD":
		case "down":
			for(int row = 0; row < radius; row++)
			{
				int x; int y;
				for(int tile = 0; tile < 1 + (row * 2); tile++)
				{
					x = worldX - (row * gp.tileSize) + (tile * gp.tileSize);
					y = worldY + ((row + 1) * gp.tileSize);
											
					detectionTiles[tile + (row * row)] = new Rectangle(x, y, gp.tileSize, gp.tileSize);
				}
			}
			break;
		case "idleR":
		case "upR":
		case "downR":
		case "right":
			for(int col = 0; col < radius; col++)
			{
				int x; int y;
				for(int tile = 0; tile < 1 + (col * 2); tile++)
				{
					x = worldX + ((col + 1) * gp.tileSize);
					y = worldY - (col * gp.tileSize) + (tile * gp.tileSize);
					
					detectionTiles[tile + (col * col)] = new Rectangle(x, y, gp.tileSize, gp.tileSize);
				}
			}
			break;
		case "idleL":
		case "downL":
		case "upL":
		case "left":
			for(int col = 0; col < radius; col++)
			{
				int x; int y;
				for(int tile = 0; tile < 1 + (col * 2); tile++)
				{
					x = worldX - ((col + 1) * gp.tileSize);
					y = worldY - (col * gp.tileSize) + (tile * gp.tileSize);
					
					detectionTiles[tile + (col * col)] = new Rectangle(x, y, gp.tileSize, gp.tileSize);
				}
			}
			break;
		}
		
		//Combat through detectionTile intereactions with players hitbox
		
		Rectangle Player = new Rectangle (player.solidArea.x + player.worldX, player.solidArea.y + player.worldY, player.solidArea.width, player.solidArea.height);
		
		for(int i = 0; i < detectionTiles.length; i++)
		{
			if(detectionTiles[i].intersects(Player))
			{
				return true;
			}
		}
				
		return false;
	}
	
	public Rectangle detectionRadius;
	public boolean checkRadius(Entity player, int radius)
	{
		if(radius <= 1)
		{
			radius = 1;
		}
		
		detectionRadius = new Rectangle(worldX - (radius * gp.tileSize)/2 + gp.tileSize/2, worldY - (radius * gp.tileSize)/2 + Up1.getHeight()/2, radius * gp.tileSize, radius * gp.tileSize);
				
		Rectangle Player = new Rectangle (player.solidArea.x + player.worldX, player.solidArea.y + player.worldY, player.solidArea.width, player.solidArea.height);
		
		if(detectionRadius.intersects(Player))
		{
			return true;
		}
		
		return false;
	}
	
	public class icon
	{
		public BufferedImage icon;
		public boolean iconPresent = false;
		public int bobY = 0;
		public boolean bobDir = false;
		public int bobCounter = 0;
	}
	
	public void printCoor(int x, int  y)
	{
		System.out.println("(" + x + ", " + y + ")");
	}
}
