package debug;

import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import main.GameWindow;

public class Console implements KeyListener, MouseWheelListener, MouseListener
{
	//VK TO NORMAL
	//Letters
	protected static final int a = KeyEvent.VK_A; protected static final int b = KeyEvent.VK_B; protected static final int c = KeyEvent.VK_C; protected static final int d = KeyEvent.VK_D; protected static final int e = KeyEvent.VK_E; 
	protected static final int f = KeyEvent.VK_F; protected static final int g = KeyEvent.VK_G; protected static final int h = KeyEvent.VK_H; protected static final int i = KeyEvent.VK_I; protected static final int j = KeyEvent.VK_J; 
	protected static final int k = KeyEvent.VK_K; protected static final int l = KeyEvent.VK_L; protected static final int m = KeyEvent.VK_M; protected static final int n = KeyEvent.VK_N; protected static final int o = KeyEvent.VK_O; 
	protected static final int p = KeyEvent.VK_P; protected static final int q = KeyEvent.VK_Q; protected static final int r = KeyEvent.VK_R; protected static final int s = KeyEvent.VK_S; protected static final int t = KeyEvent.VK_T; 
	protected static final int u = KeyEvent.VK_U; protected static final int v = KeyEvent.VK_V; protected static final int w = KeyEvent.VK_W; protected static final int x = KeyEvent.VK_X; protected static final int y = KeyEvent.VK_Y; 
	protected static final int z = KeyEvent.VK_Z;
	//Numbers
	protected static final int zero = KeyEvent.VK_0; protected static final int one = KeyEvent.VK_1; protected static final int two = KeyEvent.VK_2; protected static final int three = KeyEvent.VK_3; protected static final int four = KeyEvent.VK_4; 
	protected static final int five = KeyEvent.VK_5; protected static final int six = KeyEvent.VK_6; protected static final int seven = KeyEvent.VK_7; protected static final int eight = KeyEvent.VK_8; protected static final int nine = KeyEvent.VK_9;
	//Shift masked numbers
	protected static final int exclamation = KeyEvent.VK_EXCLAMATION_MARK; protected static final int at = KeyEvent.VK_AT; protected static final int numberSign = KeyEvent.VK_NUMBER_SIGN; protected static final int dollar = KeyEvent.VK_DOLLAR;
	protected static final int percent = 0x00; protected static final int circumflex = KeyEvent.VK_CIRCUMFLEX;
	//Arrow keys
	protected static final int up = KeyEvent.VK_UP; protected static final int down = KeyEvent.VK_DOWN; protected static final int left = KeyEvent.VK_LEFT; protected static final int right = KeyEvent.VK_RIGHT; 
	//Special Keys
	protected static final int shift = KeyEvent.VK_SHIFT; protected static final int tab = KeyEvent.VK_TAB; protected static final int alt = KeyEvent.VK_ALT; protected static final int esc = KeyEvent.VK_ESCAPE;
	protected static final int backspace = KeyEvent.VK_BACK_SPACE; protected final static int tilde = KeyEvent.VK_BACK_QUOTE; protected final static int enter = KeyEvent.VK_ENTER;
	protected static final int shiftMask = KeyEvent.SHIFT_DOWN_MASK; protected static final int capslock = KeyEvent.VK_CAPS_LOCK; protected static final int ctrlMask = KeyEvent.CTRL_DOWN_MASK;
	
	ArrayList<Integer> key = new ArrayList<Integer>();
	
	GameWindow gp;
	public Console(GameWindow gp)
	{
		this.gp = gp;
	}

	@Override
	public void keyTyped(KeyEvent e) 
	{
		
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{	
		if(e.getKeyCode() != shift && e.getKeyCode() != capslock)
		{
			key.add(e.getKeyCode());
		}
		
		if(e.getKeyCode() == capslock)
		{
			ConsoleOverlay.caps = !ConsoleOverlay.caps;
		}
		
		if(e.getModifiersEx() == shiftMask || e.getModifiersEx() == ctrlMask)
		{
			key.add(e.getModifiersEx());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{

	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	Rectangle mouseArea = new Rectangle(0, 0, 8, 8);
	boolean leftClick = false;
	boolean rightClick = false;
	int scrollUnits = 0;
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(SwingUtilities.isLeftMouseButton(e))
		{
			leftClick = false;
		}
		
		if(SwingUtilities.isRightMouseButton(e))
		{
			rightClick = false;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{		
		if(SwingUtilities.isLeftMouseButton(e))
		{
			leftClick = true;
		}
		
		if(SwingUtilities.isRightMouseButton(e))
		{
			rightClick = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if(SwingUtilities.isLeftMouseButton(e))
		{
			leftClick = false;
		}
		
		if(SwingUtilities.isRightMouseButton(e))
		{
			rightClick = false;
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
		scrollUnits += e.getWheelRotation();
	}
	
	public void updateMouse()
	{
		if(gp.fullScreen)
		{
			mouseArea.x = (int) ((double) MouseInfo.getPointerInfo().getLocation().x * ((double) gp.screenWidth/gp.nativeScreenWidth)) - mouseArea.width/2;
			mouseArea.y = (int) ((double) MouseInfo.getPointerInfo().getLocation().y * ((double) gp.screenHeight/gp.nativeScreenHeight));
		}
		else
		{
			mouseArea.x = (int) (MouseInfo.getPointerInfo().getLocation().x - gp.getLocationOnScreen().x) - mouseArea.width/2;
			mouseArea.y = (int) (MouseInfo.getPointerInfo().getLocation().y - gp.getLocationOnScreen().y);	
		}
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void update()
	{
		updateMouse();
	}
	
	public spawn checkEntity(String str, int xLimit, int yLimit)
	{
		spawn sp = new spawn();
		
		try
		{
			int nameStart = 0; int nameEnd = 0;
						
			String entityName = str.substring(6, str.length());;
			
			char q = entityName.charAt(0);
			if(q != '"')
			{
				throw new Exception("Entity name does not start with quote");
			}
			
			nameStart = 1;
						
			for(int i = nameStart; i < entityName.length(); i++)
			{
				char c = entityName.charAt(i);
				if(c == '"')
				{
					nameEnd = i;
					break;
				}
				
				if(i == entityName.length() - 1 && c != '"')
				{
					throw new Exception("Entity name not ended by quote.");
				}
			}
			
			q = entityName.charAt(nameEnd + 1);
			if(q != ',')
			{
				throw new Exception("Name not separated by comma");
			}
			
			entityName = entityName.substring(nameStart, nameEnd);
			
			//Check if entity exists
			if(!checkEntityName(entityName))
			{
				throw new Exception("Entity does not exist");
			}
			
			//Get x position
			
			String xPos = str.substring(nameEnd + 7, str.length());
						
			int xPosEnd = 0;
			
			for(int i = 1; i < xPos.length(); i++)
			{
				char c = xPos.charAt(i);
				if(c == ',')
				{
					xPosEnd = i;
				}
				else if(i >= xPos.length() && c != ',')
				{
					throw new Exception("Value X not ended with comma");
				}
			}
			
			xPos = xPos.substring(1, xPosEnd);
			xPos = xPos.replaceAll(" ", "");
			int x = Integer.parseInt(xPos);
			
			String yPos = str.substring(nameEnd + xPosEnd + 7, str.length());
			int yPosEnd = 0;
			
			for(int i = 1; i < yPos.length(); i++)
			{
				char c = yPos.charAt(i);
				if(c == ')')
				{
					yPosEnd = i;
					break;
				}
				else if(i >= yPos.length() - 1 && c != ')')
				{
					throw new Exception("Command does not end correctly.");
				}
			}
			
			char c = yPos.charAt(yPos.length() - 1);
			
			if(c != ';')
			{
				throw new Exception("Command not delimited. Put a semi-colon.");
			}
			
			yPos = yPos.substring(1, yPosEnd);
			yPos = yPos.replaceAll(" ", "");
			int y = Integer.parseInt(yPos);
			
			if((x < 0 && y < 0) || (x > xLimit && y > yLimit) || (x < 0 && y > yLimit) || (x > xLimit && y < 0))
			{
				System.out.println(x + ", " + y);
				
				System.out.println("Zero test, X: " + (x < 0) + ", Y: " + (y < 0));
				System.out.println("Out of map test, X: " + (x > xLimit) + ", Y: " + (y > yLimit));
				
				throw new Exception("x & y out of bounds.");
			}
			else if(y < 0 || y > yLimit)
			{
				throw new Exception("y out of bounds.");
			}
			else if(x < 0 || x > xLimit)
			{
				throw new Exception("x out of bounds.");
			}
			
			sp.x = x;
			sp.y = y;
			sp.name = entityName;
			sp.valid = true;
			
			return sp;
		}
		catch(Exception e)
		{
			System.out.println(e.getCause());
			
			sp.valid = false; sp.x = 0; sp.y = 0; sp.name = "";
			
			sp.cause = e.getMessage();
			
			return sp;
		}
	}
	
	public boolean checkEntityName(String name)
	{
		switch(name)
		{
		case entities.NPCs.Merchant.npcName:
			return true;
		case entities.NPCs.OldMan.npcName:
			return true;
		case entities.NPCs.MinibossGuard.npcName:
			return true;
		case entities.NPCs.PathFinderSlave.npcName:
			return true;
		default:
			return false;
		}
	}
	
	class spawn
	{
		boolean valid;
		int x; int y;
		String name;
		String cause = "";
	}
}
