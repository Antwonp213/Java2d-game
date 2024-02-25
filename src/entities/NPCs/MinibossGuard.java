package entities.NPCs;

import entities.Entity;
import entities.EntityInterface;
import entities.Player;
import main.GameWindow;

public class MinibossGuard extends Entity implements EntityInterface	
{
	GameWindow gp;
	Player Player;
	
	public static final String npcName = "Miniboss Guard";
	
	public MinibossGuard(GameWindow gp) 
	{
		super(gp);
		
		this. gp = gp;
		this.Player = gp.Player;
		
		dialogue.add("You. Shall. Not. Pass.");
		dialogue.add("Then die.");
		
		speed = 6; 
		spriteNum = 1;
		frameCount = 2;
		
		detector = true;
		
		name = npcName;
		
		Folder = "/NPCs/Guard/";
		
		getImages(Folder);
		
		setSolidArea();
	}

	@Override
	public String getDialogue() 
	{
		return null;
	}

	private boolean warning = true;
	private boolean chase = false;
	private boolean battleCheck = false;
	public static boolean dead = false;
	@Override
	public void setAction() 
	{
		if(warning)
		{
			if(checkDetection(Player, 4))
			{
				System.out.println(dialogue.get(0));
				dialogue.remove(0);
				warning = false;
			}
		}
		else if(!chase && !warning && !battleCheck)
		{
			if(checkDetection(Player, 2))
			{
				System.out.println(dialogue.get(0));
				dialogue.remove(0);
				chase = true;
				battleCheck = true;

				gp.oo.addMessage("Run away!");
			}
		}
		
		if(chase)
		{
			int goalCol = (Player.worldX + Player.solidArea.x)/80;
			int goalRow = (Player.worldY + Player.solidArea.y)/80;
			searchPath(goalCol, goalRow);
			
			if(Player.checkRadius(this, 1))
			{
				chase = false;
			}
		}
		else
		{
			if(battleCheck)
			{
				gp.triggerCombat(GameWindow.MinibossGuard);
			}
			else
			{
				direction = "idleD";
			}
		}
	}

}
