package ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import combat.CombatEngine;
import combat.CombatEntity;
import entities.Player;
import main.GameWindow;
import main.KeyHandler;
import main.UtilityTool;

public class UI_OLD implements MouseListener, MouseWheelListener
{
	//MOUSE LISTENER
	public static int mouseX, mouseY;
	public Rectangle mouseArea = new Rectangle(0, 0, 16, 16);
	public static boolean mousePressed = false;
	public static boolean mouseMenuSet = false;
	public MouseEvent mouse;
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		mousePressed = false;
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		mousePressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		mousePressed = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{

	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
//		int rotation = e.getWheelRotation();
	}
	
	public void mouseMoved(MouseEvent e)
	{
		
	}
	
	public void updateMousePosition()
	{
		if(!gp.fullScreen)
		{
			//130
			mouseX = (int) (MouseInfo.getPointerInfo().getLocation().x - gp.getLocationOnScreen().x);
			//60
			mouseY = (int) (MouseInfo.getPointerInfo().getLocation().y - gp.getLocationOnScreen().y);	
		}
		else
		{
			mouseX = (int) ((double) MouseInfo.getPointerInfo().getLocation().x * ((double) gp.screenWidth/gp.nativeScreenWidth));
			mouseY = (int) ((double) MouseInfo.getPointerInfo().getLocation().y * ((double) gp.screenHeight/gp.nativeScreenHeight));
		}
		//Update rectangle solidArea
		mouseArea.x = mouseX;
		mouseArea.y = mouseY;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	GameWindow gp;
	KeyHandler keyH;
	Player Player;
	CombatEngine ce;
	MouseEvent e;
	
	BufferedImage mouseNormal, mouseAttack, titleCursor, title, GrassNRoadBackground;
	
	public int counter = 0;
	public byte cursorState = 0;
	public String displayRuntime = null;
	//Set invisible rectangles around the 5 options
	Rectangle[] overworldMenuButton = new Rectangle[5];
	public byte mouseSelection = 0;
	BufferedImage worldMap[];
	
	private HashSet<String> debugMSG = new HashSet<String>();
	
	//UTILITIES SECTION
	public UI_OLD(GameWindow gp)
	{
		this.gp = gp;
				
		overworldMenuButton[0] = new Rectangle(90, 85, 260, 50);
		overworldMenuButton[1] = new Rectangle(147, 165, 145, 50);
		overworldMenuButton[2] = new Rectangle(160, 245, 120, 60);
		overworldMenuButton[3] = new Rectangle(148, 325, 145, 45);
		overworldMenuButton[4] = new Rectangle(150, 405, 140, 60);
		
		
	}
	
	public void initEngines()
	{
		ce = gp.combat;
	}
	
	public void setupImages()
	{
		UtilityTool uTool = new UtilityTool();
		titleCursor = setup("/UserInterface/Title Cursor");
		
		//Regular sized images
		try 
		{
			GrassNRoadBackground = ImageIO.read(getClass().getResourceAsStream("/CombatMap/GrassBG.png"));
			GrassNRoadBackground = uTool.scaleImage(GrassNRoadBackground, gp.screenWidth, gp.screenHeight);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
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
			System.err.println("Exception in loading UI images");
			e.printStackTrace();
		}
		return scaledImage;
	}
	
	public void createWorldMaps()
	{
		worldMap = new BufferedImage[gp.maxMap];
		int worldMapWidth = gp.tileSize * gp.maxWorldCol;
		int worldMapHeight = gp.tileSize * gp.maxWorldRow;
		
		for(int i = 0; i < gp.maxMap; i++)
		{
			worldMap[i] = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D) worldMap[i].createGraphics();
			
			int col = 0;
			int row = 0;
			
			while(row < gp.maxWorldRow)
			{
				int tileNum = gp.tileM.mapTileNum[i][col][row];
				int x = gp.tileSize * col;
				int y = gp.tileSize * row;
				g2.drawImage(gp.tileM.tile[tileNum].image, x, y, null);
				
				col++;
				if(col == gp.maxWorldCol)
				{
					col = 0;
					row++;
				}
			}
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//OVERWORLD
	public void drawMinimap(Graphics2D g2)
	{
		//Draw Oval
		BufferedImage destination = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphic = destination.createGraphics();
		
		//Draw in source image using AlphaComposite
		graphic.setComposite(AlphaComposite.SrcIn);
		graphic.drawImage(worldMap[gp.mapNum], 0, 0, null);
	}
	
	int menuX;
	int menuY;
	byte menuView = 0;
	public void drawMenu(Graphics2D g2)
	{	
		updateMousePosition();
		g2.setColor(new Color(0, 0, 0, 175));
		g2.fillRect(64, 35, 310, 480);
		g2.fillRect(64, 530, 310, 155);
		g2.fillRect(387, 35, 830, 650);
		
		//Draw Text
		g2.setColor(Color.white);
		g2.setFont(new Font("Times New Roman", Font.PLAIN, 65));
		g2.drawString("Character", 90, 130);
		g2.drawString("Skills", 147, 210);
		g2.drawString("Map", 160, 290);
		g2.drawString("Items", 148, 370);
		g2.drawString("Party", 150, 450);
		g2.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		g2.drawString("Gold:", 72, 590);
		g2.drawString("Time: ", 72, 640);
		//Draw changing varaibles on lower-left box
		g2.drawString("0", 150, 590);
		//Upate runtime
		updateRuntime();
		g2.drawString(displayRuntime, 150, 640);
		
		//Check for collisions with mouse
		for(int i = 0; i < overworldMenuButton.length; i++)
		{
			if(overworldMenuButton[i].intersects(mouseArea))
			{	
				if(mousePressed && mouseSelection != (byte) i)
				{
					mouseSelection = (byte) i;
				}
			}
		}
		
		if(!mousePressed)
		{
			menuView = mouseSelection;
		}
		
		//Draw particular menu screen
		switch(menuView)
		{
		case 0:
			break;
		case 1:
			break;
		case 2:
			drawMap(g2);
			break;
		case 3:
			break;
		case 4:
			break;
		}
		
		g2.setColor(Color.white);
		g2.fillRect(mouseArea.x, mouseArea.y, 16, 16);
	}
	
	private int playerBoxOpacity = 255;
	private boolean fadeInOut = false; //False is fade-out, true is fade-in
	public void drawMap(Graphics2D g2)
	{
		//Draw map based on width and height, then scale
		g2.drawImage(worldMap[gp.mapNum], 477, 35, 650, 650, null);
		g2.setColor(new Color(252, 57, 3, playerBoxOpacity));
		
		if(!fadeInOut)
		{
			playerBoxOpacity -= 5;
			if(playerBoxOpacity == 0)
			{
				fadeInOut = true;
			}
		}
		else
		{
			playerBoxOpacity += 5;
			if(playerBoxOpacity == 255)
			{
				fadeInOut = false;
			}
		}
		
		//Draw player
		double scale = (double)(gp.tileSize * gp.maxWorldCol)/650;
		int playerX = 477 + (int) (gp.Player.worldX/scale);
		int playerY = 35 + (int) (gp.Player.worldY/scale);
		int playDot = (int) (gp.tileSize/scale);
		g2.fillRect(playerX, playerY, playDot, playDot);
	}
	
	public void updateRuntime()
	{
		//TODO: When loading the game, update the currentTime variable to be subtracted from current seconds
		GameWindow.differenceTime =  System.currentTimeMillis() - GameWindow.startTime;
		GameWindow.seconds = (int) (GameWindow.differenceTime/1000);
		
		GameWindow.minutes = GameWindow.seconds/60;
		GameWindow.hours = GameWindow.seconds/3600;
		
		if(GameWindow.seconds >= 60)
		{
			GameWindow.seconds %= 60;
		}
		
		if(GameWindow.minutes >= 60)
		{
			GameWindow.minutes %= 60;
		}
		
		//Pack it all into a string, first clear
		displayRuntime = "";
		displayRuntime += GameWindow.hours < 10 ? ("0" + GameWindow.hours) : GameWindow.hours;
		displayRuntime += GameWindow.minutes < 10 ? (" : 0" + GameWindow.minutes) : " : " + GameWindow.minutes;
		displayRuntime += GameWindow.seconds < 10 ? (" : 0" + GameWindow.seconds) : " : " + GameWindow.seconds;
	}
	
	//Player section
	public void drawStaminaBar(Graphics2D g2, int opacity)
	{
		//Calculate stamina bar position and width as a percentage in 80
		int Opacity = opacity * 3 > 255 ? 255 : (opacity * 3);
		int StaminaBar = (int) (entities.Player.stamina * (double) gp.tileSize/gp.Player.maxStamina);
		int centerX = entities.Player.screenX;
		int centerY = entities.Player.screenY - 20;
		//Stamina bar
		g2.setColor(new Color(0, 0, 0, Opacity));
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(centerX - 2, centerY - 2, gp.tileSize + 3, 15);
		g2.setColor(new Color(0, 0, 0, (int) (100 / ((double) 255/Opacity))));
		g2.fillRect(centerX, centerY, gp.tileSize, 12);
		g2.setColor(new Color(64, 150, 45, Opacity));
		g2.fillRect(centerX, centerY, StaminaBar, 12);
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//DEBUG
	public void drawPath(Graphics2D g2)
	{
		g2.setColor(new Color(46, 209, 136, 100));
		
		for(int i = 0; i < gp.pFinder.pathList.size(); i++)
		{
			int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
			int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
			int screenX = worldX - gp.Player.worldX + entities.Player.screenX;
			int screenY = worldY - gp.Player.worldY + entities.Player.screenY;
			
			g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
			
//			if(gp.pFinder.pathList.get(0) != null)
//			{
//				int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
//				int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;
//				nextX += 40;
//				nextY += 40;
//				int nextScreenX = nextX - gp.Player.worldX + entities.Player.screenX;
//				int nextScreenY = nextY - gp.Player.worldY + entities.Player.screenY;
//				
//				g2.setColor(Color.LIGHT_GRAY);
//				g2.fillRect(nextScreenX - 5, nextScreenY - 5, 10, 10);
//			}
		}
	}
	
	public void drawOpenlist(Graphics2D g2)
	{
		int size = gp.pFinder.openedList.size();
		
		for(int i = 0; i < gp.pFinder.openedList.size(); i++)
		{
			if(size != gp.pFinder.openedList.size())
			{
				break;
			}
			
			Tiled tile = (n) -> { return n * gp.tileSize;};
			
			int worldX = tile.tile(gp.pFinder.openedList.get(i).col);
			int worldY = tile.tile(gp.pFinder.openedList.get(i).row);
			int screenX = worldX - gp.Player.worldX + entities.Player.screenX;
			int screenY = worldY - gp.Player.worldY + entities.Player.screenY;
			
			int diff = i/((size / 255) + 1);
			
			g2.setColor(new Color(255 - diff, 255 - diff, 255 - diff, 255 - diff));
			
			g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
		}
	}
	
	interface Tiled
	{
		int tile(int i);
	}
	
	public void showCoordinates(Graphics2D g2)
	{
		g2.setColor(Color.white);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		int x = gp.Player.worldX + gp.Player.solidArea.x;
		int y = gp.Player.worldY + gp.Player.solidArea.y;
		g2.drawString("Coordinate: (" + x + ", " + y + ")", 20, 20);
		g2.drawString("Tile: (" + x/gp.tileSize + ", " + y/gp.tileSize + ")", 50, 40);
		
//		g2.fillRect(gp.Player.screenY, gp.Player.screenY, 5, 5);
	}
	
	public void displayMessage(int msg, int time, Graphics2D g2)
	{
		if(time > 0) //Disply message for 3 seconds
		{
			int opacity = time * 2 > 255 ? 255 : time * 2;
			g2.setColor(new Color(255, 255, 255, opacity));
			g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			
			switch(msg)
			{
			case 0:
				g2.drawString("Debug mode enabled", entities.Player.screenX - 55, (entities.Player.screenY/2) + opacity - (int) (gp.tileSize * 1.5));
				break;
			case 1:
				g2.drawString("Combat triggers are disabled", entities.Player.screenX - 90, (entities.Player.screenY/2) + opacity - (int) (gp.tileSize * 1.5));
				break;
			case 2:
				g2.drawString("Combat triggers are enabled", entities.Player.screenX - 90, (entities.Player.screenY/2) + opacity - (int) (gp.tileSize * 1.5));
				break;
			}
			
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Combat section
	public boolean transitionDrawn = false;
	public void drawCombatTransition(Graphics2D g2)
	{	
//		g2.setColor(new Color(0, 0, 0, counter));
//		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
//		
//		if(counter < 254)
//		{
//			counter += 2;
//		}
		
		if(transitionDrawn)
		{
			return;
		}
		
		int x = 0;
		int y = 0;
		counter++;
		for(int i = 0; i < counter; i++)
		{
			g2.setColor(Color.black);
			g2.fillRect(x * 80, y * 80, 80, 80);
			x++;
			if(x > 16)
			{
				x = 0;
				y++;
			}
			
			if(y > 9)
			{
				transitionDrawn = true;
				return;
			}
		}
	}
	
	public void drawCombatMenu(Graphics2D g2)
	{
		
	}
	
	public void drawCharStats(Graphics2D g2)
	{
		
	}
	
	private int tileBackground;
	
	public void retrieveTileInfo()
	{
		//Get tile informatin from the tile we're standing on. IE: the center of the collision box
		tileBackground = gp.tileM.mapTileNum[gp.mapNum][(gp.Player.worldX + gp.Player.solidArea.x + (gp.Player.solidArea.width/2))/gp.tileSize][(gp.Player.worldY + gp.Player.solidArea.y + (gp.Player.solidArea.height/2))/gp.tileSize];
	}
	
	public void drawBackground(Graphics2D g2)
	{
		//Construct background in conjunction to the tile we set it off in
		switch(tileBackground)
		{
		//Grass & Road tiles
		case 0:
		case 4:
		case 5:
			g2.drawImage(GrassNRoadBackground, 0, 0, null);
			break;
		default:
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		}
	}
	
	public void drawTurnOrderBar(Graphics2D g2, LinkedList<CombatEntity> entityList)
	{
		//Set up bar
		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(1, 626, 749, 93);
		g2.setStroke(new BasicStroke(1));
		g2.setColor(new Color(225, 225, 225, 225));
		g2.drawRect(1, 625, 750, 94);
		g2.setColor(new Color(190, 190, 190, 225));
		g2.drawRect(2, 626, 749, 46);
		g2.drawRect(2, 626, 75, 46);
		g2.drawRect(2, 672, 75, 46);
		
		//Draw images based on speedCounter
		int y = 0;
		int x = 0;
		for(int i = 0; i < entityList.size(); i++)
		{
			if(entityList.get(i).speedCounter > 5000)
			{
				if(entityList.get(i).playableID == 0)
				{
					y = 672;
				}
				else
				{
					y = 626;
				}
				
				x = 750 - (int) ((double) ((entityList.get(i).speedCounter - 5000) * (750.0/5000.0)));
				
				x = x < 0 ? 10 : x;
				
				g2.drawImage(entityList.get(i).icon, x, y, null);
			}
		}
	}
	
	public void drawActionBar(Graphics2D g2)
	{
		g2.setColor(new Color(100, 100, 100, 150));
		g2.fillRect(1024, 0, 1280, 384);
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
