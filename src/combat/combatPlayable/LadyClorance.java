package combat.combatPlayable;

import java.awt.image.BufferedImage;

import combat.CombatEntity;
import combat.EntityMethods;

public class LadyClorance extends CombatEntity implements EntityMethods
{
	public LadyClorance()
	{
		//Important identification information
		name = "Clorance"; //Subject to change in the future.
		Class = "Archer";
		playableID = 4;
		inBattle = true;
		
		imageScale = 2;
		attackOffsetX = (int) (10 * imageScale);
		attackOffsetY = (int) (9 * imageScale);
		
		offsetX = (int) (-7 * imageScale);
		offsetY = (int) (0 * imageScale);
		
		initArrays();
		setStats();
		
		Folder = "Clorance";
		subFolder = "Idle";
		imageID = 107206312;
		
		getImages();
		
		icon = setIcon(Folder);
	}
	
	public void setStats()
	{
		mag = 5; //Magic, determines magic attack power and how much MP you have
		str = 15; //Influences atk stat
		con = 5; //Influences HP
		agi = 8; //Miss-hit chance
		spd = 75; //Initiative stat
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
		//Sorted alphabetically
		attackFrames = new BufferedImage[44];
		dashbackFrames = new BufferedImage[25];
		castFrames = new BufferedImage[33];
		damageFrames = new BufferedImage[2];
		dashFrames = new BufferedImage[42];
		idleFrames = new BufferedImage[31];
		skill1Frames = new BufferedImage[81];
		skill2Frames = new BufferedImage[113];
		skill3Frames = new BufferedImage[157];
		skill3bFrames = null;
		skill3LibFrames = null;
	}
}
