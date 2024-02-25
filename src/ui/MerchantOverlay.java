package ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GameWindow;

public class MerchantOverlay extends UI
{
	//BUFFERED IMAGES
	BufferedImage[] staticIMG;
	BufferedImage[] mainLeftTurn;
	BufferedImage[] mainRightTurn;
	BufferedImage[] leftPageTurn;
	BufferedImage[] rightPageTurn;
	BufferedImage[] openTabs;
	BufferedImage[] closeTabs;
	BufferedImage[] openBook;
	BufferedImage[] closeBook;
	BufferedImage scale;
	BufferedImage background;
	
	//MENU STATES
	public final static int SET_MERCHANT_MENU = 0; public final static int MERCHANT_MENU_READY = 1;
	
	public MerchantOverlay(GameWindow gp) 
	{
		super(gp);
		
		initArr();
	}
		
	public void initArr()
	{
		staticIMG = new BufferedImage[7];
		mainLeftTurn = new BufferedImage[1];
		mainRightTurn = new BufferedImage[1];
		leftPageTurn = new BufferedImage[1];
		rightPageTurn = new BufferedImage[1];
		openTabs = new BufferedImage[1];
		closeTabs = new BufferedImage[1];
		openBook = new BufferedImage[1];
		closeBook = new BufferedImage[1];
		
		loadStatic.start();
		loadLeftTurn.start();
		loadRightTurn.start();
		loadOpen.start();
		loadClose.start();
	}
	
	private boolean escSet = false;
	public void update()
	{
		if(gp.keyH.key.contains(gp.keyH.esc))
		{
			escSet = true;
		}
		else if(escSet)
		{
			gp.gameState = gp.playState;
			escSet = false;
		}
	}
	
	public void draw(Graphics2D g2)
	{	
		g2.drawImage(background, 0, 0, null);
		
		g2.drawImage(staticIMG[0], 0, gp.screenHeight/2 - staticIMG[0].getHeight()/2, null);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Threadinators

	Thread loadLeftTurn = new Thread(new Runnable() 
	{
		@Override
		public void run()
		{
		
		}
	});
	
	Thread loadRightTurn = new Thread(new Runnable() 
	{
		@Override
		public void run()
		{
		
		}
	});
	
	Thread loadOpen = new Thread(new Runnable() 
	{
		@Override
		public void run()
		{
		
		}
	});
	
	Thread loadClose = new Thread(new Runnable() 
	{
		@Override
		public void run()
		{
		
		}
	});
	
	Thread loadStatic = new Thread(new Runnable() 
	{
		String Folder = "/MerchantOverlay/Main/Static/";
		@Override
		public void run()
		{
			for(int i = 0; i < staticIMG.length; i++)
			{
				staticIMG[i] = setupWS(Folder + i);
			}
			
			scale = setupNS("/MerchantOverlay/TwinScale");
			background = setupWS("/MerchantOverlay/MerchantTable");
		}
	});
}
