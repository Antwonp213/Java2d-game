package data;

import main.GameWindow;

public class PlayerInventory 
{
	GameWindow gp;
	
	public int gold = 0;
	
	
	
	public PlayerInventory(GameWindow gp)
	{
		this.gp = gp;
	}
	
	public class item
	{
		String name;
		int value;
		boolean stackable = false;
		public item()
		{
			
		}
	}
}
