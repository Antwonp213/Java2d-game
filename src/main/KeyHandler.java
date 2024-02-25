package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class KeyHandler implements KeyListener
{	
	GameWindow gp;
	
	public KeyHandler(GameWindow gp)
	{
		this.gp = gp;
	}
	
	//VK TO NORMAL
	//Letters
	public final int a = KeyEvent.VK_A; public final int b = KeyEvent.VK_B; public final int c = KeyEvent.VK_C; public final int d = KeyEvent.VK_D; public final int e = KeyEvent.VK_E; 
	public final int f = KeyEvent.VK_F; public final int g = KeyEvent.VK_G; public final int h = KeyEvent.VK_H; public final int i = KeyEvent.VK_I; public final int j = KeyEvent.VK_J; 
	public final int k = KeyEvent.VK_K; public final int l = KeyEvent.VK_L; public final int m = KeyEvent.VK_M; public final int n = KeyEvent.VK_N; public final int o = KeyEvent.VK_O; 
	public final int p = KeyEvent.VK_P; public final int q = KeyEvent.VK_Q; public final int r = KeyEvent.VK_R; public final int s = KeyEvent.VK_S; public final int t = KeyEvent.VK_T; 
	public final int u = KeyEvent.VK_U; public final int v = KeyEvent.VK_V; public final int w = KeyEvent.VK_W; public final int x = KeyEvent.VK_X; public final int y = KeyEvent.VK_Y; 
	public final int z = KeyEvent.VK_Z;
	//Numbers
	private final int zero = KeyEvent.VK_0; private final int one = KeyEvent.VK_1; private final int two = KeyEvent.VK_2; private final int three = KeyEvent.VK_3; private final int four = KeyEvent.VK_4; 
	private final int five = KeyEvent.VK_5; private final int six = KeyEvent.VK_6; private final int seven = KeyEvent.VK_7; private final int eight = KeyEvent.VK_8; private final int nine = KeyEvent.VK_9; 
	//Arrow keys
	public static final int up = KeyEvent.VK_UP; public static final int down = KeyEvent.VK_DOWN; public static final int left = KeyEvent.VK_LEFT; public static final int right = KeyEvent.VK_RIGHT; 
	//Special Keys
	private final int shift = KeyEvent.VK_SHIFT; private final int tab = KeyEvent.VK_TAB; private final int alt = KeyEvent.VK_ALT; public final int esc = KeyEvent.VK_ESCAPE;
	private final int backspace = KeyEvent.VK_BACK_SPACE; private final int tilde = KeyEvent.VK_BACK_QUOTE;
	
	public boolean RESTART = false;
	public byte konamiCode; //If you know the Konami code, you'll enter debug mode.
	public boolean UP, DOWN, LEFT, RIGHT, SHIFT, ENTER, TAB, FULLSCREEN, MINIMAP, drawFPS;
	//DEBUG bools
	public boolean CONSOLE = false;
	public boolean SKIP, SHOWCOLLISION, SETNPCPATH, SHOWPATH, COORDINATE, BACKSPACE;
	public boolean COMBATENABLED = false;
	public boolean COLLISIONOFF = false;
	public boolean MENU = false; //Brings up a menu when you toggle it
	public HashSet<Integer> key = new HashSet<Integer>();

	@Override
	public void keyTyped(KeyEvent e) 
	{
		
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		key.add(e.getKeyCode());
		
		if(!key.isEmpty())
		{
			setKeyTrue(e.getKeyCode());			
			//Key combinations
			if(key.contains(alt) && key.contains(x))
			{
				//TODO: When pressed, show prompt asking if this is what you want.
				System.exit(0);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		if(key.contains(tilde) && key.contains(shift))
		{
			CONSOLE = !CONSOLE;
		}
		
		
		if(setKeyFalse(e.getKeyCode()))
		{
			toggleKey(e.getKeyCode());
			key.remove(e.getKeyCode());
		}
	}
	
	public boolean setKeyTrue(int keyCode)
	{
		switch(keyCode)
		{
		//Player movement
		case w:
			UP = true;
			break;
		case s:
			DOWN = true;
			break;
		case a:
			LEFT = true;
			break;
		case d:
			RIGHT = true;
			break;
		case shift:
			SHIFT = true;
			break;
		}
		
		return true;
	}
	
	public boolean setKeyFalse(int keyCode)
	{
		switch(keyCode)
		{
		case w:
			UP = false;
			break;
		case s:
			DOWN = false;
			break;
		case a:
			LEFT = false;
			break;
		case d:
			RIGHT = false;
			break;
		case shift:
			SHIFT = false;
			break;
		}
		return true;
	}
	
	public void toggleKey(int keyCode)
	{
		switch(keyCode)
		{
		case y:
			if(gp.gameState == gp.playState)
			{
				COORDINATE = !COORDINATE;
			}
			break;
		case o:
			if(gp.gameState == gp.playState)
			{
				COMBATENABLED = !COMBATENABLED;
			}
			else if(gp.gameState == gp.combatState)
			{
				SKIP = !SKIP;
			}
			break;
		case k:
			if(gp.gameState == gp.playState)
			{
				SHOWPATH = !SHOWPATH;
			}			
			break;
		case n:
			if(gp.gameState == gp.playState)
			{
				SETNPCPATH = !SETNPCPATH;
			}
			break;
		case tab:
			if(!MENU && !key.contains(alt) && (gp.gameState == gp.playState || gp.gameState == gp.menuState))
			{
				TAB = !TAB;
			}
			break;
		case f:
			FULLSCREEN = !FULLSCREEN;
			break;
		case u:
			if(gp.gameState == gp.playState)
			{
				COLLISIONOFF = !COLLISIONOFF;
			}
			break;
		case esc:
			if(!TAB && gp.gameState == gp.playState || gp.gameState == gp.pauseState)
			{
				gp.ps.menuState = gp.ps.SET_PAUSE_MENU;
				MENU = !MENU;
			}
			break;
		case p:
			if(gp.gameState == gp.playState)
			{
				gp.gameState = gp.combatTransitionState;
			}
			break;
		case backspace:
			if(gp.gameState == gp.pauseState)
			{
				BACKSPACE = true;
			}
		case g:
			drawFPS = !drawFPS;
			break;
		}
	}
}
