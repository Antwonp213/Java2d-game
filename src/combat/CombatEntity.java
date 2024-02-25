package combat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

import main.UtilityTool;

public class CombatEntity 
{
	//Being accessed in sub-packages, thus protected.
	protected UtilityTool uTool = new UtilityTool();
	protected Random rand = new Random();
	URL animURL;
	
	//PLAYABLE ONLY
	public String Class;
	public byte playableCount; //How many companions are set in the party.
	public byte playableID = 0; //1 and above are playable, 0 and below are Monsters
	public boolean inBattle = false;
	public byte playableMenuPos; //Based on position in the menu
	//SHARED VARIABLES
	public int monsterID = 0;
	public int spriteX, spriteY;
	public int x, y;
	public double imageScale;
	public int imageID;
	public int turnID;
	//STATS
	public int lvl, maxLVL;
	public int maxHP, hp, maxMP, mp, wpn, arm, atk, con, def, mag, agi, str, spd;
	public long exp, maxEXP;
	public int spdRoll; //Base is SPD, rolled during loading
	public int speedCounter = 0;
	public int rolledInit; //Initiative rolled based on speed.
	//How many transparent pixels away from what I think the origin is
	public int offsetX = 0;
	public int offsetY = 0; 
	//How many transparent pixels away from what I think the origin is during attack
	public int attackOffsetX;
	public int attackOffsetY;
	public int spriteNum; //What frame number the sprite is on
	public int spriteNumMax; //The maximum number of frames, determined by current animation state
	public int spriteCounterMax; //Number of screen frames, before it changes animation frames
	public int spriteCounter;
	public int currentAnimationState;
	public boolean MeleeRange; //Determine if the sprite must move to the enemy for an attack
	//SPRITE VARIABLES
	public String Folder;
	public String subFolder; //Name of the Sub-folder for the entity. For example, Knight would have /Knight/anim.png
	public String animName; //Name of the animation, typically something like "idle" or "skill1/2/3"
	public int animNum; //Integer that keeps track of animNum; Set manually
	
	public String name; //Name of the Sprite, used as a reference with displaying enemy actions
	public int intiative; //Innate attribute. The higher the intitiave the better the chance, duh.
	
	//FINAL POSITION VALUES FOR ENEMIES
	final int x1 = 600; final int x2 = 400; final int x3 = 200; final int row1 = 250; final int row2 = 475;
	public int pos; //Six possible positions
	
	//SPRITE FRAME ARRAYS (TEST)
	public BufferedImage idleFrames[];
	public BufferedImage damageFrames[];
	public BufferedImage attackFrames[];
	public BufferedImage castFrames[];
	public BufferedImage skill1Frames[];
	public BufferedImage skill2Frames[];
	public BufferedImage skill3Frames[];
	public BufferedImage skill3bFrames[];
	public BufferedImage skill3LibFrames[];
	public BufferedImage dashbackFrames[];
	public BufferedImage dashFrames[];
	
	//ICON for Turn Order Bar
	public BufferedImage icon;
	
	//AI ACTIONS
	protected byte aiAction;
	protected final byte attack = 0;
	protected final byte defend = 1;
	protected final byte atkBoost = 2;
	protected final byte defBoost = 3;
	protected final byte heal = 4;
	protected final byte healAlly = 5;
	
	
//	public CombatEntity() {}
	
	public void getAction() {} //Written in the class itself
	
	protected void setStats() {}
	
	protected void getImages()
	{
		long pastTime = System.nanoTime();
		
		subFolder = "Idle";
		animName = "idle";
		animNum = idleFrames.length;
		
		System.out.println("\nCalibrating " + Folder);
		
		for(int i = 0; i < 9; i++)
		{
			loadThread(i);
		}
		
		while(!checkThread(0) || !checkThread(1) || !checkThread(1) || !checkThread(3) || !checkThread(4) || !checkThread(5) || !checkThread(6) || !checkThread(7) || !checkThread(8));
		
		long currTime = System.nanoTime();
		System.out.println(name + " took: " + String.format("%.2f", (currTime - pastTime)/1000000000.00) + "s to complete its intitiation");
	}
	
	//Specialized CombatEntity setup function, as we already know the predefined folder path
	public BufferedImage setup(String imagePath, double scale)
	{	
		BufferedImage scaledImage = null;
		try
		{
			scaledImage = ImageIO.read(getClass().getResourceAsStream("/CombatSprites/" + imagePath + ".png"));
			scaledImage = uTool.scaleImage(scaledImage, (int) (scaledImage.getWidth() * scale), (int) (scaledImage.getHeight() * scale));
		}
		catch(Exception e)
		{
			System.err.println("This image does not exist " + imagePath + ".png");
		}
		
		return scaledImage;
	}
	
	public BufferedImage fastSetup(String imagePath, double scale) throws MalformedURLException
	{
		
		BufferedImage image = uTool.fastLoadImage(new File("/D:/School%20Programming%20Workspace/2D%20JRPG/bin/CombatSprites/" + imagePath + ".png").toURI().toURL());
		image = uTool.scaleImage(image, (int) (image.getWidth() * scale), (int) (image.getHeight() * scale)); //Scale the image
		return image;
	}
	
	protected BufferedImage setIcon(String Folder)
	{
		BufferedImage Ico = null;
		try
		{
			Ico = ImageIO.read(getClass().getResourceAsStream("/CombatSprites/" + Folder + "/icon.png"));
			//Forumla, cause I'm too lazy to super gp, gp.scale * 10;
			Ico = uTool.scaleImage(Ico, 46, 46);
		}
		catch(Exception e)
		{
			System.err.println("Icon doesn't exist - Path checked: /CombatSprites/" + Folder + "/icon.png");
		}
		
		return Ico;
	}
	
	public void lvlCalc()
	{
		while(exp > maxEXP)
		{
			System.out.println("Level up!");
			exp -= maxEXP;
			lvl++;
			maxEXP = (long) (((lvl * 100) * lvl) * (double) (Math.log(lvl) + 1));
			
			System.out.println(name + "'s EXP: " + exp + ", new Limit: " + maxEXP + ", at lvl: " + lvl);
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Thread section. Yikes.
	Thread loadIdle;
	Thread loadAttack;
	Thread loadDashback;
	Thread loadSkill1;
	Thread loadSkill2;
	Thread loadSkill3;
	Thread loadSkill3B;
	Thread loadSkill3Lib;
	Thread loadDash; 
	
	public boolean checkThread(int threadNum)
	{
		switch(threadNum)
		{
		case 0:
			if(loadIdle.getState().toString() == "TERMINATED")
			{
				return true;
			}
			break;
		case 1:
			if(loadAttack.getState().toString() == "TERMINATED")
			{
				return true;
			}
			break;
		case 2:
			if(loadDashback.getState().toString() == "TERMINATED")
			{
				return true;
			}			
			break;
		case 3:
			if(loadSkill1.getState().toString() == "TERMINATED")
			{
				return true;
			}			
			break;
		case 4:
			if(loadSkill2 == null)
			{
				return true;
			}
			
			if(loadSkill2.getState().toString() == "TERMINATED")
			{
				return true;
			}			
			break;
		case 5:
			if(loadSkill3 == null)
			{
				return true;
			}
			
			if(loadSkill3.getState().toString() == "TERMINATED")
			{
				return true;
			}			
			break;
		case 6:
			if(loadSkill3B == null)
			{
				return true;
			}
			
			if(loadSkill3B.getState().toString() == "TERMINATED")
			{
				return true;
			}			
			break;
		case 7:
			if(loadSkill3Lib == null)
			{
				return true;
			}
			
			if(loadSkill3Lib.getState().toString() == "TERMINATED")
			{
				return true;
			}			
			break;
		case 8:
			if(loadDash.getState().toString() == "TERMINATED")
			{
				return true;
			}			
			break;
		}
		return false;
	}
	
	public void loadThread(int threadNum)
	{
		switch(threadNum)
		{
		case 0:
			if(idleFrames == null)
			{
				break;
			}
			createLoadIdle();
			break;
		case 1:
			if(attackFrames == null)
			{
				break;
			}
			createLoadAttack();
			break;
		case 2:
			if(dashbackFrames == null)
			{
				break;
			}
			createLoadDashback();
			break;
		case 3:
			if(skill1Frames == null)
			{
				break;
			}
			createLoadSkill1();
			break;
		case 4:
			if(skill2Frames == null)
			{
				break;
			}
			createLoadSkill2();
			break;
		case 5:
			if(skill3Frames == null)
			{
				break;
			}
			createLoadSkill3();
			break;
		case 6:
			if(skill3bFrames == null)
			{
				break;
			}
			createLoadSkill3B();
			break;
		case 7:
			if(skill3LibFrames == null)
			{
				break;
			}
			createLoadSkill3Lib();
			break;
		case 8:
			createLoadDash();
			break;
		}
	}
	
	private void createLoadIdle()
	{
		loadIdle = new Thread(new Runnable() 
		{
			int tAnimNum;
			String tSubFolder;
			String tAnimName;
			
			@Override
			public void run() 
			{
				tSubFolder = "Idle";
				tAnimName = "idle";
				tAnimNum = idleFrames.length;
				
				for(int i = 0; i < tAnimNum; i++)
				{
					//If animNum is above 100
					if(tAnimNum > 100 && i < 10)
					{
						idleFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_00" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 10 && i < 100)
					{
						idleFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 100)
					{
						idleFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(tAnimNum < 100 && i < 10)
					{
						idleFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else
					{
						idleFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
				}
//				System.out.println(tSubFolder + " completed. for: " + name);
			}
			
		});
		
		loadIdle.start();
	}
	
	private void createLoadAttack()
	{
		loadAttack = new Thread(new Runnable() 
		{
			int tAnimNum;
			String tSubFolder;
			String tAnimName;
			
			@Override
			public void run() 
			{
				tSubFolder = "Attack";
				tAnimName = "attack1";
				tAnimNum = attackFrames.length;
				
				for(int i = 0; i < tAnimNum; i++)
				{
					//If animNum is above 100
					if(tAnimNum > 100 && i < 10)
					{
						attackFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_00" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 10 && i < 100)
					{
						attackFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 100)
					{
						attackFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(tAnimNum < 100 && i < 10)
					{
						attackFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else
					{
						attackFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
				}
//				System.out.println(tSubFolder + " completed. for: " + name);
			}
			
		});
		
		loadAttack.start();
	}
	
	private void createLoadDashback()
	{
		loadDashback = new Thread(new Runnable() 
		{
			int tAnimNum;
			String tSubFolder;
			String tAnimName;
			
			@Override
			public void run() 
			{
				tSubFolder = "Back";
				tAnimName = "back";
				tAnimNum = dashbackFrames.length;
				
				for(int i = 0; i < tAnimNum; i++)
				{
					//If animNum is above 100
					if(tAnimNum > 100 && i < 10)
					{
						dashbackFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_00" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 10 && i < 100)
					{
						dashbackFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 100)
					{
						dashbackFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(tAnimNum < 100 && i < 10)
					{
						dashbackFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else
					{
						dashbackFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
				}
//				System.out.println(tSubFolder + " completed. for: " + name);
			}
			
		});
		
		loadDashback.start();
	}
	
	private void createLoadSkill1()
	{
		loadSkill1 = new Thread(new Runnable() 
		{
			int tAnimNum;
			String tSubFolder;
			String tAnimName;
			
			@Override
			public void run() 
			{
				tSubFolder = "Skill1";
				tAnimName = "skill1";
				tAnimNum = skill1Frames.length;
				
				for(int i = 0; i < tAnimNum; i++)
				{
					//If animNum is above 100
					if(tAnimNum > 100 && i < 10)
					{
						skill1Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_00" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 10 && i < 100)
					{
						skill1Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 100)
					{
						skill1Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(tAnimNum < 100 && i < 10)
					{
						skill1Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else
					{
						skill1Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
				}
//				System.out.println(tSubFolder + " completed. for: " + name);
			}
			
		});
		
		loadSkill1.start();
	}
	
	private void createLoadSkill2()
	{
		loadSkill2 = new Thread(new Runnable() 
		{
			int tAnimNum;
			String tSubFolder;
			String tAnimName;
			
			@Override
			public void run() 
			{
				tSubFolder = "Skill2";
				tAnimName = "skill2";
				tAnimNum = skill2Frames.length;
				
				
				for(int i = 0; i < tAnimNum; i++)
				{
					//If animNum is above 100
					if(tAnimNum > 100 && i < 10)
					{
						skill2Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_00" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 10 && i < 100)
					{
						skill2Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 100)
					{
						skill2Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(tAnimNum < 100 && i < 10)
					{
						skill2Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else
					{
						skill2Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
				}
//				System.out.println(tSubFolder + " completed. for: " + name);
			}
			
		});
		
		loadSkill2.start();
	}
	
	private void createLoadSkill3()
	{	
		loadSkill3 = new Thread(new Runnable() 
		{
			int tAnimNum;
			String tSubFolder;
			String tAnimName;
			
			@Override
			public void run() 
			{	
				tSubFolder = "Skill3";
				tAnimName = "skill3";
				tAnimNum = skill3Frames.length;
				
				System.out.println("Skill 3 length: " + tAnimNum);
				
				for(int i = 0; i < tAnimNum; i++)
				{
					//If animNum is above 100
					if(tAnimNum > 100 && i < 10)
					{
						skill3Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_00" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 10 && i < 100)
					{
						skill3Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 100)
					{
						skill3Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(tAnimNum < 100 && i < 10)
					{
						skill3Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else
					{
						skill3Frames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
				}
//				System.out.println(tSubFolder + " completed. for: " + name);
			}
			
		});
		
		loadSkill3.start();
	}
	
	private void createLoadSkill3B()
	{	
		loadSkill3B = new Thread(new Runnable() 
		{
			int tAnimNum;
			String tSubFolder;
			String tAnimName;
			
			@Override
			public void run() 
			{	
				tSubFolder = "Skill3b";
				tAnimName = "skill3b";
				tAnimNum = skill3bFrames.length;
				
				for(int i = 0; i < tAnimNum; i++)
				{
					//If animNum is above 100
					if(tAnimNum > 100 && i < 10)
					{
						skill3bFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_00" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 10 && i < 100)
					{
						skill3bFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 100)
					{
						skill3bFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(tAnimNum < 100 && i < 10)
					{
						skill3bFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else
					{
						skill3bFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
				}
//				System.out.println(tSubFolder + " completed. for: " + name);
			}
		});
		
		loadSkill3B.start();
	}
	
	private void createLoadSkill3Lib()
	{	
		loadSkill3Lib = new Thread(new Runnable() 
		{
			int tAnimNum;
			String tSubFolder;
			String tAnimName;
			
			@Override
			public void run() 
			{	
				tSubFolder = "Skill3Library";
				tAnimName = "skill3-library";
				tAnimNum = skill3LibFrames.length;
				
				for(int i = 0; i < tAnimNum; i++)
				{
					//If animNum is above 100
					if(tAnimNum > 100 && i < 10)
					{
						skill3LibFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_00" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 10 && i < 100)
					{
						skill3LibFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else if(tAnimNum > 100 && i >= 100)
					{
						skill3LibFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(tAnimNum < 100 && i < 10)
					{
						skill3LibFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else
					{
						skill3LibFrames[i] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
				}
//				System.out.println(tSubFolder + " completed. For: " + name);
			}
		});
		
		loadSkill3Lib.start();
	}
	
	private void createLoadDash()
	{	
		loadDash = new Thread(new Runnable() 
		{
			int tAnimNum;
			String tSubFolder;
			String tAnimName;
			
			@Override
			public void run() 
			{
				tSubFolder = "Dash";
				tAnimName = "in";
				tAnimNum = dashFrames.length;
				
				dashFrames[0] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_front_0", imageScale);
				dashFrames[1] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_front_1", imageScale);
				
				for(int i = 0; i < tAnimNum - 2; i++)
				{
					if(i < 10)
					{
						dashFrames[i + 2] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_0" + i, imageScale);
					}
					else
					{
						dashFrames[i + 2] = setup(Folder + "/" + tSubFolder + "/" + imageID + "_" + tAnimName + "_" + i, imageScale);
					}
				}
//				System.out.println(tSubFolder + " completed. for: " + name);
			}
		});
		
		loadDash.start();
	}
}
