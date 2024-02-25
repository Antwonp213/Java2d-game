package combat.combatPlayable;

import java.awt.image.BufferedImage;

import combat.CombatEntity;
import combat.EntityMethods;

public class KonradTheWizard extends CombatEntity implements EntityMethods
{
	public KonradTheWizard()
	{
		//Important identification information
		name = "Konrad"; //Subject to change in the future.
		Class = "Wizard";
		playableID = 2;
		inBattle = true;
		
		imageScale = 2;
		attackOffsetX = (int) (10 * imageScale);
		attackOffsetY = (int) (9 * imageScale);
		offsetX = (int) (-4 * imageScale);
		offsetY = (int) (8 * imageScale);
		
		initArrays();
		setStats();
		
		Folder = "Konrad";
		subFolder = "Idle";
		imageID = 102255212;
		
		getImages();
		
		icon = setIcon(Folder);
	}
	
	public void setStats()
	{
		mag = 5; //Magic, determines magic attack power and how much MP you have
		str = 15; //Influences atk stat
		con = 5; //Influences HP
		agi = 8; //Miss-hit chance
		spd = 30; //Initiative stat
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
		idleFrames = new BufferedImage[33];
		damageFrames = new BufferedImage[2];
		
		attackFrames = new BufferedImage[41];
		castFrames = new BufferedImage[31];
		skill1Frames = new BufferedImage[68];
		skill2Frames = new BufferedImage[125];
		skill3Frames = null;
		skill3bFrames = null;
		skill3LibFrames = null;
		
		dashbackFrames = new BufferedImage[19];
		dashFrames = new BufferedImage[23];
	}
}
