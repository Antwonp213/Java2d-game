package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LoadWindow extends JPanel implements Runnable
{
	Thread loading;
	BufferedImage[] loadScreen = new BufferedImage[60];
	URL loadMusic = getClass().getResource("/Music/LoadingMusic.wav");
	Clip clip;
	
	//Image loading threads
	Thread tOne = new Thread(new Runnable() 
	{

		@Override
		public void run() 
		{
			String loadFrame = "";
			for(int i = 0; i < 15; i++)
			{
				if(i < 10)
				{
					loadFrame = "0" + i;
				}
				else
				{
					loadFrame = "" + i;
				}
				setup("/LoadingScreen/frame" + loadFrame, i);
			}
		}
		
	});
	
	Thread tTwo = new Thread(new Runnable() 
	{

		@Override
		public void run() 
		{
			for(int i = 15; i < 30; i++)
			{	
				setup("/LoadingScreen/frame" + i, i);
			}
		}
		
	});
	
	Thread tThree = new Thread(new Runnable() 
	{

		@Override
		public void run() 
		{
			for(int i = 30; i < 45; i++)
			{	
				setup("/LoadingScreen/frame" + i, i);
			}	
		}
		
	});
	
	Thread tFour = new Thread(new Runnable() 
	{

		@Override
		public void run() 
		{
			for(int i = 45; i < 60; i++)
			{	
				setup("/LoadingScreen/frame" + i, i);
			}
		}
		
	});
	
	public LoadWindow()
	{
		setPreferredSize(new Dimension(800, 600));
		setBackground(Color.blue);
		setDoubleBuffered(true);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
	}
	
	public void startThread()
	{
		loading = new Thread(this);
		loading.start();
	}
	
	public void getLoadScreen()
	{
		tOne.start();
		tTwo.start();
		tThree.start();
		tFour.start();
		
		try
		{
			AudioInputStream ais = AudioSystem.getAudioInputStream(loadMusic);
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		catch(Exception e)
		{
			System.err.println("File does not exist");
			System.exit(0);
		}
	}
	
	public void setup(String filePath, int idx)
	{
		try
		{
			loadScreen[idx] = ImageIO.read(getClass().getResourceAsStream(filePath + ".png"));
		}
		catch(Exception e)
		{
			System.err.println(filePath + " does not exist");
			System.exit(0);
		}
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(loadScreen[frame], 0, 0, null);
	}
	
	int frame = 0;
	int timer = 0;
	@Override
	public void run() 
	{
		long prevTime = System.nanoTime();
		double delta = 0;
		double interval = 1000000000/240;
		long currTime;
		
		while(loading != null)
		{
			currTime = System.nanoTime();
			delta += (currTime - prevTime)/interval;
			prevTime = currTime;
			
			if(delta >= 1)
			{
				delta--;
				
				timer++;
				if(timer > 6)
				{
					timer = 0;
					frame++;
					
					if(frame == 60)
					{
						frame = 0;
					}
				}
				
				repaint();
			}
		}
	}
	
	public void disimilateWindow()
	{
		clip.stop();
		loading = null;
	}
}
