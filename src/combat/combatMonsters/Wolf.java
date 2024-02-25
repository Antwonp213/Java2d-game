package combat.combatMonsters;

import java.awt.image.BufferedImage;

import combat.CombatEntity;

//Revised Wolf class

public class Wolf extends CombatEntity
{
	//Color Variation is a variable that determines the wolf color. This is to avoid over-crowding the class area, and to make stats uniform.
	int colorVariant;
	public Wolf(int colorVariation)
	{
		monsterID = 4;
		if(colorVariation == 0)
		{
			name = "Brown Wolf";
			subFolder = "BrownWolf";
			icon = setIcon(subFolder);
		}
		else if(colorVariation == 1)
		{
			name = "Gray Wolf";
			subFolder = "GrayWolf";
			icon = setIcon(subFolder);
		}
		else
		{
			name = "White Wolf";
			subFolder = "WhiteWolf";
			icon = setIcon(subFolder);
		}
		
		idleFrames = new BufferedImage[1];
		
		colorVariant = colorVariation;
		imageScale = 3;
		offsetY = (int) (-10 * imageScale);
		getImages();
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
