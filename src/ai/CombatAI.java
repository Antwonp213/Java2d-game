package ai;

import combat.CombatEntity;

public class CombatAI 
{
	//VARAIBLES
	public boolean healer = false;
	public boolean melee = false;
	public boolean range = false;
	
	public int target = 0;
	public final int PLAYER = 0; public final int COMP1 = 1; public final int COMP2 = 2; public final int COMP3 = 3;
	
	public String actionName;
	
	CombatEntity Player;
	CombatEntity[] Playable;
	
	public CombatAI(CombatEntity Player, CombatEntity[] Playable)
	{
		this.Player = Player;
		this.Playable = Playable;
	}
	
	public void calculateAction(int id)
	{
		if(id == 1)
		{
			gruntAction();
		}
		
		switch(id)
		{
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		}
	}
	
	public void gruntAction()
	{
		
	}
}
