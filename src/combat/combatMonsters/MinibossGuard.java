package combat.combatMonsters;

import java.awt.image.BufferedImage;

import combat.CombatEntity;

public class MinibossGuard extends CombatEntity
{
	public MinibossGuard()
	{
		//Important identification information
		name = "Sir Heinrich";
		
		imageScale = 2.5;
		attackOffsetX = (int) (10 * imageScale);
		attackOffsetY = (int) (9 * imageScale);
		offsetX = (int) (-4 * imageScale);
		offsetY = (int) (-6 * imageScale);
		
		initArrays();
		setStats();
		
		Folder = "MinibossGuard";
		subFolder = "Idle";
		imageID = 999999999;
		
		getImages();
		
		icon = setIcon(Folder);
	}
	
	public void setStats()
	{
		mag = 5; //Magic, determines magic attack power and how much MP you have
		str = 15; //Influences atk stat
		con = 5; //Influences HP
		agi = 8; //Miss-hit chance
		spd = 100; //Initiative stat
		def = 15; //How much damage is mitigated from attacks
		wpn = 10; //TODO: Integrate with player inventory's weapon
		arm = 10; //TODO: Integrate with player inventory's armor
		
		maxHP = 100 * (int) (con/10 + 1);
		maxMP = 50 * (int) (mag/10 + 1);
		hp = maxHP;
		mp = maxMP;
		
		atk = (10 + (int) lvl) * (int) (str/10 + 1) + wpn;
	}
	
	public void initArrays()
	{
		idleFrames = new BufferedImage[26];
		damageFrames = new BufferedImage[2];
		
		attackFrames = new BufferedImage[26];
		castFrames = new BufferedImage[26];
		skill1Frames = new BufferedImage[101];
		skill2Frames = new BufferedImage[111];
		skill3Frames = null;
		skill3bFrames = null;
		skill3LibFrames = null;
		
		dashbackFrames = new BufferedImage[18];
		dashFrames = new BufferedImage[2];
	}
}
