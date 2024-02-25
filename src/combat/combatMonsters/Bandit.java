package combat.combatMonsters;

import java.awt.image.BufferedImage;

import combat.CombatEntity;

public class Bandit extends CombatEntity
{
	public Bandit()
	{
		monsterID = 1;
		name = "Bandit";
		subFolder = "Bandit";
		imageScale = 5;
		
		idleFrames = new BufferedImage[1];
		
		getImages();
		
		icon = setIcon(subFolder);
	}
	
	public void setStats()
	{
		mag = 0 * lvl;
		str = 15 + (lvl * 2);
		spd = 20 + (lvl * 5);
		con = 5 + (lvl * 3);
		agi = 20 + (lvl * 5);
		def = 10 * (int) (lvl/10 + 1);
		wpn = 0;
		arm = 0;
		
		exp = (long) ((lvl * 50) * 1.25);
		
		maxHP = 50 + con;
		maxMP = 0 + mag;
		hp = maxHP;
		mp = maxMP;
		
		atk = (10 + lvl) * (int) (str/10 + 1) + wpn;
	}
	
	public void getImages()
	{
		idleFrames[0] = setup(subFolder + "/ready_1", imageScale);
	}
	
	public void getAction()
	{
		int decision = rand.nextInt(999); //0 to 999
		
		if(decision < 100)
		{
			
		}
		else if(decision >= 100 && decision <= 200)
		{
			
		}
		else if(decision >= 200 && decision <= 300)
		{
			
		}
		else if(decision >= 300 && decision <= 400)
		{
			
		}
		else if(decision >= 400 && decision <= 500)
		{
			
		}
		else //Plain ole attack
		{
			
		}
	}
}
