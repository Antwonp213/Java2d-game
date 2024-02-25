package tiles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Player;
import main.GameWindow;
import main.UtilityTool;

//TODO: Expand to incorporate layers

public class TileManager
{
	GameWindow gp;
	public Tile[] tile;
	public int mapTileNum[][][]; //Element 1: The map we're on, Element 2: Column of tile, Element 3: Row of tile
	
	//Tools to get map information from files
	ArrayList<int[][]> maps = new ArrayList<int[][]>();
	int[][] mapGetter;
	
	public TileManager(GameWindow gp)
	{
		this.gp = gp;
		
		tile = new Tile[40];
		mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage();
		loadMapSet(1);
	}
	
	public void setup(int index, String imageName, boolean collision, boolean animated)
	{
		UtilityTool uTool = new UtilityTool();
		
		try
		{
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/Tile/" + imageName +".png"));
			tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadMapSet(int set)
	{
		maps.clear();
		switch(set)
		{
		default:
			loadMap("/Maps/WorldMap.txt");
			break;
		case 0:
			break;
		}
	}
	
//	public void loadMap(String filePath)
//	{
//		maps.clear();
//		mapGetter = null;
//		try
//		{
//			InputStream tempIS = getClass().getResourceAsStream(filePath);
//			BufferedReader tempBR = new BufferedReader(new InputStreamReader(tempIS));
//			
//			int mapCol = 0; int mapRow = 0;
//			
//			String input = "";
//			if((input = tempBR.readLine()) != null)
//			{
//				mapRow++;
//				mapCol = input.split(",").length;
//			}
//			else
//			{
//				throw new Exception("Map does not exist");
//			}
//			
//			while((input = tempBR.readLine()) != null)
//			{
//				mapRow++;
//			}
//			
//			gp.maxWorldCol = mapCol;
//			gp.maxWorldRow = mapRow;
//			
//			tempBR.close();
//			tempIS = null;
//			tempBR = null;
//			
//			InputStream is = getClass().getResourceAsStream(filePath);
//			BufferedReader br = new BufferedReader(new InputStreamReader(is));
//			
//			mapGetter = new int[mapCol][mapRow];
//			
//			int col = 0; int row = 0;
//			
//			while((input = br.readLine()) != null)
//			{
//				while(col < mapCol)
//				{
//					String[] number = input.split(",");
//					
//					int num = Integer.parseInt(number[col]);
//					
//					mapGetter[col][row] = num;
//					
//					col++;
//				}
//				
//				if(col >= mapCol)
//				{
//					col = 0;
//					row++;
//				}
//			}
//			
//			maps.add(mapGetter);
//			mapGetter = null;
//		}
//		catch(Exception e)
//		{
//			System.err.println(filePath + " does not exist.");
//			System.exit(0);
//		}
//	}
	
	//Load map from map package, but if you're entering a new area or something, load them here
	public void loadMap(String filePath)
	{
		try
		{
			InputStream tempIS = getClass().getResourceAsStream(filePath);
			BufferedReader tempBR = new BufferedReader(new InputStreamReader(tempIS));
			
			int mapCol = 0; int mapRow = 0;
			//Count rows and cols
			String input = "";
			
			input = tempBR.readLine();
			if(input != null)
			{
				mapCol++;
				//Get how long a row is
				mapRow = input.split(" ").length;
			}
			else
			{
				throw new Exception("Map does not exist");
			}
			
			while((input = tempBR.readLine()) != null)
			{
				mapCol++; //Find row amount first
			}
			
			gp.maxWorldCol = mapCol;
			gp.maxWorldRow = mapRow;
			
			tempBR.close();
			tempIS = null;
			tempBR = null;
			
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0; int row = 0;
			
			while(row < mapRow)
			{
				String line = br.readLine();
				
				while(col < mapCol)
				{
					String numbers[] = line.split(" ");
					
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[gp.mapNum][col][row] = num;
					col++;
				}
				if(col >= mapCol)
				{
					col = 0;
					row++;
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			e.getCause();
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2)
	{
		//Implementing an automated system to draw tiles based on an array.
		int worldCol = 0; int worldRow = 0;
		
		if(gp.keyH.COORDINATE)
		{
			g2.setStroke(new BasicStroke(1));
			g2.setColor(new Color(100, 100, 100, 200));
		}
		
		while(worldRow < gp.maxWorldRow)
		{
			int tileNum = mapTileNum[gp.mapNum][worldCol][worldRow];
			
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			int screenX = worldX - gp.Player.worldX + Player.screenX;
			int screenY = worldY - gp.Player.worldY + Player.screenY;
			
			//Draw tiles within the boundaries of the screen, for optimization and efficiency
			if(worldX + gp.tileSize > gp.Player.worldX - Player.screenX &&
			   worldX - gp.tileSize < gp.Player.worldX + Player.screenX &&
			   worldY + gp.tileSize > gp.Player.worldY - Player.screenY &&
			   worldY - gp.tileSize < gp.Player.worldY + Player.screenY)
			{
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
				
				if(gp.keyH.COORDINATE)
				{
					g2.drawRect(screenX, screenY, gp.tileSize, gp.tileSize);
				}
			}
			worldCol++;
			
			if(worldCol >= gp.maxWorldCol)
			{
				worldCol = 0;
				worldRow++;
			}
		}
	}
	
	public void getTileImage()
	{
		try
		{
			for(int i = 0; i < 36; i++)
			{
				if(i < 10)
				{
					setup(i, "00" + i, false, false);
				}
				else
				{
					if((i >= 18 && i <= 32) || i == 16)
					{
						setup(i, "0" + i, true, false);
						continue;
					}
					setup(i, "0" + i, false, false);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
}
