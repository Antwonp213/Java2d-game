package main;

import java.awt.BasicStroke;
//Imports
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ai.PathFinder;
import combat.CombatEngine;
import data.SaveLoad;
import debug.ConsoleOverlay;
import entities.Entity;
import entities.Player;
import tiles.TileManager;
import ui.DialogueOverlay;
import ui.MenuScreen;
import ui.MerchantOverlay;
import ui.OverworldOverlay;
import ui.PauseScreen;
import ui.TitleScreen;
import ui.UI;
import ui.UI_OLD;

@SuppressWarnings("serial")
public class GameWindow extends JPanel implements Runnable
{
	//DEBUG
	ConsoleOverlay co = new ConsoleOverlay(this);
	public boolean DEBUG = true;
	public boolean combatEnabled = false;
	public boolean collisionEnabled = false;
	public boolean displayDebugMsg = false;
	public boolean console = false;
	int displayMessage = 0;
	
	//SCREEN SETTINGS
	final int originalTileSize = 16; //Making a world-map of 16px by 16px tiles
	public final int scale = 5; //Scale original size of 16px to fit modern monitors
	public final int tileSize = originalTileSize * scale; //16 * 5 = 80px per tile side
	//Find resolution of game
	public final int maxScreenColumn = 16; //16 * 80 = 1280px
	public final int maxScreenRow = 9; //9 * 80 = 720px
	public final int screenWidth = tileSize * maxScreenColumn;
	public final int screenHeight = tileSize * maxScreenRow;
	//FOR FULLSCREEN
	public int nativeScreenWidth;
	public int nativeScreenHeight;
	int screenWidth2 = screenWidth;
	int screenHeight2 = screenHeight;
	BufferedImage tempScreen;
	public Graphics2D g2;
	static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	public boolean fullScreen = false; //Flip-flop so the setFullscreen(); method doesn't get called a bunch
	
	//WORLD SETTINGS
	public int maxMap = 1; //How many maps we have on file
	public int mapNum; //The map we're on
	public int maxWorldCol = 50;
	public int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;
	
	//CLOCK
	public static long startTime = 0;
	public static long differenceTime = 0;
	public static int seconds = 0;
	public static int minutes = 0;
	public static int hours = 0;
	
	//SYSTEM - ESSENTIAL
	public StaticSound ss = new StaticSound();
	public TitleScreen ts;
	public SaveLoad sl = new SaveLoad(this);
	public KeyHandler keyH = new KeyHandler(this); //Controls player and UI movements
	public Sound soundEffect = new Sound(); //Controls Sound Effect files
	public Sound Music = new Sound(); //Controls music files
	public UI_OLD UI = new UI_OLD(this);
	
	//SYSTEM - USER INTERFACE
	public UI newUI = new UI(this);
	public OverworldOverlay oo = new OverworldOverlay(this);
	public MenuScreen ms = new MenuScreen(this);
	public PauseScreen ps = new PauseScreen(this);
	public MerchantOverlay mo = new MerchantOverlay(this);
	public DialogueOverlay dio = new DialogueOverlay(this);
	
	//SYSTEM - INESSENTIAL
	Thread gameThread;
	public TileManager tileM;
	public CollisionChecker cChecker;
	public AssetSetter aSetter;
	public CombatEngine combat;
	public PathFinder pFinder;
	
	//ENTITY & OBJECTS
	public Player Player;
	public Entity obj[];
	public Entity NPC[];
	public Entity Monster[];
	LinkedList<Entity> drawList;
	
	//DRAW SETTINGS
	public final int FPS = 60; //Depending on what I want to do, change this variable
	int drawCounter = 0; //Counts how many times the window is drawn per second
	long timer = 0;
	int transitionTimer = 0;
	int staminaTimer = 0; //2 second timer before it disappears
	
	//STATE MACHINE
	public int forceEncounter = 0;
	public static enum encounter
	{
		none, guardMiniboss
	}
	
	public int questState; //Keep track of what questline we're in.
	public int mapState; //Keep track of what map we're in.
	public int gameState; //Keeps track of what state we are in.
	public int prevState;
	public final int titleState = 0; public final int playState = 1; public final int pauseState = 2; public final int menuState = 3;
	public final int dialogueState = 4; public final int combatState = 5; public final int combatOverState = 6; public final int gameOverState = 7;
	public final int combatTransitionState = 8; public final int mapTransitionState = 9; public final int merchantState = 10; public final int consoleState = 11;
	
	public state game;
	public enum state
	{
		title, play, pause, menu, dialogue, combat, combatOver, gameOver, combatTransition, mapTransition, merchant, console
	};
	
	public GameWindow()
	{
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.blue);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.addMouseListener(UI);
		this.addMouseListener(newUI);
		this.addMouseWheelListener(newUI);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		
		ImageIO.setUseCache(false);
	}
	
	public void setupGame()
	{		
		//Initialize non-essential things here...
		ts = new TitleScreen(this);
		tileM = new TileManager(this);
		cChecker = new CollisionChecker(this);
		aSetter = new AssetSetter(this);
		combat = new CombatEngine(this, this.keyH);
		pFinder = new PathFinder(this);
		//Initialize Objects
		Player = new Player(this, keyH);
		obj = new Entity[50];
		NPC = new Entity[50];
		Monster = new Entity[20];
		//Initialize Lists
		drawList = new LinkedList<>();
		//UI Elements
		UI.createWorldMaps();
		UI.setupImages();
		
		mapNum = 0; //Map 1
		setMouse();
		
		Dimension nScreen = Toolkit.getDefaultToolkit().getScreenSize();
		nativeScreenWidth = (int) nScreen.getWidth();
		nativeScreenHeight = (int) nScreen.getHeight();
		
		aSetter.setObject();
		aSetter.setNPC();
		
//		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
//		g2 = (Graphics2D)tempScreen.getGraphics();
		
		game = state.play;
		gameState = titleState;
		UI.initEngines();
		
		startTime = System.currentTimeMillis();
		
//		startGame();
	}
	
	public void startGame()
	{
		gameState = playState;
		UI.initEngines();
		startTime = System.currentTimeMillis();
	}
	
	public void setFullScreen()
	{
		//Dispose main panel
		Main.window.dispose();
		Main.window.setUndecorated(true);
		
		//Get local screen device
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(Main.window);
		
		Main.packWindow();
		
		Main.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		screenWidth2 = nativeScreenWidth;
		screenHeight2 = nativeScreenHeight;;
	}
	
	public void startGameThread()
	{	
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void setInitMsg()
	{
		if(DEBUG)
		{
			oo.addMessage("Debug Enabled");
		}
		
		if(combatEnabled)
		{
			oo.addMessage("Combat Enabled");
		}
		else
		{
			oo.addMessage("Combat Disabled");
		}
		
		if(collisionEnabled)
		{
			oo.addMessage("Collision Enabled");
		}
		else
		{
			oo.addMessage("Collision Disabled");
		}
	}
	
	
	int FPSCounter = 0;
	int FPSCount = 0;
	int prevFPS = 0;
	double currFPS = 0;
	@Override
	public void run()
	{
		double delta = 0;
		long prevTime = System.nanoTime();
		long currentTime;
		
		double drawInterval = 1000000000/60;
		
		sl.loadUserPref();
		
		setInitMsg();
		
		while(gameThread != null)
		{
			while(gameState == titleState)
			{
				currentTime = System.nanoTime();
				delta += (currentTime - prevTime) / drawInterval;
				FPSCounter += (currentTime - prevTime);
				prevTime = currentTime;
				
				if(delta >= 1)
				{				
					getState();
					
					update();
							
					repaint();
					
					FPSCount += delta >= 1.05f ? (-2) : 1;
					
//					currFPS = (double) 1000000000.0 / (System.nanoTime() - prevTime);
					
//					System.out.println(String.format("%.2f", currFPS));
					
					delta--;
//					System.out.println(String.format("%.2f", delta));
				}
				
				if(FPSCounter >= 1000000000)
				{
					prevFPS = FPSCount;
					FPSCount = 0;
					FPSCounter -= 1000000000;
				}
			}
			
			drawInterval = 1000000000/FPS;
			
			playMusic(Sound.OverWorld);
			while(gameThread != null)
			{
				currentTime = System.nanoTime();
				delta += (currentTime - prevTime) / drawInterval;
				FPSCounter += (currentTime - prevTime);
				prevTime = currentTime;
				
				if(delta >= 1)
				{
					getState();
					
					update();

					repaint();
					
					FPSCount += delta >= 1.05f ? (-2) : 1;
					
					delta--; 
				}
				
				if(FPSCounter >= 1000000000)
				{
					prevFPS = FPSCount;
					FPSCount = 0;
					FPSCounter -= 1000000000;
				}
			}	
		}
	}

	public void update()
	{			
		if(keyH.FULLSCREEN && !fullScreen)
		{
			device.setFullScreenWindow(Main.window);
			setFullScreen();
			fullScreen = true;
		}
		else if(!keyH.FULLSCREEN && fullScreen)
		{	
			device.setFullScreenWindow(null);
			Main.window.dispose();
			Main.window.setUndecorated(false);
			Main.packWindow();
			fullScreen = false;
			screenWidth2 = screenWidth;
			screenHeight2 = screenHeight;
		}
		
		if(gameState == pauseState)
		{
			ps.update();
		}
		else if(gameState == titleState)
		{
			ts.updateTitle();
			if(!ts.titleListenerAdded)
			{
				ts.titleListenerAdded = true;
				this.addKeyListener(ts);
			}
		}
		else if(gameState == menuState)
		{
			ms.update();
		}
		else if(gameState == playState)
		{
			Player.update();
			for(int i = 0; i < NPC.length; i++)
			{
				if(NPC[i] != null)
				{
					NPC[i].update();
				}
			}
			
			for(int i = 0; i < obj.length; i++)
			{
				if(obj[i] != null && obj[i].interactable)
				{
					obj[i].update();
				}
			}
		}
		else if(gameState == combatState)
		{
			combat.loop();
		}
		else if(gameState == merchantState)
		{
			mo.update();
		}
		else if(gameState == dialogueState)
		{
			dio.update();
		}
		else if(gameState == consoleState)
		{
			co.update();
		}
		
		//DEBUG keys
		if(keyH.COLLISIONOFF)
		{
			keyH.COLLISIONOFF = false;
			collisionEnabled = !collisionEnabled;
			if(collisionEnabled)
			{
				oo.addMessage("Collision Enabled");
			}
			else
			{
				oo.addMessage("Collision Disabled");
			}
		}
		
		if(keyH.COMBATENABLED && DEBUG)
		{
			keyH.COMBATENABLED = false;
			combatEnabled = !combatEnabled;
			if(combatEnabled)
			{
				oo.addMessage("Combat Enabled");
			}
			else
			{
				oo.addMessage("Combat Disabled");
			}
		}
		
		if(keyH.CONSOLE)
		{
			keyH.CONSOLE = false;
			console = !console;
			
			System.out.println("Toggling console: " + console);
			
			if(console)
			{
				prevState = gameState;
				gameState = consoleState;
				this.addMouseListener(co);
				this.addMouseWheelListener(co);
				this.addKeyListener(co);
				this.removeKeyListener(keyH);
				this.removeMouseListener(UI);
				this.removeMouseWheelListener(UI);
			}
			else
			{
				gameState = prevState;
				this.removeMouseListener(co);
				this.removeMouseWheelListener(co);
				this.removeKeyListener(co);
				this.addKeyListener(keyH);
				this.addMouseListener(UI);
				this.addMouseWheelListener(UI);
			}
		}
	}
	
	public void getState()
	{
		if(gameState == titleState)
		{
			return;
		}
		
		if(keyH.TAB)
		{
			gameState = menuState;
			return;
		}
		
		if(keyH.MENU)
		{
			gameState = pauseState;
			return;
		}
		
		if(gameState == consoleState)
		{
			return;
		}
		
		if(gameState == combatState || gameState == combatTransitionState || gameState == combatOverState || gameState == merchantState)
		{
			return;
		}
		
		if(gameState != playState)
		{
			return;
		}
		
		gameState = playState;		
	}
	
	boolean throwMessageOnce = true;
	int counter = 0;
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		try
		{
			//SLOW AF, think of another way...
			if(fullScreen)
			{
				g2.scale((double) nativeScreenWidth/screenWidth, (double) nativeScreenHeight/screenHeight);
			}
			else
			{
				g2.scale(1, 1);
			}
			
			drawToScreen(g2);
			
			if(keyH.drawFPS)
			{
//				AffineTransform transform = g2.getTransform();
//				transform.translate(1100, 40);
//				g2.transform(transform);
//				g2.setColor(Color.black);
//				FontRenderContext frc = g2.getFontRenderContext();
//				TextLayout tl = new TextLayout("FPS: " + prevFPS, g.getFont().deriveFont(30F), frc);
//				Shape shape = tl.getOutline(null);
//				g2.setStroke(new BasicStroke(5f));
//				g2.draw(shape);
//				g2.setColor(Color.white);
//				g2.fill(shape);
				
				g2.setFont(new Font("Times New Roman", Font.PLAIN, 20));
				g2.setColor(Color.white);
				g2.drawString("FPS: " + prevFPS, 1260 - g2.getFontMetrics().stringWidth("FPS: " + prevFPS), 20);
			}
			
			g2.dispose();
		}
		catch(Exception e)
		{	
			if(throwMessageOnce)
			{
				e.printStackTrace();
				throwMessageOnce = false;
				StringWriter output = new StringWriter();
				e.printStackTrace(new PrintWriter(output));
				JOptionPane.showMessageDialog(null, output.toString());
				Main.window.dispatchEvent(new WindowEvent(Main.window, WindowEvent.WINDOW_CLOSING));
				sl.saveUserPref();
				System.exit(0);
			}
		}
	}
	
	public boolean callCombatThread = true;
	public boolean combatLoaded = false;
	String loading = "Loading";
	public void drawToScreen(Graphics2D g2)
	{	
		//Checking states of the game
		if(gameState == titleState)
		{
			ts.drawTitle(g2);
			return;
		}
		else if(gameState == playState)
		{
			drawPlayScreen(g2);
		}
		else if(gameState == pauseState)
		{
			ps.draw(g2);
		}
		else if(gameState == menuState)
		{
			drawPlayScreen(g2);
			ms.draw(g2);
		}
		else if(gameState == dialogueState)
		{
			dio.draw(g2);
		}
		else if(gameState == gameOverState)
		{
			drawPlayScreen(g2);
		}
		else if(gameState == combatTransitionState)
		{
			drawPlayScreen(g2);
			//Create a new thread for loading combat
			if(callCombatThread)
			{
				Thread loadCombat = new Thread(new Runnable() 
				{
					@Override
					public void run()
					{
						combat.loadEncounter(forceEncounter);
						
						System.out.println("Combat loaded. Starting");
						
						combatLoaded = true;
					}
				});
				
				//Call loadCombat thread to start running it while transitioning
				loadCombat.start();
				Music.stop();
				
				soundEffect.STOPPED = false;
				playSE(Sound.CombatTrigger);
				
				callCombatThread = false;
				
				if(DEBUG)
				{
					oo.addMessage("Combat triggered");
				}
			}
			
			UI.drawCombatTransition(g2);
			
			if(UI.transitionDrawn)
			{
				g2.setColor(Color.black);
				g2.fillRect(0, 0, screenWidth, screenHeight);
				g2.setColor(Color.white);
				g2.setFont(new Font("Times New Roman", Font.ITALIC, 20));
				g2.drawString(loading, screenWidth/2 - (g2.getFontMetrics().stringWidth(loading))/2, screenHeight/2 - (g2.getFontMetrics().getHeight())/2);
				loading += ".";
				
				if(loading.equals("Loading...."))
				{
					loading = "Loading";
				}
			}
			
			if(soundEffect.STOPPED)
			{
				soundEffect.STOPPED = false;
				playSE(Sound.CombatLoading);
			}
			
			if(combatLoaded && UI.transitionDrawn) //3 seconds
			{
				loading = "Loading";
				stopSE();
				callCombatThread = true;
				UI.transitionDrawn = false;
				combatLoaded = false;
				
				UI.counter = 0;
				playMusic(Sound.Battle1);
				gameState = combatState;
			}
		}
		else if(gameState == combatState)
		{
			UI.drawBackground(g2);
			combat.repaintLoop(g2);
		}
		else if(gameState == merchantState)
		{
//			drawPlayScreen(g2);
			mo.draw(g2);
		}
		else if(gameState == dialogueState)
		{
			drawPlayScreen(g2);
			dio.draw(g2);
		}
		
		
		
		if(console)
		{
			switch(prevState)
			{
			case playState:
				drawPlayScreen(g2);
				break;
			case titleState:
				ts.drawTitle(g2);
				break;
			case combatState:
				UI.drawBackground(g2);
				combat.repaintLoop(g2);
				break;
			case merchantState:
				mo.draw(g2);
				break;
			}
			
			if(co.openWindow)
			{
				co.draw(g2);
			}
			else
			{
				co.openWindow(g2);
			}
		}
		else if(!console && !co.closeWindow)
		{
			co.closeWindow(g2);
		}
		
		
		
		if(displayDebugMsg)
		{
			keyH.konamiCode = -1;
			DEBUG = true;
			if(displayMessage > 0)
			{
				displayMessage--;	
			}
			UI.displayMessage(0, displayMessage, g2);
			if(displayMessage == 0)
			{
				displayDebugMsg = false;
			}
		}
		
		if(keyH.SHOWPATH && DEBUG)
		{
			UI.drawOpenlist(g2);
			UI.drawPath(g2);
			
			pFinder.pathList.clear();
		}
	
		
		if(keyH.COORDINATE && DEBUG)
		{	
			UI.showCoordinates(g2);
		}
		
		if(DEBUG)
		{
			oo.displayMessage(g2);
		}
	}
	
	public void drawPlayScreen(Graphics2D g2)
	{
		//Tiles
		tileM.draw(g2);
		
		//Add things to the Entity arraylist for drawing
		drawList.add(Player);
		
		for(int i = 0; i < NPC.length; i++)
		{
			if(NPC[i] != null)
			{
				drawList.add(NPC[i]);
			}
		}
		
		for(int i = 0; i < obj.length; i++)
		{
			if(obj[i] != null)
			{
				drawList.add(obj[i]);
			}
		}
		
		//Sory by worldY
		Collections.sort(drawList, new Comparator<Entity>() 
		{

			@Override
			public int compare(Entity e1, Entity e2) 
			{
				return Integer.compare(e1.worldY, e2.worldY);
			}
	
		});
		
		//Add tilemaps to be drawn in order
		
		//Draw entites
		for(int i = 0; i < drawList.size(); i++)
		{
			drawList.get(i).draw(g2);
		}
		
		//Empty the list so it doesn't get gigantic per loop
		drawList.clear();
		
		//Draw UI Elements
		if(entities.Player.stamina < Player.maxStamina)
		{
			staminaTimer = 120;
		}
		
		if(staminaTimer > 0)
		{
			staminaTimer--;
			UI.drawStaminaBar(g2, staminaTimer);	
		}
		
		for(int i = 0; i < NPC.length; i++)
		{
			if(NPC[i] != null)
			{
				if(NPC[i].interaction)
				{
					g2.setStroke(new BasicStroke(1));
					g2.setColor(Color.white);
					g2.drawString("Press 'e' to interact", NPC[i].screenX, NPC[i].screenY - 10);
				}
			}
		}
	}
	
	public final static int MinibossGuard = 1;
	
	public void triggerCombat(int i)
	{
		forceEncounter = i;
		gameState = combatTransitionState;
	}
	
	public int musicFile;
	
	public void playMusic(int i)
	{
		musicFile = i;
		Music.setFile(i);
		Music.play();
		Music.loop();		
	}
	
	public void stopMusic()
	{
		Music.stop();
	}
	
	public void playSE(int i)
	{
		
		soundEffect.setFile(i);
		soundEffect.play();
	}
	
	public void stopSE()
	{
		soundEffect.stop();
	}
	
	public void setMouse()
	{
		if(!DEBUG)
		{
			Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/Cursor/debug.png")).getImage(), new Point(this.getY(), this.getX()), "custom cursor");
			setCursor(cursor);	
			return;
		}
		
		//TODO: Determine cursor type by what I'm doing
		if(DEBUG)
		{
			Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/Cursor/normal.png")).getImage(), new Point(this.getY(), this.getX()), "custom cursor");
			setCursor(cursor);		
		}
	}

	public int tile(int i)
	{
		return i / tileSize;
	}
}
