package combat.combatPlayable;

import java.awt.image.BufferedImage;

import combat.CombatEntity;

public class CombatPlayer extends CombatEntity
{
	public final String Class;
	public byte weaponType; //Can wield 4 different weapon types: Sword, Sword & Shield, Spear, & Bow.
	public int x = 0;
	public int y = 0;
	//Initial starting weapon will just be the sword.
	
	
	public CombatPlayer()
	{
		//Important identification information
		name = "Player"; //Subject to change in the future.
		Class = "Champion";
		playableID = 1;
		inBattle = true;
		
		//Animation stuff
		Folder = "TempPlayer";
		subFolder = "Idle";
		imageID = 102766512;
		
		imageScale = 2;
		weaponType = 0;
		attackOffsetX = (int) (10 * imageScale);
		attackOffsetY = (int) (9 * imageScale);
		
		spriteX = 940;
		spriteY = 350;
		
		initArrays();
		
		//When implementing a save/load system, determine from boolean if we should set it.
		setStats(); //Called once in the temporary intro, called again after creating character
		
		getImages();
		
		icon = setIcon(Folder);
	}
	
	public void setStats()
	{
		mag = 5; //Magic, determines magic attack power and how much MP you have
		str = 15; //Influences atk stat
		con = 5; //Influences HP
		agi = 8; //Miss-hit chance
		spd = 50; //Initiative stat
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
		idleFrames = new BufferedImage[41];
		damageFrames = new BufferedImage[2];
		
		attackFrames = new BufferedImage[41];
		castFrames = new BufferedImage[31];
		skill1Frames = new BufferedImage[51];
		skill2Frames = new BufferedImage[91];
		skill3Frames = new BufferedImage[106];
		skill3bFrames = null;
		skill3LibFrames = null;
		
		dashbackFrames = new BufferedImage[18];
		dashFrames = new BufferedImage[28];
	}
}
