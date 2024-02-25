package debug;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import data.GenerateEntity;
import main.GameWindow;

public class ConsoleOverlay extends Console
{
	Color window = new Color(0, 0, 0, 200);
	Color uText = new Color(52, 191, 42);
	Color cText = new Color(191, 109, 33);
	Color hText = new Color(66, 135, 245);
	Color tHighlight = new Color(55, 110, 230);
	Color cInvalid = new Color(191, 69, 41);
	Color white = Color.white;
	Color black = Color.black;
	
	Rectangle scrollBar;
	boolean entryScroll = false;
	int trueY = 0;
	int scrollPage = 0;
	int scrollbarSize = 0;
	int maxEntries = 0;
	int currEntries = 0;
	int topEntry = 0;
	int bottomEntry = 0;
	int scrollPos = 0;
	int size = 0;
	//Precise to avoid integer rounding errors on scrollbar
	double precisePos = 0;
	double preciseOffset = 0;
	
	int prevCmdSize = 0;
	
	int cursorOpacity = 0;
	boolean blink = true;
	
	public void addLine(Color color, String txt)
	{
		commands.add(new CommandPrompt(color, txt));
	}
	
	public void addLine(String txt)
	{
		commands.add(new CommandPrompt(txt));
	}
	
	class CommandPrompt
	{
		Color color;
		String text;
		
		CommandPrompt(Color color, String text)
		{
			this.color = color;
			this.text = text;
		}
		
		CommandPrompt(String text)
		{
			color = cText;
			this.text = text;
		}
	}
	
	ArrayList<CommandPrompt> commands = new ArrayList<CommandPrompt>();
	
	GameWindow gp;
	public int y;
	public ConsoleOverlay(GameWindow gp)
	{
		super(gp);
		this.gp = gp;
	}
	
	public void draw(Graphics2D g2)
	{		
		g2.setColor(window);
		g2.fillRect(0, 0, gp.screenWidth, y);
		
		g2.setColor(white);
		g2.fillRect(0, y - 20, gp.screenWidth, 20);
		
		g2.setColor(black);
		g2.setFont(new Font("Constantia", Font.PLAIN, 20));
		g2.drawString(userInput, 10, y - 3);
		
		if(entryScroll)
		{			
			scrollBar = new Rectangle(gp.screenWidth - 20, scrollPos, 20, scrollbarSize);
			
			g2.setColor(new Color(171, 38, 166));
			g2.fillRect(gp.screenWidth - 20, 0, 20, y - 20);
			
			if(!mouseArea.intersects(scrollBar))
			{
				g2.setColor(new Color(0, 0, 0, 100));
			}
			else
			{
				g2.setColor(new Color(0, 0, 0, 150));
				if(leftClick)
				{
					g2.setColor(new Color(0, 0, 0, 255));
				}
			}
			
			g2.fillRect(scrollBar.x, scrollBar.y, scrollBar.width, scrollBar.height);
			
			g2.setColor(Color.green);
			g2.fillRect(mouseArea.x, mouseArea.y, mouseArea.width, mouseArea.height);
		}
		
		for(int i = topEntry; i < bottomEntry; i++)
		{			
			g2.setColor(commands.get(i).color);
			g2.drawString(commands.get(i).text, 8, 19 + ((i - topEntry) * 19));
		}
		
		g2.setColor(new Color(0, 0, 0, cursorOpacity));
		
		if(userInput.length() == 0)
		{
			g2.fillRect(g2.getFontMetrics().stringWidth(userInput) + 15, y - 19, 2, 18);
		}
		else
		{
			int charLen = g2.getFontMetrics().charWidth(userInput.charAt(userInput.length() - 1));
			int strLen = g2.getFontMetrics().stringWidth(userInput);
			
			charLen = charLen < 11 ? 11 : charLen;
			
			g2.fillRect(strLen + charLen, y - 19, 2, 18);
		}
		
	}
	
	public boolean openWindow = false;
	public void openWindow(Graphics2D g2)
	{
		g2.setColor(window);
		g2.fillRect(0, 0, gp.screenWidth, y);
		if(y < (gp.screenHeight/2) + 20)
		{
			y += 10;
		}
		else
		{
			openWindow = true;
			closeWindow = false;
		}
	}
	
	public boolean closeWindow = false;
	public void closeWindow(Graphics2D g2)
	{
		g2.setColor(window);
		g2.fillRect(0, 0, gp.screenWidth, y);
				
		if(y > 0)
		{
			y -= 10;
		}
		else
		{
			closeWindow = true;
			openWindow = false;
		}
	}
	
	public int repeatLimit = 5;
	String userInput = "";
	String highlight = "";
	boolean leftClickSet = false;
	boolean moving = false;
	public static boolean caps = false;
	int debugCount = 0;
	boolean enterPassword = false;
	int prevCmdIdx = 0;
	public void update()
	{
		super.update();
				
		if(!openWindow && gp.console)
		{
			key.clear();
			return;
		}
		
		if(closeWindow && !gp.console)
		{
			key.clear();
			return;
		}
		
		if(key.contains(Console.tilde) && key.contains(shiftMask))
		{
			gp.keyH.CONSOLE = true;
			userInput = "";
			return;
		}
		
		int keyCode;
		int maskCode;
		char c;
		
//		if(key.size() > 0)
//		{
//			System.out.println("Keys: " + key.toString());
//		}
		
		for(int i = 0; i < key.size(); i++)
		{	
			if(key.size() > 1)
			{
				maskCode = key.get(1);
				keyCode = key.get(0);
				
				if(maskCode == Console.shiftMask)
				{
					if(getShiftMask(keyCode))
					{
						continue;
					}
					
					if(keyCode == 0x3B)
					{
						keyCode--;
					}
					
					c = (char) keyCode;
					
					userInput += Character.toString(c);
					break;
				}
				
				if(maskCode == Console.ctrlMask) 
				{
					c = (char) keyCode;
					c = Character.toLowerCase(c);
					
					Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
					
					switch(c)
					{
					//search
					case 'f':
						break;
					//Select whole input
					case 'a':
						break;
					//Undo
					case 'z':
						break;
					//Redo
					case 'y':
						break;
					//copy to clipboard
					case 'c':
						StringSelection ss = new StringSelection(userInput);
						clip.setContents(ss, null);
						break;
					//paste to input
					case 'v':
						try 
						{
							userInput += (String) clip.getData(DataFlavor.stringFlavor);
						} 
						catch (UnsupportedFlavorException | IOException e) 
						{
							e.printStackTrace();
						}
						break;
					}
					
					break;
				}
			}
			
			keyCode = key.get(i);
			
			if(keyCode == Console.enter)
			{
				submitInput();
				continue;
			}
			
			if(keyCode == Console.alt || keyCode == Console.capslock || keyCode == Console.shiftMask || keyCode == Console.ctrlMask || keyCode == Console.esc)
			{
				continue;
			}
			
			if(keyCode == Console.tab)
			{
				userInput += "     ";
				continue;
			}
			
			if(keyCode == 0xDE)
			{
				userInput += "'";
				key.remove(0);
				continue;
			}
			
			if(prevCmdSize != commands.size() && (keyCode == Console.up || keyCode == Console.down) && commands.size() > 0)
			{
				prevCmdSize = commands.size();
				for(int j = prevCmdSize - 1; j >= 0; j--)
				{
					if(commands.get(j).color == uText)
					{
						prevCmdIdx = j;
						break;
					}
				}
				
				userInput = commands.get(prevCmdIdx).text;
				key.remove(0);
				break;
			}
			else if(keyCode == Console.up && commands.size() > 0)
			{	

				for(int j = prevCmdIdx - 1; j >= 0; j--)
				{
					if(commands.get(j).color == uText)
					{
						prevCmdIdx = j;
						userInput = commands.get(prevCmdIdx).text;
						
						if(prevCmdIdx <= 0)
						{
							prevCmdIdx = prevCmdSize - 1;
						}
						
						key.remove(0);
						return;
					}						
				}
				
				prevCmdIdx = prevCmdSize - 1;
				key.remove(0);
				break;
			}
			else if(keyCode == Console.down && commands.size() > 0)
			{
				for(int j = prevCmdIdx + 1; j <= commands.size() - 1; j++)
				{
					if(commands.get(j).color == uText)
					{
						prevCmdIdx = j;
						userInput = commands.get(prevCmdIdx).text;
						key.remove(0);
						return;
					}
				}
				
				prevCmdIdx = 0;
				userInput = commands.get(prevCmdIdx).text;
				key.remove(0);
				break;
			}
			else if(keyCode == Console.down || keyCode == Console.up)
			{
				key.remove(0);
				break;
			}
			
			c = (char) keyCode;
			
			
			if(!caps)
			{
				c = Character.toLowerCase(c);
			}
			
			if(keyCode == Console.backspace)
			{
				if(userInput.length() > 0)
				{	
					userInput = userInput.substring(0, userInput.length() - 1);
				}
				continue;
			}
			
			userInput += Character.toString(c);
		}
		
		key.clear();
		
		entryScroll = (commands.size() * 20) + 40 > y;
		
		if(entryScroll)
		{
			updateScrollbar();
		}
		else
		{
			topEntry = 0;
			bottomEntry = commands.size();
		}
		
		//blinking cursor
		if(blink)
		{
			cursorOpacity += 10;
			
			if(cursorOpacity >= 250)
			{
				blink = false;
			}
		}
		else
		{
			cursorOpacity -= 10;
			
			if(cursorOpacity <= 5)
			{
				blink = true;
			}
		}
	}
	
	public void submitInput()
	{
		if(userInput.toLowerCase().equals("clear"))
		{
			commands.clear();
			commands.add(new CommandPrompt(uText, userInput));
			userInput = "";
			return;
		}
		
		commands.add(new CommandPrompt(uText, userInput));
		
		switch(userInput.replaceAll(" ", "").toLowerCase())
		{
		case "preload":
			addLine("Preloading random Commands...");
			preloadCmds();
			break;
		case "debug":
			enterPassword = true;
			addLine("Enter the password.");
			break;
		case "state":
			returnState();
			break;
		case "mypos":
		case "myposition":
			addLine("Current position: (" + (gp.Player.worldX/gp.tileSize) + ", " + (gp.Player.worldY/gp.tileSize) + ")");
			break;
			
		case "exit":
		case "quit":
			System.exit(0);
			break;
		case "hi":
		case "hello":
		case "howdy":
		case "hi-ya":
		case "hey":
			addLine(userInput + " there!");
			break;
		case "greetings!":
		case "greetings":
			addLine("Greetings!");
			break;
		case "69":
			addLine("nice");
			break;
		case "nice":
			addLine("69");
			break;
		case "help":
			addCommandList();
			break;
		case "combat":
			addLine("Combat triggered");
			gp.addMouseWheelListener(gp.UI);
			gp.addMouseListener(gp.UI);
			gp.addKeyListener(gp.keyH);
			gp.removeKeyListener(this);
			gp.removeMouseListener(this);
			gp.removeMouseWheelListener(this);
			gp.console = false;
			gp.gameState = gp.combatTransitionState;
			break;
		case "combatend":
		case "combatover":
			addLine("Ending combat");
			if(gp.prevState == gp.combatState)
			{
				gp.addMouseWheelListener(gp.UI);
				gp.addMouseListener(gp.UI);
				gp.addKeyListener(gp.keyH);
				gp.removeKeyListener(this);
				gp.removeMouseListener(this);
				gp.removeMouseWheelListener(this);
				gp.console = false;
				gp.prevState = gp.combatOverState;
				gp.gameState = gp.combatOverState;
				gp.combat.finish();
			}
			break;
			
			
		case "collisionoff":
			addLine("Collision turned off, have fun no clipping!");
			gp.keyH.COLLISIONOFF = true;
			gp.collisionEnabled = true;
			break;
		case "collisionon":
			addLine("Collision turned on, try not to get stuck!");
			gp.keyH.COLLISIONOFF = true;
			gp.collisionEnabled = false;
			break;
		case "togglecollision":
			gp.keyH.COLLISIONOFF = true;
			if(gp.collisionEnabled)
			{
				addLine("Collision turned off, have fun no clipping!");
			}
			else
			{
				addLine("Collision turned on, try not to get stuck!");
			}
			break;
			
		case "coordinateson":
			addLine("Coordinates are now displayed.");
			gp.keyH.COORDINATE = true;
			break;
		case "coordinatesoff":
			addLine("Coordinates are not displayed.");
			gp.keyH.COORDINATE = false;
			break;
		case "togglecoordinates":
			gp.keyH.COORDINATE = !gp.keyH.COORDINATE;
			if(gp.keyH.COORDINATE)
			{
				addLine("Coordinates are now displayed.");
			}
			else
			{
				addLine("Coordinates are not displayed.");
			}
			break;
			
		default:
			if(userInput.length() >= 7)
			{
				if(userInput.substring(0, 6).toLowerCase().equals("spawn("))
				{
					spawn sp = super.checkEntity(userInput, gp.maxWorldCol, gp.maxWorldRow);
					if(sp.valid)
					{
						for(int i = 0; i < gp.NPC.length; i++)
						{
							if(gp.NPC[i] == null)
							{
								GenerateEntity.createEntity(gp.NPC, sp.name, i);
								if(gp.NPC[i] != null)
								{
									gp.NPC[i].worldX = sp.x * gp.tileSize;
									gp.NPC[i].worldY = sp.y * gp.tileSize;
									addLine(sp.name + " spawned at: (" + sp.x + ", " + sp.y +")");
									userInput = "";
									return;
								}
								break;
							}
						}
						
						sp.cause = "No available resources to create entity.";
						
						addLine(cInvalid, "Entity not created. Reason: " + sp.cause);
						break;
					}
					else
					{
						addLine(cInvalid, "Entity not created. Reason: " + sp.cause);
						break;
					}
				}
			}
			
			if(userInput.length() > 4)
			{
				if(userInput.substring(0, 4).toLowerCase().equals("gold"))
				{
					addGold();
					break;
				}
			}
			
			if(enterPassword)
			{
				try
				{					
					if(!Encrypter.checkPassword(userInput))
					{
						addLine(cInvalid, "Invalid password. Enter \"debug\" to try again.");
						break;
					}
					else
					{
						gp.DEBUG = true;
						addLine("You now have debug access.");
						break;
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					System.exit(0);
				}
			}
			
			addLine(cInvalid, "This is not a valid command. Type \"help\" for a list.");
			break;
		}
		
		userInput = "";
	}
	
	public void addCommandList()
	{
		addLine(hText, "debug");
		addLine(hText, "My pos/My position");
		addLine(hText, "Quit/Exit");
		addLine(hText, "Clear");
		addLine(hText, "Combat [start/over]");
		addLine(hText, "Collision [on/off]");
		addLine(hText, "Toggle collision");
		addLine(hText, "Coordinates [on/off]");
		addLine(hText, "Toggle coordinates");
		addLine(hText, "Gold #");
		addLine(hText, "Add_item #[ID] - NOT YET IMPLEMENTED");
		addLine(hText, "Spawn(\"[NPC name]\", [x], [y]);");
		addLine(hText, "Enemy(\"[Enemy name]\", [x], [y]); - NOT YET IMPLEMENTED");
	}
	
	public void addGold()
	{
		String num = userInput.replaceAll(" ", "");
		num = num.substring(4, num.length());
		
		int goldNum;
		
		try
		{
			goldNum = Integer.parseInt(num);
		}
		catch(Exception e)
		{
			addLine(cInvalid, " You have entered a value that is not valid. Shame on you!");
			
			userInput = "";
			
			return;
		}
		
		if(gp.Player.gold + goldNum > Integer.MAX_VALUE)
		{
			addLine("You received some gold... But now it's negative... Whoops!");
		}
		
		gp.Player.gold += goldNum;
		
		addLine("You received " + goldNum + " gold.");
	}

	public void returnState()
	{
		if(gp.prevState == gp.playState)
		{
			addLine("play state");
		}
		else if(gp.prevState == gp.titleState)
		{
			addLine("title state");
		}
	}

	public void updateScrollbar()
	{
		if(size != commands.size())
		{
			size = commands.size();
			trueY = y - 20;
			//Max entries on the window at a time - Currently 18
			maxEntries = trueY/20;
			//Get current amount of entries
			currEntries = size;
			//1 page is equal to 18 entires. Find how many pages we have.
			scrollPage = (currEntries/maxEntries) + 1;
			//Get scroll bar size relative to how many pages exist.
			scrollbarSize = trueY/scrollPage;
			
			//Since we are adding new commands, reset scroll position to the bottom
			scrollPos = trueY - scrollbarSize;
			precisePos = trueY - scrollbarSize;
			//Reset bottom entry to true bottom
			bottomEntry = currEntries;
		}
		
		if(bottomEntry + scrollUnits < maxEntries)
		{
			bottomEntry = maxEntries;
		}
		else if(bottomEntry + scrollUnits > currEntries)
		{
			bottomEntry = currEntries;
		}
		else
		{
			bottomEntry += scrollUnits;
		}
		
		topEntry = bottomEntry - maxEntries;
		
		//Scroll bar stuff, yay!
		double preciseSegment = ((double) trueY - scrollbarSize)/((double) currEntries - maxEntries);
		double preciseScroll = ((double) scrollUnits) * preciseSegment;
		
		if((precisePos + preciseScroll) > trueY - scrollbarSize)
		{
			precisePos = trueY - scrollbarSize;
		}
		else if(precisePos + preciseScroll < 0)
		{
			precisePos = 0;
		}
		else
		{
			precisePos += preciseScroll;
		}
		
		scrollUnits = 0;
		
		//Check if mouse held to move rectangle
		if(leftClick && moving)
		{
			precisePos = (double) mouseArea.y + preciseOffset;
			
			if(precisePos < 0)
			{
				precisePos = 0;
			}
			else if(precisePos > trueY - scrollbarSize)
			{
				precisePos = (double) trueY - scrollbarSize;
			}
			
			//Update selection and whatnot
			double pos = precisePos/preciseSegment;
			
			bottomEntry = (int) pos + maxEntries;
			topEntry = (int) pos;
		}
		else if(leftClick && mouseArea.intersects(scrollBar))
		{
			moving = true;
			if(!leftClickSet)
			{
				leftClickSet = true;
				preciseOffset = precisePos - (double) mouseArea.y;
				System.out.println("Setting offset");
			}
		}
		else if(!leftClick)
		{
			leftClickSet = false;
			moving = false;
		}
		
		scrollPos = (int) precisePos;
	}

	int prevUISize;
	public boolean getShiftMask(int keyCode)
	{
		prevUISize = userInput.length();
		switch(keyCode)
		{
		case 0x30:
			userInput += ")";
			break;
		case 0x31:
			userInput += "!";
			break;
		case 0x32:
			userInput += "@";
			break;
		case 0x33:
			userInput += "#";
			break;
		case 0x34:
			userInput += "$";
			break;
		case 0x35:
			userInput += "%";
			break;
		case 0x36:
			userInput += "^";
			break;
		case 0x37:
			userInput += "&";
			break;
		case 0x38:
			userInput += "*";
			break;
		case 0x39:
			userInput += "(";
			break;
		case 0x5B:
			userInput += "{";
			break;
		case 0x5D:
			userInput += "}";
			break;
		case 0x2E:
			userInput += ">";
			break;
		case 0x2C:
			userInput += "<";
			break;
		case 0x5C:
			userInput += "|";
			break;
		case 0x2F:
			userInput += "?";
			break;
		case 0x2D:
			userInput += "_";
			break;
		case 0x3D:
			userInput += "+";
			break;
		case 0xDE:
			userInput += "\"";
			break;
		}
		
		if(userInput.length() != prevUISize)
		{
			key.remove(0);
			key.remove(0);
			return true;
		}
		
		return false;
	}
	
	public void preloadCmds()
	{
		String[] str = null;
		str = randomString(str);
		
		for(int i = 0; i < str.length; i++)
		{
			userInput = str[i];
			submitInput();
		}
	}
	
	public String[] randomString(String[] str)
	{
		str = new String[ThreadLocalRandom.current().nextInt(10, 21)];
		
		for(int i = 0; i < str.length; i++)
		{
			String random = "";
			for(int j = 0; j < ThreadLocalRandom.current().nextInt(1, 11); j++)
			{
				int integer = ThreadLocalRandom.current().nextInt(0x21, 0x7E + 1);
				char character = (char) integer;
				
				random += character;
			}
			str[i] = random;
		}
		
		return str;
	}
}
