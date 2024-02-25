package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import combat.CombatEntity;

public class UtilityTool 
{
	GameWindow gp;
	
	public BufferedImage scaleImage(BufferedImage original, int width, int height)
	{
		BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
		if(scaledImage.getType() == 12 || scaledImage.getType() == 13)
		{
			scaledImage = new BufferedImage(width, height, 6);
		}
		Graphics2D g2 = scaledImage.createGraphics();
		g2.drawImage(original, 0, 0, width, height, null);
		g2.dispose();
		
//		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
//	    GraphicsDevice device = env.getDefaultScreenDevice();
//	    GraphicsConfiguration config = device.getDefaultConfiguration();
//		scaledImage = config.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
//		g2 = (Graphics2D) scaledImage.getGraphics();
//		
		return scaledImage;  
	}
	
	public void makeCompatibleImage(Graphics2D g2, int width, int height)
	{
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice device = env.getDefaultScreenDevice();
	    GraphicsConfiguration config = device.getDefaultConfiguration();
	    BufferedImage buffy = config.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
	    Graphics g = buffy.getGraphics();
	}
	
	public BufferedImage fastLoadImage(URL source)
	{
		try
		{
			Image image = Toolkit.getDefaultToolkit().createImage(source);
			Method method = image.getClass().getMethod("getBufferedImage");
			BufferedImage baseImage = null;
			int counter = 0; //Count to 30 seconds before declaring error
			
			while(baseImage == null && counter < 3000)
			{
				image.getWidth(null);
				baseImage = (BufferedImage) method.invoke(image);
				try
				{
					Thread.sleep(10);
				}
				catch(InterruptedException e) {}
				counter++;
			}
			
			if(baseImage != null)
			{
				return baseImage;
			}
		}
		catch(Exception e)
		{
			System.err.println("Fast loading of " + source.toString() + " Failed. Trying ImageIO");
		}
		
		try
		{
			return ImageIO.read(source);
		}
		catch(IOException ioe)
		{
			System.err.println("Your file doesn't exist dummy");
			return null;
		}
	}
	
	//Load pixels of an image into an integer array.
	public static int[] loadPixelsCrazyFast( BufferedImage img )
	{
	    return ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//DEBUG console
	public void loadConsole()
	{
		
	}
	
	public void unloadConsole()
	{
		
	}
	
	public void parseCommand(String command)
	{
		
	}
	
	public void drawList(LinkedList<CombatEntity> list)
	{
		System.out.print("List: [");
		for(int i = 0; i < list.size(); i++)
		{
			if(i == list.size() - 1)
			{
				System.out.print(list.get(i).name + "]\n");
			}
			else
			{
				System.out.print(list.get(i).name + ", ");
			}
		}
	}
}

