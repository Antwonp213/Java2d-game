package main;

import javax.swing.JFrame;

public class Main 
{
	public static JFrame window;
	public static GameWindow gamePanel;
	public static LoadWindow loadWindow;
	
	public static void main(String[] args)
	{
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() 
		{
			@Override
			public void run()
			{
				System.out.println("Saving Prefs");
				gamePanel.sl.saveUserPref();
				
				Runtime.getRuntime().halt(0);
			}
		}));
				
		while(true)
		{	
			window = new JFrame();
			gamePanel = new GameWindow();
			
			loadScreenWindow();
			
			gamePanel.setupGame();
			
			window.dispose();
			loadWindow.disimilateWindow();
			loadWindow = null;
			
			packWindow();
			
			gamePanel.startGameThread();
			
//			while(gamePanel.gameThread != null);
//			
//			//Nullify everything to be redone
//			
//			gamePanel = null;
//			loadWindow = null;
//			window = null;
			break;
		}
	}
	
	public static void packWindow()
	{
		window.getContentPane().removeAll();
		window.setResizable(false);
		window.setTitle("Simple 2D JRPG - Name subject to change");
		
		gamePanel.setDoubleBuffered(true);
		window.add(gamePanel);
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void loadScreenWindow()
	{
		loadWindow = new LoadWindow();
		loadWindow.getLoadScreen();
		loadWindow.startThread();
		window.setResizable(false);
		window.setTitle("Loading game...");
		
		window.add(loadWindow);
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
