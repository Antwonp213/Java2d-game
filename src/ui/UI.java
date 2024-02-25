package ui;

import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import data.SaveLoad;
import main.GameWindow;
import main.KeyHandler;
import main.Sound;
import main.UtilityTool;

public class UI implements MouseListener, MouseWheelListener
{
	//MOUSE LISTENER
		public static int mouseX, mouseY;
		public Rectangle mouseArea = new Rectangle(0, 0, 16, 16);
		volatile public static boolean mouseLeftClick = false;
		volatile public static boolean mouseRightClick = false;
		volatile public static boolean mouseMenuSet = false;
		volatile public static boolean mouseWheelMoved = false;
		volatile public static int mouseWheelUnits = 0;
		public MouseEvent mouse;
		
		@Override
		public void mouseClicked(MouseEvent e) 
		{
			if(SwingUtilities.isLeftMouseButton(e))
			{
				mouseLeftClick = false;
			}
			
			if(SwingUtilities.isRightMouseButton(e))
			{
				mouseRightClick = false;
			}
		}

		@Override
		public void mousePressed(MouseEvent e) 
		{
			if(SwingUtilities.isLeftMouseButton(e))
			{
				mouseLeftClick = true;
			}
			
			if(SwingUtilities.isRightMouseButton(e))
			{
				mouseRightClick = true;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) 
		{
			if(SwingUtilities.isLeftMouseButton(e))
			{
				mouseLeftClick = false;
			}
			
			if(SwingUtilities.isRightMouseButton(e))
			{
				mouseRightClick = false;
			}
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
			mouseWheelMoved = true;
			mouseWheelUnits = e.getWheelRotation();
		}
		
		public void resetMouseWheel()
		{
			mouseWheelMoved = false;
			mouseWheelUnits = 0;
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
		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	GameWindow gp;
	
	//Global variables
	Sound SFX;
	Sound Music;
	KeyHandler keyH;
	SaveLoad sl;
	Rectangle[] buttons;
	protected int mouseSelection = -1;
	protected int updateMenu = -1;
	public int menuState = 0;
	
	public UI(GameWindow gp)
	{
		this.gp = gp;
		
		this.keyH = gp.keyH;
		this.sl = gp.sl;
		SFX = gp.soundEffect;
		Music = gp.Music;
	}
	
	
	
	//TODO: Write this garbage later to integrate into TitleScreen & PauseScreen
	public class menu
	{
		protected boolean opened = false;
		protected int frameCounter = 0;
		protected int frameLimit;
		protected int frame = 0;
		protected BufferedImage[] openAnim = new BufferedImage[8];
		protected BufferedImage[] closeAnim = new BufferedImage[8];
		protected BufferedImage staticImg;
		
		public menu()
		{
			
		}

		Thread loadImages;
		
		public void drawStatic(Graphics2D g2)
		{
			g2.drawImage(staticImg, 0, 0, null);
		}
		
		public void drawOpen(Graphics2D g2)
		{
			g2.drawImage(openAnim[frame], 0, 0, null);
			updateFrame();
		}
		
		public void drawClose(Graphics2D g2)
		{
			g2.drawImage(closeAnim[frame], 0, 0, null);
			updateFrame();
		}
		
		public void updateFrame()
		{
			frameCounter++;
			if(frameCounter >= frameLimit)
			{
				frameCounter = 0;
				frame++;
				if(frame > 7)
				{
					frame = 0;
					opened = true;
				}
			}
		}
		
		public void playSound()
		{
			if(opened)
			{
				SFX.setFile(103);
				SFX.play();
			}
			else
			{
				SFX.setFile(104);
				SFX.play();
			}
		}
	}
	
	public class SaveBook extends menu
	{
		public SaveBook()
		{
			frameLimit = 15;
			
			loadImages = new Thread(new Runnable() 
			{
				@Override
				public void run()
				{
					for(int i = 0; i < openAnim.length; i++)
					{
						
					}
					
					for(int i = 0; i < closeAnim.length; i++)
					{
						
					}
				}
			});
			
			
			loadImages.start();
		}
		
	}
	
	public class OptionScroll extends menu
	{
		public OptionScroll()
		{
			frameLimit = 25;
			
			loadImages = new Thread(new Runnable() 
			{
				@Override
				public void run()
				{
					for(int i = 0; i < openAnim.length; i++)
					{
						
					}
					
					for(int i = 0; i < closeAnim.length; i++)
					{
						
					}
				}
			});
			
			
			loadImages.start();
		}
	}
	
	//SETUP garbage
	//Setup no scale
	public BufferedImage setupNS(String filePath)
	{
		BufferedImage image = null;
		
		try
		{
			image = ImageIO.read(getClass().getResourceAsStream(filePath + ".png"));
		}
		catch(Exception e)
		{
			System.err.println("File does not exist on path: " + filePath);
			System.exit(0);
		}
		
		return image;
	}
	
	//Setup with scale
	public BufferedImage setupWS(String filePath)
	{
		BufferedImage image = null;
		UtilityTool uTool = new UtilityTool();
		
		try
		{
			image = ImageIO.read(getClass().getResourceAsStream(filePath + ".png"));
			image = uTool.scaleImage(image, gp.screenWidth, gp.screenHeight);
		}
		catch(Exception e)
		{
			System.err.println("File does not exist on path: " + filePath);
			System.exit(0);
		}
		
		return image;
	}
	
	//Setup custom scale
	public BufferedImage setupCS(String filePath, double scaleWidth, double scaleHeight)
	{
		BufferedImage image = null;
		UtilityTool uTool = new UtilityTool();
		
		try
		{
			image = ImageIO.read(getClass().getResourceAsStream(filePath + ".png"));
			image = uTool.scaleImage(image, (int) (image.getWidth() * scaleWidth), (int) (image.getHeight() * scaleHeight));
		}
		catch(Exception e)
		{
			System.err.println("File does not exist on path: " + filePath);
			System.exit(0);
		}
		
		return image;
	}
}
