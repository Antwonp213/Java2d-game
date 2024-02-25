package combat.combatMonsters;

import java.awt.image.BufferedImage;

import combat.CombatEntity;

public class BanditSkirmisher extends CombatEntity
{
	public BanditSkirmisher()
	{
		monsterID = 3;
		name = "Bandit Skirmisher";
		subFolder = "BanditSkirmisher";
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
}