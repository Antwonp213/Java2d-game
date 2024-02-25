package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GameWindow;

public class PauseScreen extends UI
{
	//Menu states
	public final int SET_PAUSE_MENU = 0; public final int PAUSE_MENU_READY = 1; public final int SET_OPTION_MENU = 2; public final int OPTION_MENU_READY = 3; 
	public final int SET_DELETE_SAVE = 4; public final int DELETE_SAVE_READY = 5; public final int SET_LOAD_SAVE_PROMPT = 6; public final int LOAD_SAVE_PROMPT_READY = 7;
	public final int SET_LOAD_MENU = 8; public final int LOAD_MENU_READY = 9; public final int SET_SAVE_MENU = 10; public final int SAVE_MENU_READY = 11;
	
	
	
	SaveBook sb;
	OptionScroll os;
	
	public PauseScreen(GameWindow gp)
	{
		super(gp);
		menuState = 0;
		
		sb = new SaveBook();
		os = new OptionScroll();
	}
	
	private boolean debounceRight = false;
	private boolean debounceESC = false;
	public void update()
	{
		if(gp.keyH.key.contains(gp.keyH.esc))
		{
			debounceESC = true;
		}
		else if(!gp.keyH.key.contains(gp.keyH.esc) && debounceESC)
		{
			debounceESC = false;
			gp.gameState = gp.playState;
		}
		
		if(mouseRightClick)
		{
			debounceRight = true;
		}
		
		//Check ESC and Right click
		if(keyH.BACKSPACE || debounceRight)
		{
			if(mouseRightClick)
			{
				return;
			}
						
			debounceRight = false;
			
			switch(menuState)
			{
			default:
				menuState = SET_PAUSE_MENU;
				break;
			}
		}
		
		//Set buttons
		switch(menuState)
		{
		case SET_PAUSE_MENU:
			setButtons(0);
			menuState = PAUSE_MENU_READY;
			break;
		case SET_LOAD_MENU:
		case SET_SAVE_MENU:
			setButtons(1);
			menuState = menuState == SET_LOAD_MENU ? LOAD_MENU_READY : SAVE_MENU_READY;
			break;
		case SET_OPTION_MENU:
			setButtons(2);
			menuState = OPTION_MENU_READY;
			break;
		case SET_LOAD_SAVE_PROMPT:
			setButtons(3);
			menuState = LOAD_SAVE_PROMPT_READY;
			break;
		}
		
		updateMousePosition();
		
		for(int i = 0; i < buttons.length; i++)
		{
			if(buttons[i].intersects(mouseArea))
			{
				if(mouseLeftClick && mouseSelection != i)
				{
					mouseSelection = i;
				}
			}
		}
		
		if(!mouseLeftClick)
		{
			if(mouseSelection != -1 && !buttons[mouseSelection].intersects(mouseArea))
			{
				mouseSelection = -1;
				return;
			}
			
			updateMenu = mouseSelection;
			mouseSelection = -1;
			
			switch(menuState)
			{
			case PAUSE_MENU_READY:
				switch(updateMenu)
				{
				case 0:
					gp.keyH.MENU = false;
					gp.gameState = gp.playState;
					break;
				case 1:
					//Ask Save/Load
					menuState = SET_LOAD_SAVE_PROMPT;
					break;
				case 2:
					//Options
					break;
				case 3:
					//Ask if you want to go back to TitleScreen
					break;
				case 4:
					System.exit(0);
					break;
				}
				break;
			case LOAD_SAVE_PROMPT_READY:
				switch(updateMenu)
				{
				case 0:
					//Load menu
					menuState = SET_LOAD_MENU;
					break;
				case 1:
					//Save menu
					menuState = SET_SAVE_MENU;
					break;
				}
				break;
			case LOAD_MENU_READY:
				switch(updateMenu)
				{
				case 0:
				case 1:
				case 2:
				case 3:
					System.out.println("Loading Game");
					if(sl.checkSave(updateMenu))
					{
						sl.load(updateMenu);
						menuState = SET_PAUSE_MENU;
						gp.keyH.MENU = false;
					}
					else
					{
						System.out.println("Save does not exist");
					}
					break;
				case 4:
				case 5:
				case 6:
				case 7:
					System.out.println("Deleting Save");
					sl.deleteSave(updateMenu - 4);
					break;
				}
				break;
			case SAVE_MENU_READY:
				switch(updateMenu)
				{
				case 0:
				case 1:
				case 2:
				case 3:
					System.out.println("Saving Game");
					sl.save(updateMenu);
					break;
				case 4:
				case 5:
				case 6:
				case 7:
					sl.deleteSave(updateMenu - 4);
					break;
				}
				break;
			}
		}
		
	}
	
	public void setButtons(int set)
	{
		switch(set)
		{
		case 0:
			buttons = new Rectangle[5];
			buttons[0] = new Rectangle(gp.screenWidth/2, 90, 80, 15);
			buttons[1] = new Rectangle(gp.screenWidth/2, 190, 90, 15);
			buttons[2] = new Rectangle(gp.screenWidth/2, 290, 41, 15);
			buttons[3] = new Rectangle(gp.screenWidth/2, 390, 122, 15);
			buttons[4] = new Rectangle(gp.screenWidth/2, 490, 22, 15);
			break;
		case 1:
			buttons = new Rectangle[8];
			//Save buttons
			buttons[0] = new Rectangle(200, 200, 200, 200);
			buttons[1] = new Rectangle(500, 200, 200, 200);
			buttons[2] = new Rectangle(200, 500, 200, 200);
			buttons[3] = new Rectangle(500, 500, 200, 200);
			//DeleteButtons
			buttons[4] = new Rectangle(370, 210, 25, 25);
			buttons[5] = new Rectangle(670, 210, 25, 25);
			buttons[6] = new Rectangle(370, 510, 25, 25);
			buttons[7] = new Rectangle(670, 510, 25, 25);
			break;
		case 3:
			buttons = new Rectangle[2];
			buttons[0] = new Rectangle(400, 340, 26, 15);
			buttons[1] = new Rectangle(400, 440, 26, 15);
			break;
		}
	}
	
	//Logic stuff
	
	public void draw(Graphics2D g2)
	{
		drawTempScreen(g2);
		
		switch(menuState)
		{
		case LOAD_SAVE_PROMPT_READY:
			drawSLPrompt(g2);
			break;
		case LOAD_MENU_READY:
		case SAVE_MENU_READY:
			for(int i = 0; i < buttons.length; i++)
			{
				if(i >= 4)
				{
					g2.setColor(new Color(150, 150, 150));
				}
				else
				{
					g2.setColor(Color.white);
				}
				g2.fillRect(buttons[i].x, buttons[i].y, buttons[i].width, buttons[i].height);
			}
			break;
		}
	}
	
	public void drawSLPrompt(Graphics2D g2)
	{
		g2.setColor(Color.blue);
		g2.fillRect(300, 300, 300, 200);
		g2.setColor(Color.white);
		g2.drawString("Load", 400, 350);
		g2.drawString("Save", 400, 450);
	}
	
	public void drawTempScreen(Graphics2D g2)
	{
		g2.setColor(Color.black);
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		g2.setColor(Color.white);
		g2.drawString("Resume Game", gp.screenWidth/2, 100);
		g2.drawString("Save/Load Game", gp.screenWidth/2, 200);
		g2.drawString("Options", gp.screenWidth/2, 300);
		g2.drawString("Go back to Title Screen", gp.screenWidth/2, 400);
		g2.drawString("Quit", gp.screenWidth/2, 500);
	}
}
