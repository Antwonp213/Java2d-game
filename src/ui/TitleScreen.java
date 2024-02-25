package ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import data.DataStorage;
import data.SaveLoad;
import main.GameWindow;
import main.Sound;
import main.UtilityTool;

public class TitleScreen extends UI implements KeyListener
{	
	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		
	}

	private boolean ESC = false;
	private boolean ENTER = false;
	@Override
	public void keyReleased(KeyEvent e) 
	{
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_ESCAPE)
		{
			ESC = true;
		}
		
		if(key == KeyEvent.VK_ENTER)
		{
			ENTER = true;
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//MENU STATES
	public final int SET_MAIN_MENU = 0; public final int MAIN_MENU_READY = 1; public final int SET_LOAD_MENU = 2; public final int LOAD_MENU_READY = 3;
	public final int SET_OPTION_MENU = 4; public final int OPTION_MENU_READY = 5; public final int CLOSE_LOAD_MENU = 6; public final int CLOSE_OPTION_MENU = 7; 
	public final int SET_EMPTY_PROMPT = 8; public final int EMPTY_PROMPT_READY = 9; public final int SET_LOCAL_SAVE_INFO = 10; public final int LOCAL_SAVE_INFO_READY = 11;
	//TRANSITION MENU
	private final int START = 0; private final int LOAD = 1; private final int OPTION = 2; private final int QUIT = 3;
	
	Sound Title = new Sound();
	Clip clip;
	Clip bookSFX;
	URL soundURL;
	URL openBookURL;
	URL closeBookURL;
	SaveLoad sl = new SaveLoad(gp);
	public boolean titleListenerAdded = false;
	public int loadFile = 0;
	public boolean loadSave = false;
	int highlightButton = -1;
	public float volF = 3f;
	
	Thread tOne = new Thread(new Runnable() 
	{
		@Override
		public void run() 
		{		
			String load = "";
			for(int i = 0; i < 12; i++)
			{
				if(i < 10)
				{
					load = "0" + i;
				}
				else
				{
					load = "" + i;
				}
				
				titleFrames[i] = setup("/TitleScreen/frame" + load);
			}
			
			for(int i = 0; i < 8; i++)
			{
				bookOpen[i] = setup("/TitleScreen/UI/BookOpen/" + i);
			}
		}
	});
	
	Thread tTwo = new Thread(new Runnable() 
	{
		@Override
		public void run() 
		{			
			for(int i = 12; i < 23; i++)
			{
				titleFrames[i] = setup("/TitleScreen/frame" + i);
			}
			
			for(int i = 0; i < 8; i++)
			{
				bookClose[i] = setup("/TitleScreen/UI/BookClose/" + i);
			}
		}
	});
	
	Thread tThree = new Thread(new Runnable() 
	{
		@Override
		public void run()
		{
			for(int i = 0; i < 8; i++)
			{
				scrollClose[i] = setup("/TitleScreen/UI/ScrollClose/" + i);
			}
			
			for(int i = 0; i < 8; i++)
			{
				scrollOpen[i] = setup("/TitleScreen/UI/ScrollOpen/" + i);
			}
		}
	});
	
	BufferedImage[] titleFrames = new BufferedImage[23];
	BufferedImage populSave;
	BufferedImage emptySave;
	BufferedImage TitleText;
	BufferedImage OptionText;
	BufferedImage EmptyPrompt;
	BufferedImage OptionsIMG;
	BufferedImage LocalInfo;
	int frameIDX = 0;
	Rectangle[] YesNo;
	
	public TitleScreen(GameWindow gp)
	{
		super(gp);
		
		loadImages();
	}
	
	FloatControl fcMusic;
	FloatControl fcSFX;
	public void setBookSFX(int i)
	{
		openBookURL = getClass().getResource("/Sounds/BookOpen.wav");
		closeBookURL = getClass().getResource("/Sounds/BookClose.wav");
		
		URL sound = soundURL;
		
		switch(i)
		{
		case 0:
			sound = openBookURL;
			break;
		case 1:
			sound = closeBookURL;
			break;
		}
		
		try
		{
			AudioInputStream ais = AudioSystem.getAudioInputStream(sound);
			bookSFX = AudioSystem.getClip();
			bookSFX.open(ais);
			
			fcSFX = (FloatControl)bookSFX.getControl(FloatControl.Type.MASTER_GAIN);
			
			updateVolume();
			bookSFX.start();
		}
		catch(Exception e)
		{
			System.err.println("Music does not exist, exitting system.");
			System.exit(0);
		}
	}
	
	public void setMusic()
	{
		soundURL = getClass().getResource("/Music/TitleScreen.wav");
		try
		{
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
			clip = AudioSystem.getClip();
			clip.open(ais);
			
			fcMusic = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		catch(Exception e)
		{
			System.err.println("Music does not exist, exitting system.");
			System.exit(0);
		}
	}
	
	public void loadImages()
	{
		tOne.start();
		tTwo.start();
		tThree.start();
		
		StaticBook = setup("/TitleScreen/UI/StaticBook");
		StaticScroll = setup("/TitleScreen/UI/StaticScroll");
		
		TitleText = setupNS("TitleText");
		OptionText = setupNS("OptionText");
		populSave = setupNS("PopulatedSaveSlot");
		emptySave = setupNS("EmptySaveSlot");
		EmptyPrompt = setupNS("EmptyPrompt");
		OptionsIMG = setupNS("Options");
		LocalInfo = setupNS("Local Info");
	}
	
	@Override
	public BufferedImage setupNS(String filePath)
	{
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(getClass().getResourceAsStream("/TitleScreen/UI/" + filePath + ".png"));
		}
		catch(IOException e)
		{
			System.err.println(filePath + " does not exist, exitting system.");
			System.exit(0);
		}
		
		return image;
	}
	
	public BufferedImage setup(String filePath)
	{
		UtilityTool uTool = new UtilityTool();
		BufferedImage preScale = null;
		try
		{
			preScale = ImageIO.read(getClass().getResourceAsStream(filePath + ".png"));
			preScale = uTool.scaleImage(preScale, gp.screenWidth, gp.screenHeight);
		}
		catch(Exception e)
		{
			System.err.println(filePath + " does not exist, exitting system.");
			System.exit(0);
		}
		return preScale;
	}
	
	public BufferedImage setupB(String filePath)
	{
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(getClass().getResourceAsStream(filePath + ".png"));
		}
		catch(Exception e)
		{
			System.err.println(filePath + " does not exist, exitting system.");
			System.exit(0);
		}
		return image;
	}
	
	int kbHighlight = 0;
	int kbSelection = -1;
	int menuState = SET_MAIN_MENU;
	boolean musicStart = false;
	boolean setVol = true;
	boolean mouseBack = false;

	public void updateTitle()
	{	
		if(mouseRightClick && !mouseBack)
		{
			mouseBack = true;
		}
		
		if((ESC || mouseBack) && !(menuState == SET_MAIN_MENU || menuState == MAIN_MENU_READY))
		{	
			boolean mouseReturnRight = mouseBack && !mouseRightClick;
						
			if((menuState == SET_LOAD_MENU || menuState == LOAD_MENU_READY) && mouseReturnRight)
			{
				menuState = CLOSE_LOAD_MENU;
				setBookSFX(1);
			}
			else if((menuState == SET_OPTION_MENU || menuState == OPTION_MENU_READY) && mouseReturnRight)
			{
				menuState = CLOSE_OPTION_MENU;
				setBookSFX(1);
			}
			else if((menuState == SET_EMPTY_PROMPT || menuState == EMPTY_PROMPT_READY) && mouseReturnRight)
			{
				setButtons(1);
				menuState = LOAD_MENU_READY;
			}
			else if((menuState == SET_LOCAL_SAVE_INFO || menuState == LOCAL_SAVE_INFO_READY) && mouseReturnRight)
			{
				setButtons(2);
				menuState = OPTION_MENU_READY;
			}
			
			ESC = false;
			
			if(!mouseRightClick)
			{
				mouseBack = false;
			}
		}
		
		if(!musicStart)
		{
			musicStart = true;
			setMusic();
			initVolume();
		}
		
		if(!opaque)
		{
			return;
		}
		
		//Set buttons based on state
		switch(menuState)
		{
		//Main menu screen
		case SET_MAIN_MENU:
			if(!bookState)
			{
				setButtons(0);
				menuState = MAIN_MENU_READY;
			}
			break;
		//Load menu screen
		case SET_LOAD_MENU:
			if(bookState)
			{
				setButtons(1);
				menuState = LOAD_MENU_READY;
				bookState = false;
			}
			break;
		//Options screen
		case SET_OPTION_MENU:
			if(scrollState)
			{
				setButtons(2);
				menuState = OPTION_MENU_READY;
				scrollState = false;
			}
			break;
		case SET_EMPTY_PROMPT:
			if(emptyFileState)
			{
				setButtons(3);
				menuState = EMPTY_PROMPT_READY;
				emptyFileState = false;
			}
			break;
		case SET_LOCAL_SAVE_INFO:
			System.out.println("Setting buttons for Local Save Info");
			setButtons(4);
			menuState = LOCAL_SAVE_INFO_READY;
			break;
		default:
			break;
		}
		
		updateMousePosition();
		
		for(int i = 0; i < buttons.length; i++)
		{
			if(buttons[i].intersects(mouseArea))
			{
				kbHighlight = i;
				
				if(menuState == OPTION_MENU_READY && i == 1 && mouseLeftClick)
				{
					updateVolumeKnob();
					return;
				}
				
				if(ENTER)
				{
					kbHighlight = kbSelection;
					ENTER = false;
				}
				
				if(mouseLeftClick && mouseSelection != i)
				{
					mouseSelection = i;
				}
			}
		}
		
		if(KMxSet)
		{
			KMxSet = false;
		}
		
		//Debounce mouse if clicked, but if it moved away from option, reset it.
		if(!mouseLeftClick && mouseSelection != -1)
		{
			if(!buttons[mouseSelection].intersects(mouseArea))
			{
				mouseSelection = -1;
				return;
			}
		}
		
		//Debounce mouse and assign the selection
		if(!mouseLeftClick)
		{
			if(mouseSelection == -1)
			{
				return;
			}
			updateMenu = mouseSelection;
			mouseSelection = -1;
			
			switch(menuState)
			{
			//Main menu
			case MAIN_MENU_READY:
				switch(updateMenu)
				{
				case -1:
					break;
				//Start Game
				case START:
					clip.stop();
					gp.gameState = gp.playState;
					break;
				//Load Option
				case LOAD:
					setBookSFX(0);
					menuState = SET_LOAD_MENU;
					break;
				//Options
				case OPTION:
					setBookSFX(0);
					menuState = SET_OPTION_MENU;
					break;
				//Quit
				case QUIT:
					//TODO: Prompt for Yes/No on quitting
					System.exit(0);
					break;
				}
				break;
			//Load screen
			case LOAD_MENU_READY:
				switch(updateMenu)
				{
				case -1:
					break;
				case 0:
					menuState = SET_MAIN_MENU;
				//Autosave
				case 1:
				case 2:
				case 3:
					if(sl.checkSave(updateMenu))
					{
						System.out.println("Save exists, confirm load.");
						//Confirm load - For now, just load the game
						sl.load(updateMenu);
						gp.gameState = gp.playState;
					}
					else
					{
						System.out.println("Save empty");

						menuState = SET_EMPTY_PROMPT;
						emptyFileState = true;
					}
					break;
				}
				break;
			case EMPTY_PROMPT_READY:
				if(updateMenu == 0)
				{
					setButtons(1);
					menuState = LOAD_MENU_READY;
				}
				break;
			//Option menu
			case OPTION_MENU_READY:
				switch(updateMenu)
				{
				//Back Button
				case 0:
					menuState = CLOSE_OPTION_MENU;
					break;
				case 1:
					//This one is a special little boy.
					break;
				case 2:
					MUTE = !MUTE;
					updateVolume();
					break;
				case 3:
					gp.keyH.FULLSCREEN = !gp.keyH.FULLSCREEN;
					break;
				case 4:
					SaveLoad.local = !SaveLoad.local;
					System.out.println("Local save set to: " + SaveLoad.local);
					break;
				case 5:
					updateVKRelativeToBar();
					break;
				case 6:
					menuState = SET_LOCAL_SAVE_INFO;
					break;
				}
				break;
			case LOCAL_SAVE_INFO_READY:
				switch(updateMenu)
				{
				case 0:
					SaveLoad.local = true;
					setButtons(2);
					menuState = OPTION_MENU_READY;
					break;
				case 1:
					setButtons(2);
					menuState = OPTION_MENU_READY;
					break;
				case 2:
					SaveLoad.local = false;
					setButtons(2);
					menuState = OPTION_MENU_READY;
					break;
				}
				break;
			}
		}
	}
	
	public void updateVKRelativeToBar()
	{
		knobX = mouseArea.x - mouseArea.width/2;
		
		if(knobX > vkMaxX)
		{
			knobX = vkMaxX;
		}
		else if(knobX < vkMinX)
		{
			knobX = vkMinX;
		}
		
		updateVolume();
	}
	
	//KnobX vs MouseX
	boolean KMxSet = false;
	public boolean MUTE = false;
	int KMx = 0;
	public void updateVolumeKnob()
	{
		//TODO: Don't shift knob, keep it to where mouseArea.x is unless you start moving it.
		if(!KMxSet)
		{
			KMx = knobX - mouseArea.x;	
			KMxSet = true;
		}
		
		knobX = mouseArea.x + KMx;
		if(knobX < vkMinX)
		{
			knobX = vkMinX;
		}
		else if(knobX > vkMaxX)
		{
			knobX = vkMaxX;
		}
		
		updateVolume();
	}
	
	public void initVolume()
	{
		if(knobX < vkMinX)
		{
			knobX = vkMinX;
		}
		else if(knobX > vkMaxX)
		{
			knobX = vkMaxX;
		}	
		
		vol = (int) (((double) (knobX - vkMinX) /(vkMaxX - vkMinX)) * 100);
		
		//Update Float Volume Control here
		if(MUTE)
		{
			volF = -80f;
		}
		else
		{
			volF = ((float) (vol * 54)/100) - 48;
			
			if(volF == -48)
			{
				volF = -80f;
			}			
		}
		
		gp.Music.volume = volF;
		gp.soundEffect.volume = volF;
//		fcSFX.setValue(volF);
		fcMusic.setValue(volF);
	}
	
	public int vol;
	public void updateVolume()
	{
		buttons[1].x = knobX;
		volumeX = knobX - 299;
		
		//Calc percentage based on distance from min - Use logarithmic math. Ew.
		vol = (int) (((double) (knobX - vkMinX) /(vkMaxX - vkMinX)) * 100);
		
		//Update Float Volume Control here
		if(MUTE)
		{
			volF = -80f;
		}
		else
		{
			volF = ((float) (vol * 54)/100) - 48;
			
			if(volF == -48)
			{
				volF = -80f;
			}			
		}
		
		if(volF < -80)
		{
			volF = -80f;
		}
		
		gp.Music.volume = volF;
		gp.soundEffect.volume = volF;
		fcSFX.setValue(volF);
		fcMusic.setValue(volF);
	}
	
	public void setButtons(int screenView)
	{
		buttons = null;
		switch(screenView)
		{
		//Main Menu
		case 0:
			buttons = new Rectangle[4];
			//START
			buttons[0] = new Rectangle(584, 225, 130, 45);
			//LOAD
			buttons[1] = new Rectangle(583, 354, 132, 51);
			//OPTIONS
			buttons[2] = new Rectangle(540, 492, 216, 62);
			//QUIT
			buttons[3] = new Rectangle(588, 625, 122, 59);
			break;
		//Load Menu
		case 1:
			buttons = new Rectangle[4];
			//Auto-save
			buttons[0] = new Rectangle(loadX1, loadY1, 542, 228);
			//Save 1
			buttons[1] = new Rectangle(loadX2, loadY1, 542, 228);
			//Save 2
			buttons[2] = new Rectangle(loadX1, loadY2, 542, 228);
			//Save 3
			buttons[3] = new Rectangle(loadX2, loadY2, 542, 228);
			break;
		//Options Menu
		case 2:
			buttons = new Rectangle[7];
			
			buttons[0] = new Rectangle(243, 174, 48, 39);
			buttons[1] = new Rectangle(knobX, knobY, 25, 25);
			buttons[2] = new Rectangle(299, 350, 18, 18);
			buttons[3] = new Rectangle(299, 431, 18, 18);
			buttons[4] = new Rectangle(299, 512, 18, 18);
			buttons[5] = new Rectangle(299, 269, 198, 18);
			buttons[6] = new Rectangle(412, 511, 86, 22);
			break;
		//Emtpy File Prompt
		case 3:
			buttons = new Rectangle[1];
			buttons[0] = new Rectangle(593, 399, 94, 32);
			break;
		case 4:
			System.out.println("Setting case 4");
			buttons = new Rectangle[3];
			buttons[0] = new Rectangle(247, 538, 223, 78);
			buttons[1] = new Rectangle(528, 538, 223, 78);
			buttons[2] = new Rectangle(809, 538, 223, 78);
		//Load Save Yes/No buttons
		}
	}
	
	public void resetOpaque()
	{
		opaque = false;
		opacityInt = 0;
		opacity = 0.0f;
		titleY = 0;
		counter = 0;
	}
	
	int counter = 0;
	float opacity = 0.0f;
	int opacityInt = 0;
	int titleY = 0;
	boolean opaque = false;
	private int countLimit = 8;
	int hlOpacity = 0;
	boolean hlFlipFlop = true;
	
	boolean bookState = false;
	boolean emptyFileState = false;
	BufferedImage StaticBook;
	BufferedImage[] bookOpen = new BufferedImage[8];
	BufferedImage[] bookClose = new BufferedImage[8];
	int bookCounter = 0;
	int bookFrame = 0;
	
	boolean scrollState = false;
	BufferedImage StaticScroll;
	BufferedImage[] scrollOpen = new BufferedImage[8];
	BufferedImage[] scrollClose = new BufferedImage[8];
	int scrollCounter = 0;
	int scrollFrame = 0;

	public void drawTitle(Graphics2D g2)
	{	
		if(!opaque)
		{
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			drawBackDrop(g2);
			drawAlphaComposite(g2);
			return;
		}
		
		drawBackDrop(g2);
		
		switch(menuState)
		{
		case MAIN_MENU_READY:
			drawMainMenu(g2);
			break;
		case SET_EMPTY_PROMPT:
		case LOAD_MENU_READY:
			drawBook(g2);
			drawLoadMenu(g2);
			break;
		case SET_LOCAL_SAVE_INFO:
		case OPTION_MENU_READY:
			drawScroll(g2);
			drawOptionsMenu(g2);
			break;
		case CLOSE_LOAD_MENU:
			closeBookAnim(g2);
			break;
		case CLOSE_OPTION_MENU:
			closeScrollAnim(g2);
			if(scrollFrame < 5)
			{
				drawOptionsMenu(g2);
			}
			break;
		case SET_LOAD_MENU:
			openBookAnim(g2);
			break;
		case SET_OPTION_MENU:
			openScrollAnim(g2);
			if(scrollFrame > 4)
			{
				drawOptionsMenu(g2);
			}
			break;
		case EMPTY_PROMPT_READY:
			drawBook(g2);
			drawLoadMenu(g2);
			drawEmptyPrompt(g2);
			break;
		case LOCAL_SAVE_INFO_READY:
			drawScroll(g2);
			drawOptionsMenu(g2);
			drawLocalInfo(g2);
			break;
		}
	}
	
	public void drawLocalInfo(Graphics2D g2)
	{
		g2.drawImage(LocalInfo, 190, 60, null);
	}
	
	private final int vkMaxX = 486;
	private final int vkMinX = 285;
	public int knobX;
	private final int knobY = 265;
	public int volumeX = knobX - 299;
	public void drawOptionsMenu(Graphics2D g2)
	{
		g2.drawImage(OptionsIMG, 243, 174, null);
		
		//Draw Volume Bar
		g2.setColor(new Color(196, 36, 201, 150));
		g2.fillRect(299, 269, volumeX, 18);
		
		g2.setColor(new Color(27, 34, 54));
		g2.fillRect(knobX, knobY, 25, 25);
		
		//Draw Fullscren, etc
		g2.setColor(new Color(196, 36, 201));
		
		//Mute sound
		if(MUTE)
		{
			g2.fillRect(299, 350, 18, 18);
		}
		
		//Fullscreen
		if(gp.fullScreen)
		{
			g2.fillRect(299, 431, 18, 18);
		}
		
		//When writing file to save it, do we store it locally within the project, or externally to the user's PC?
		if(SaveLoad.local)
		{
			g2.fillRect(299, 512, 18, 18);
		}
	}
	
	public void drawEmptyPrompt(Graphics2D g2)
	{
		g2.drawImage(EmptyPrompt, gp.screenWidth/2 - 150, gp.screenHeight/2 - 100, null);
	}
	
	final int loadX1 = 57; final int loadX2 = 684;
	final int loadY1 = 120; final int loadY2 = 360;
	public void drawLoadMenu(Graphics2D g2)
	{
		//Draw 4 panels
		//TODO: Determine if save file exists, if so load populated from stats and apply. IDEA: TempLoad the save, then draw out its essential stats
		
		DataStorage saveData = new DataStorage();
		
		if(sl.checkSave(0))
		{
			g2.drawImage(populSave, loadX1, loadY1, null);
			sl.loadTemp(0, saveData);
		}
		else
		{
			g2.drawImage(emptySave, loadX1, loadY1, null);
		}
		
		if(sl.checkSave(1))
		{
			g2.drawImage(populSave, loadX2, loadY1, null);
			sl.loadTemp(1, saveData);
		}
		else
		{
			g2.drawImage(emptySave, loadX2, loadY1, null);
		}
		
		if(sl.checkSave(2))
		{
			g2.drawImage(populSave, loadX1, loadY2, null);
			sl.loadTemp(2, saveData);
		}
		else
		{
			g2.drawImage(emptySave, loadX1, loadY2, null);
		}
		
		if(sl.checkSave(3))
		{
			g2.drawImage(populSave, loadX2, loadY2, null);
			sl.loadTemp(3, saveData);
		}
		else
		{
			g2.drawImage(emptySave, loadX2, loadY2, null);
		}
	}
	
	public void drawScroll(Graphics2D g2)
	{
		g2.drawImage(StaticScroll, 0, 0, null);
	}
	
	public void openScrollAnim(Graphics2D g2)
	{
		g2.drawImage(scrollOpen[scrollFrame], 0, 0, null);
		updateScrollFrame();
	}
	
	public  void closeScrollAnim(Graphics2D g2)
	{
		g2.drawImage(scrollClose[scrollFrame], 0, 0, null);
		updateScrollFrame();
	}
	
	public void updateScrollFrame()
	{
		int countLimit;
		if(keyH.FULLSCREEN)
		{
			countLimit = 10;
		}
		else
		{
			countLimit = 25;
		}
		
		scrollCounter++;
		if(scrollCounter == countLimit)
		{
			scrollCounter = 0;
			scrollFrame++;
			if(scrollFrame > 7)
			{
				scrollFrame = 0;
				scrollState = true;
				if(menuState == CLOSE_OPTION_MENU)
				{
					scrollState = false;
					menuState = SET_MAIN_MENU;
				}
			}
		}
	}
	
	public void drawBook(Graphics2D g2)
	{
		g2.drawImage(StaticBook, 0, 0, null);
	}
	
	public void openBookAnim(Graphics2D g2)
	{
		g2.drawImage(bookOpen[bookFrame], 0, 0, null);
		updateBookFrame();
	}
	
	public  void closeBookAnim(Graphics2D g2)
	{
		g2.drawImage(bookClose[bookFrame], 0, 0, null);
		updateBookFrame();
	}
	
	
	public void updateBookFrame()
	{
		int countLimit;
		if(keyH.FULLSCREEN)
		{
			countLimit = 5;
		}
		else
		{
			countLimit = 15;
		}
		
		bookCounter++;
		if(bookCounter == countLimit)
		{
			bookCounter = 0;
			bookFrame++;
			if(bookFrame > 7)
			{
				bookFrame = 0;
				bookState = true;
				if(menuState == CLOSE_LOAD_MENU)
				{
					bookState = false;
					menuState = SET_MAIN_MENU;
				}
			}
		}
	}
	
	public void drawMainMenu(Graphics2D g2)
	{	
		g2.drawImage(TitleText, 115, titleY, null);
		
		g2.drawImage(OptionText, 540, 225, null);
	}
	
	int opacityCounter = 0;
	public void drawAlphaComposite(Graphics2D g2)
	{
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		
		g2.drawImage(TitleText, 115, titleY, null);
		
		g2.drawImage(OptionText, 540, 225, null);
		
		opacityCounter++;
		if(opacityCounter == 3)
		{
			opacityCounter = 0;
			
			if(titleY != 79)
			{
				titleY++;
			}
			
			if(opacity < 0.99)
			{
				opacityInt += 5;
				opacity = (float) ((float) opacityInt/255.0);
			}
		}
		
		if(opacityInt >= 255)
		{
			opaque = true;
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}
	}
	
	long prevTime;
	long currTime;
	public void drawBackDrop(Graphics2D g2)
	{
		prevTime = System.nanoTime();
		g2.drawImage(titleFrames[frameIDX], 0, 0, null);
		currTime = System.nanoTime();
		
//		if((currTime - prevTime) > 5000000)
//		{
//			countLimit = 1;
//		}
//		else
//		{
//			countLimit = 5;
//		}
		//Convert to milliSeconds, 7 milliseconds in a frame, target is 8 frames per second
		countLimit = (int) ((currTime - prevTime)/1000000 + 0.5);
		
//		How fast do I want it to play... Probably 11 frames per second
//		int frameLimit = (int) ((60.0/titleFrames.length) + 0.5);
//		
//		System.out.println(frameLimit);
		
		if(gp.keyH.FULLSCREEN)
		{
			countLimit = countLimit - (countLimit - 2);
		}
		else
		{
			countLimit += countLimit;
		}
				
		counter++;
		if(counter >= countLimit)
		{
			frameIDX++;
			if(frameIDX > titleFrames.length - 1)
			{
				frameIDX = 0;
			}
			counter = 0;
		}
	}
	
	public boolean getMute()
	{
		return MUTE;
	}
}
