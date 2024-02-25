package main;

import entities.Entity;

public class CollisionChecker 
{
	GameWindow gp;
	
	public CollisionChecker(GameWindow gp)
	{
		this.gp = gp;
	}
	
	public void checkTile(Entity entity)
	{
		int entityLeftWorldX = entity.worldX + entity.solidArea.x;
		int entityRightWorldX = entityLeftWorldX + entity.solidArea.width;
		int entityTopWorldY = entity.worldY + entity.solidArea.y;
		int entityBottomWorldY = entityTopWorldY + entity.solidArea.height;
		
		int entityLeftCol = entityLeftWorldX/gp.tileSize;
		int entityRightCol = entityRightWorldX/gp.tileSize;
		int entityTopRow = entityTopWorldY/gp.tileSize;
		int entityBottomRow = entityBottomWorldY/gp.tileSize;
		
		int tileNum1, tileNum2;
		
		switch(entity.direction)
		{
		case "up":
			entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[gp.mapNum][entityLeftCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[gp.mapNum][entityRightCol][entityTopRow];
			if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionOn = true;
			}
			break;
		case "down":
			entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[gp.mapNum][entityLeftCol][entityBottomRow];
			tileNum2 = gp.tileM.mapTileNum[gp.mapNum][entityRightCol][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionOn = true;
			}
			break;
		case "right":
			entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[gp.mapNum][entityRightCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[gp.mapNum][entityRightCol][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionOn = true;
			}
			break;
		case "left":
			entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[gp.mapNum][entityLeftCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[gp.mapNum][entityLeftCol][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionOn = true;
			}
			break;
		case "upL":
			entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
			entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[gp.mapNum][entityLeftCol][entityTopRow];
			if(gp.tileM.tile[tileNum1].collision == true)
			{
				entity.collisionOn = true;
			}
			break;
		case "downL":
			entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
			entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[gp.mapNum][entityLeftCol][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collision == true)
			{
				entity.collisionOn = true;
			}
			break;
		case "upR":
			entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
			entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[gp.mapNum][entityRightCol][entityTopRow];
			if(gp.tileM.tile[tileNum1].collision == true)
			{
				entity.collisionOn = true;
			}
			break;
		case "downR":
			entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
			entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[gp.mapNum][entityRightCol][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collision == true)
			{
				entity.collisionOn = true;
			}
			break;
		}
	}
	
	public int checkObject(Entity entity)
	{
		int index = -1;
		
		for(int i = 0; i < gp.obj.length; i++)
		{
			if(gp.obj[i] != null)
			{
				//Get entity solid area position
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;
				
				//Get the object's solid area position
				gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
				gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;
				
				switch(entity.direction)
				{
				case "up":
					entity.solidArea.y -= entity.speed;
					if(entity.solidArea.intersects(gp.obj[i].solidArea))
					{
						index = i;
					}
					break;
				case "down": 
					entity.solidArea.y += entity.speed;
					if(entity.solidArea.intersects(gp.obj[i].solidArea))
					{
						index = i;
					}
					break;
				case "left":
					entity.solidArea.x -= entity.speed;
					if(entity.solidArea.intersects(gp.obj[i].solidArea))
					{
						index = i;
					}
					break;
				case "right":
					entity.solidArea.x += entity.speed;
					if(entity.solidArea.intersects(gp.obj[i].solidArea))
					{
						index = i;
					}
					break;
				case "upL": 
					entity.solidArea.y -= entity.speed;
					entity.solidArea.x -= entity.speed;
					if(entity.solidArea.intersects(gp.obj[i].solidArea))
					{
						index = i;
					}
					break;
				case "downL": 
					entity.solidArea.y += entity.speed;
					entity.solidArea.x -= entity.speed;
					if(entity.solidArea.intersects(gp.obj[i].solidArea))
					{
						index = i;
					}
					break;
				case "upR": 
					entity.solidArea.y -= entity.speed;
					entity.solidArea.x += entity.speed;
					if(entity.solidArea.intersects(gp.obj[i].solidArea))
					{
						index = i;
					}
					break;
				case "downR": 
					entity.solidArea.y += entity.speed;
					entity.solidArea.x += entity.speed;
					if(entity.solidArea.intersects(gp.obj[i].solidArea))
					{
						index = i;
					}
					break;
				}
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
				gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
			}
		}
		
		return index;
	}
	
	public int checkEntity(Entity entity, Entity[] target)
	{
		int index = -1;
		
		for(int i = 0; i < target.length; i++)
		{
			if(target[i] != null)
			{
				//Get entity solid area position
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;
				
				//Get the object's solid area position
				target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
				target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;
				
				switch(entity.direction)
				{
				case "up":
					entity.solidArea.y -= entity.speed;
					if(entity.solidArea.intersects(target[i].solidArea))
					{
						index = i;
					}
					break;
				case "down": 
					entity.solidArea.y += entity.speed;
					if(entity.solidArea.intersects(target[i].solidArea))
					{
						index = i;
					}
					break;
				case "left":
					entity.solidArea.x -= entity.speed;
					if(entity.solidArea.intersects(target[i].solidArea))
					{
						index = i;
					}
					break;
				case "right":
					entity.solidArea.x += entity.speed;
					if(entity.solidArea.intersects(target[i].solidArea))
					{
						index = i;
					}
					break;
				case "upL": 
					entity.solidArea.y -= entity.speed;
					entity.solidArea.x -= entity.speed;
					if(entity.solidArea.intersects(target[i].solidArea))
					{
						index = i;
					}
					break;
				case "downL": 
					entity.solidArea.y += entity.speed;
					entity.solidArea.x -= entity.speed;
					if(entity.solidArea.intersects(target[i].solidArea))
					{
						index = i;
					}
					break;
				case "upR": 
					entity.solidArea.y -= entity.speed;
					entity.solidArea.x += entity.speed;
					if(entity.solidArea.intersects(target[i].solidArea))
					{
						index = i;
					}
					break;
				case "downR": 
					entity.solidArea.y += entity.speed;
					entity.solidArea.x += entity.speed;
					if(entity.solidArea.intersects(target[i].solidArea))
					{
						index = i;
					}
					break;
				}
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				target[i].solidArea.x = target[i].solidAreaDefaultX;
				target[i].solidArea.y = target[i].solidAreaDefaultY;
			}
		}
		return index;
	}
	
	public void checkPlayer(Entity entity)
	{
		//Get entity solid area position
		entity.solidArea.x = entity.worldX + entity.solidArea.x;
		entity.solidArea.y = entity.worldY + entity.solidArea.y;
		
		//Get the player's solid area position
		gp.Player.solidArea.x = gp.Player.worldX + gp.Player.solidArea.x;
		gp.Player.solidArea.y = gp.Player.worldY + gp.Player.solidArea.y;
		
		switch(entity.direction)
		{
		case "up":
			entity.solidArea.y -= entity.speed;
			if(entity.solidArea.intersects(gp.Player.solidArea))
			{
				entity.collisionOn = true;
			}
			break;
		case "down": 
			entity.solidArea.y += entity.speed;
			if(entity.solidArea.intersects(gp.Player.solidArea))
			{
				entity.collisionOn = true;
			}
			break;
		case "left":
			entity.solidArea.x -= entity.speed;
			if(entity.solidArea.intersects(gp.Player.solidArea))
			{
				entity.collisionOn = true;
			}
			break;
		case "right":
			entity.solidArea.x += entity.speed;
			if(entity.solidArea.intersects(gp.Player.solidArea))
			{
				entity.collisionOn = true;
			}
			break;
		case "upL": 
			entity.solidArea.y -= entity.speed;
			entity.solidArea.x -= entity.speed;
			if(entity.solidArea.intersects(gp.Player.solidArea))
			{
				entity.collisionOn = true;
			}
			break;
		case "downL": 
			entity.solidArea.y += entity.speed;
			entity.solidArea.x -= entity.speed;
			if(entity.solidArea.intersects(gp.Player.solidArea))
			{
				entity.collisionOn = true;
			}
			break;
		case "upR": 
			entity.solidArea.y -= entity.speed;
			entity.solidArea.x += entity.speed;
			if(entity.solidArea.intersects(gp.Player.solidArea))
			{
				entity.collisionOn = true;
			}
			break;
		case "downR": 
			entity.solidArea.y += entity.speed;
			entity.solidArea.x += entity.speed;
			if(entity.solidArea.intersects(gp.Player.solidArea))
			{
				entity.collisionOn = true;
			}
			break;
		}
		entity.solidArea.x = entity.solidAreaDefaultX;
		entity.solidArea.y = entity.solidAreaDefaultY;
		gp.Player.solidArea.x = gp.Player.solidAreaDefaultX;
		gp.Player.solidArea.y = gp.Player.solidAreaDefaultY;
	}
}
