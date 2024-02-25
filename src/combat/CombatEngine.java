package combat;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

import ai.CombatAI;
import combat.combatMonsters.Bandit;
import combat.combatMonsters.BanditBruiser;
import combat.combatMonsters.BanditSkirmisher;
import combat.combatMonsters.MinibossGuard;
import combat.combatMonsters.Wolf;
import combat.combatPlayable.CombatPlayer;
import combat.combatPlayable.KonradTheWizard;
import combat.combatPlayable.LadyClorance;
import combat.combatPlayable.SirVox;
import main.GameWindow;
import main.KeyHandler;
import main.UtilityTool;
import ui.UI_OLD;

public class CombatEngine 
{
	UtilityTool uTool = new UtilityTool();
	GameWindow gp;
	KeyHandler keyH;
	UI_OLD UI;
	
	CombatEntity mainCE = new CombatEntity();
	CombatEntity[] Monster;
	CombatEntity[] Playable = new CombatEntity[3];
	CombatPlayer Player = new CombatPlayer();
	CombatAI AI = new CombatAI(Player, Playable);
	
	//Manipulatd by UI, set ID's corrosponding to those set in CombatEntity classes
	public byte[] partyMemberID = new byte[3];
	public byte companionsPresent = 0;
	public int animationSet = 0;
	
	//Combat Engine variables
	public int tileInfo;
	public int monsterCount;
	public final int speedGoal = 1000; //The speed you need to his before you can take a turn
	
	//Used to draw in order, and to determine whose turn it is
//	ArrayList<CombatEntity> turnQueue = new ArrayList<CombatEntity>();
	LinkedList<CombatEntity> entityList = new LinkedList<CombatEntity>();
	LinkedList<CombatEntity> drawOrder = new LinkedList<CombatEntity>();
	ArrayList<CombatEntity> turnQueue = new ArrayList<CombatEntity>();
	
	public CombatEngine(GameWindow gp, KeyHandler keyH)
	{
		this.gp = gp;
		this.keyH = keyH;
		this.UI = gp.UI;
		
		partyMemberID[0] = 2;
		partyMemberID[1] = 0;
		partyMemberID[2] = 0;
	}
	
	public void loadEncounter(int id)
	{
		Random rand = new Random();
		monsterCount = rand.nextInt(6) + 1;
		Monster = new CombatEntity[monsterCount];
		
		drawOrder.clear();
		entityList.clear();
		
		loadMonsters = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				loadMonsters(rand);
			}
		});
		
		loadPlayables = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				loadPlayables(rand);
			}
		});
		
		switch(id)
		{
		case 0:
		default: 
			loadMonsters.start();
			loadPlayables.start();
			break;
		case 1:
			loadPlayables.start();
			//Load special encounter
			Monster = new CombatEntity[1];
			
			Monster[0] = new MinibossGuard();
			
			Monster[0].pos = 4;
			setPos(Monster);
			drawOrder.add(Monster[0]);
			entityList.add(Monster[0]);
			
			break;
		}
				
		if(loadPlayables.getState() == Thread.State.RUNNABLE)
		{
			while(!(loadPlayables.getState() == Thread.State.TERMINATED));
		}
		
		if(loadMonsters.getState() == Thread.State.RUNNABLE)
		{
			while(!(loadMonsters.getState() == Thread.State.TERMINATED));
		}
		
		
		//Assign ID by turnOrder & reset speedCounter
		for(int i = 0; i < entityList.size(); i++)
		{
			entityList.get(i).spdRoll = (int) (rand.nextInt(entityList.get(i).spd) + ((int) entityList.get(i).spd * 0.3));
			entityList.get(i).speedCounter = entityList.get(i).spdRoll * rand.nextInt(100) + 1;
			entityList.get(i).turnID = i;
		}
		
		curEntity = 0;
		turnNumber = 0;
		tempCount = 0;
		entityTurnMax = drawOrder.size();
		
		turnNumber = 0;
		
		currentListIndex = drawOrder.size() - 1;
	}
	
	
	int curEntity;
	int entityTurnMax;
	int turnNumber;
	int tempCount;
	int currentListIndex = 0;
	boolean entityHasTurn;
	boolean entityDidTurn = false;
	int entityCurrentTurnID;
	private final int speedLimit = 10000;
	int turnCounter = 0;
	public void loop()
	{
		//Main combat loop
		if(keyH.SKIP && gp.DEBUG)
		{
			System.err.println("Ending combat");
			turnQueue.clear();
			keyH.SKIP = false;
			gp.gameState = gp.combatOverState;
			finish();
		}	
		
		tempCount++;
		if(tempCount > 0)
		{
			//Increment speed counters for each entity, based on their speed stat
			if(turnQueue.size() == 0)
			{
				for(int i = 0; i < entityList.size(); i++)
				{
					entityList.get(i).speedCounter += entityList.get(i).spdRoll;
					
					if(entityList.get(i).speedCounter > speedLimit)
					{
						for(int j = 0; j < entityList.size(); j++)
						{
							if(entityList.get(i).speedCounter == entityList.get(j).speedCounter)
							{
								if(entityList.get(i).speedCounter == entityList.get(j).speedCounter && i != j)
								{
//									System.out.println("Rare case, pushing both to stack");
									turnQueue.add(entityList.get(i));
									turnQueue.add(entityList.get(j));
								}
								turnQueue.add(entityList.get(j));
							}
						}
					}
				}
			}
			else
			{
//				
				if(turnCounter != 30)
				{
					turnCounter++;
					return;
				}
				turnCounter = 0;
				
				entityCurrentTurnID = turnQueue.get(0).turnID;
//				System.out.println(turnOrder.get(entityCurrentTurnID).name + " has a turn ");
//				System.out.println(turnOrder.get(entityCurrentTurnID).speedCounter);
				entityList.get(entityCurrentTurnID).getAction();
				System.out.println(entityList.get(entityCurrentTurnID).name + " has a turn");
				entityDidTurn = true;
				if(entityDidTurn)
				{
					entityList.get(entityCurrentTurnID).speedCounter -= speedLimit;
					turnQueue.remove(0);
//					System.out.println(turnOrder.get(entityCurrentTurnID).speedCounter);
					entityHasTurn = false;
				}
			}
		}
	}
	
	int IMGCount = 1;
	int tempIMGCount = 0;
	int tempSpriteNum = 0;
	
	public void repaintLoop(Graphics2D g2)
	{
		//TODO: In the future, rotate drawings based on turn-order. With the active turn being last to draw
		//TODO: Add zoom in and out features.
		//IDEA: For render engine, add everything to an ArrayList. When it's someone's turn, add them to the beginning of the list, when they're done replace back to original spot.
		CombatAnimator ca = new CombatAnimator(gp, g2);
		
		BufferedImage image;
		
		for(int i = 0; i < drawOrder.size(); i++)
		{
			image = getFrame(drawOrder.get(i));
			int x = drawOrder.get(i).spriteX - image.getWidth()/2 - drawOrder.get(i).offsetX;
			int y = drawOrder.get(i).spriteY - image.getHeight()/2 - drawOrder.get(i).offsetY;
			g2.drawImage(image, x, y, null);		
		}
		
		//DRAW UI
		UI.drawTurnOrderBar(g2, entityList);
		
		//DEBUG section
		if(gp.DEBUG == true)
		{
//			UI.drawDrawListOrder(g2, drawOrder);
			drawDebug(g2);
		}
	}
	
	public BufferedImage getFrame(CombatEntity cEntity)
	{
		//Pick Animation array based on action
		if(cEntity.idleFrames.length == 1)
		{
			return cEntity.idleFrames[0];
		}
		//updateSpriteNum
		cEntity.spriteCounter++;
		if(cEntity.spriteCounter > 1)
		{
			cEntity.spriteCounter = 0;
			cEntity.spriteNum++;
			if(cEntity.spriteNum == cEntity.idleFrames.length)
			{
				cEntity.spriteNum = 0;
			}
		}
		
//			System.out.println(cEntity.name + " has tried to grab frame " + cEntity.spriteNum);
		return cEntity.idleFrames[cEntity.spriteNum];
	}
	
	public void drawDebug(Graphics2D g2)
	{
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.black);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 50));
		g2.drawString("Press 'O' to close the combat screen", 51, 49);
		g2.drawString("Press 'O' to close the combat screen", 51, 51);
		g2.drawString("Press 'O' to close the combat screen", 49, 49);
		g2.drawString("Press 'O' to close the combat screen", 49, 51);
		g2.setColor(Color.white);
		g2.drawString("Press 'O' to close the combat screen", 50, 50);
		//Draw 4 squares where I think I want my player and companions to be
		g2.setColor(new Color(50, 181, 184));
		g2.fillRect(Player.spriteX - 5, Player.spriteY - 5, 10, 10);
		if(Playable[0] != null)
		{
			g2.fillRect(Playable[0].spriteX - 5, Playable[0].spriteY - 5, 10, 10);
		}
		if(Playable[1] != null)
		{
			g2.fillRect(Playable[1].spriteX - 5, Playable[1].spriteY - 5, 10, 10);
		}
		if(Playable[2] != null)
		{
			g2.fillRect(Playable[2].spriteX - 5, Playable[2].spriteY - 5, 10, 10);
		}
		
		//Draw 6 squares where I think I want the enemies to be
		g2.setColor(new Color(245, 10, 225));
		//Row 1
		g2.fillRect(Monster[0].x1 - 5, Monster[0].row1 - 5, 10, 10);
		g2.fillRect(Monster[0].x2 - 5, Monster[0].row1 - 5, 10, 10);
		g2.fillRect(Monster[0].x3 - 5, Monster[0].row1 - 5, 10, 10);
		
		//Row 2
		g2.fillRect(Monster[0].x1 - 5, Monster[0].row2 - 5, 10, 10);
		g2.fillRect(Monster[0].x2 - 5, Monster[0].row2 - 5, 10, 10);
		g2.fillRect(Monster[0].x3 - 5, Monster[0].row2 - 5, 10, 10);
		
		//Draw 2 horizontal lines on where I want the enemies to stand
		g2.drawLine(0, Monster[0].row1 + 108, gp.screenWidth/2, Monster[0].row1 + 108);
		g2.drawLine(0, Monster[0].row2 + 108, gp.screenWidth/2, Monster[0].row2 + 108);
		
		g2.drawString("Current Turn: " + turnNumber, 50, 100);
	}
	
	public void finish()
	{
		if(gp.forceEncounter != 0)
		{
			switch(gp.forceEncounter)
			{
			case 1:
				gp.NPC[2] = null;
				break;
			}
			
			gp.forceEncounter = 0;
		}
		
		for(int i = 0; i < Monster.length; i++)
		{
			Player.exp += Monster[i].exp;
		}
		
		Player.lvlCalc();
		
		for(int i = 0; i < partyMemberID.length; i++)
		{
			if(Playable[i] != null)
			{
				for(int j = 0; j < Monster.length; j++)
				{
					Playable[i].exp += Monster[j].exp;
				}
				
				Playable[i].lvlCalc();
			}
		}
		
		//Display new skills from level-ups & change ability scores
		
		//Determine loot drops from Monster IDs
		
		//Add loot drops to player inventory
		//Determine how much loot drops from each monster
		//Determine what rarity of loot is dropped
		
		//Last, but not least, clear the monster object array for the next iteration.
		drawOrder.clear();
		entityList.clear();
		
		Monster = null;
		
		gp.stopMusic();
		gp.playMusic(1);
		gp.gameState = gp.playState;
	}
	
	//At the bottom, because when I start implementing the game, this list can get LOOOOOOOOONG
	public void assignMonsterList(CombatEntity[] Monster, int i)
	{
		//TODO: ASSIGN MONSTER LIST BASED ON MAP, NOT TILE
		
		Random random = new Random();
		int enemyType;
		
		//TODO: Temporary fix
		enemyType = random.nextInt(6);
		switch(enemyType)
		{
		case 0:
			Monster[i] = new Bandit();
			System.out.println(Monster[i].name + " created");
			Monster[i].lvl = random.nextInt((6 + 1) - Player.lvl) + Player.lvl;
			Monster[i].setStats();
			break;
		case 1:
			Monster[i] = new BanditBruiser();
			System.out.println(Monster[i].name + " created");
			Monster[i].lvl = random.nextInt((6 + 1) - Player.lvl) + Player.lvl;
			Monster[i].setStats();
			break;
		case 2:
			Monster[i] = new BanditSkirmisher();
			System.out.println(Monster[i].name + " created");
			Monster[i].lvl = random.nextInt((6 + 1) - Player.lvl) + Player.lvl;
			Monster[i].setStats();
			break;
		case 3:
		case 4:
		case 5:
			Monster[i] = new Wolf(enemyType - 3);
			System.out.println(Monster[i].name + " created");
			Monster[i].lvl = random.nextInt((6 + 1) - Player.lvl) + Player.lvl;
			Monster[i].setStats();
			break;
		}		
	}
	
	protected void instantiatePlayable(CombatEntity[] cEntity, int i, byte ID)
	{		
		try
		{
			switch(ID)
			{
			default:
				throw new Exception("Invalid character " + ID);
			case 0:
				return;
			case 1:
				throw new Exception("Invalid Character");
			case 2:
				cEntity[i] = new KonradTheWizard();
				System.out.println("Konrad created");
				break;
			case 3:
				cEntity[i] = new SirVox();
				System.out.println("Vox created");
				break;
			case 4:
				cEntity[i] = new LadyClorance();
				System.out.println("Clorance created");
				break;
			}
		}
		catch(Exception e)
		{
			System.err.println("The character with an ID of " + ID + " does not exist");
			e.printStackTrace();
		}
	}
	
	Thread loadMonsters;
	Thread loadPlayables;
	
	protected void loadMonsters(Random rand)
	{
		boolean[] finished = new boolean[monsterCount];
		
		for(int i = 0; i < finished.length; i++)
		{
			finished[i] = false;
		}
		
		for(int i = 0; i < monsterCount; i++)
		{
			int idx = i;
			//Randomly assign monsters from a list using tile information, we will initializing an index of the object array for Monster
			Thread instantiateM = new Thread(new Runnable() 
			{
				@Override
				public void run()
				{
					assignMonsterList(Monster, idx);
					Monster[idx].spriteNum = rand.nextInt(Monster[idx].idleFrames.length);
					Monster[idx].spriteCounter = 0;
					finished[idx] = true;
				}
			});
			
			instantiateM.start();
		}
		
		for(int i = 0; i < finished.length; i++)
		{
			if(!finished[i])
			{
				i = -1;
				continue;
			}
		}
		
		//Assign positions randomly
		for(int i = 0; i < monsterCount; i++)
		{	
			//Get position value from 1 to 6
			int position = rand.nextInt(6) + 1;
			
			//Check each monster's position in array. If there is a match, then re-roll. Keep re-rolling until there is an empty position
			for(int j = 0; j < monsterCount; j++) 
			{	
				if(Monster[j] != null && Monster[j].pos == position)
				{
					position = rand.nextInt(6) + 1;
					j = -1; //Reset J to recheck the entire list again
				}
			}
			
			Monster[i].pos = position;
		}
		
		//Assign spriteX and spriteY values based on position
		setPos(Monster);
		
		for(int i = 0; i < monsterCount; i++)
		{
			if(i % 2 == 1)
			{
				drawOrder.add(Monster[i]);
			}
		}
		
		for(int i = 0; i < monsterCount; i++)
		{
			if(i % 2 == 0)
			{
				drawOrder.add(Monster[i]);
			}
		}
		
		for(int i = 0; i < monsterCount; i++)
		{
			entityList.add(Monster[i]);
		}
	}
	
	protected void setPos(CombatEntity[] Monster)
	{
		for(int i = 0; i < Monster.length; i++)
		{
			switch(Monster[i].pos)
			{
			case 1:
				Monster[i].spriteX = Monster[i].x1;
				Monster[i].spriteY = Monster[i].row1;
				break;
			case 2:
				Monster[i].spriteX = Monster[i].x1;
				Monster[i].spriteY = Monster[i].row2;
				break;
			case 3:
				Monster[i].spriteX = Monster[i].x2;
				Monster[i].spriteY = Monster[i].row1;
				break;
			case 4:
				Monster[i].spriteX = Monster[i].x2;
				Monster[i].spriteY = Monster[i].row2;
				break;
			case 5:
				Monster[i].spriteX = Monster[i].x3;
				Monster[i].spriteY = Monster[i].row1;
				break;
			case 6:
				Monster[i].spriteX = Monster[i].x3;
				Monster[i].spriteY = Monster[i].row2;
				break;
			}
		}
	}
	
	protected void loadPlayables(Random rand)
	{
		boolean[] finished = new boolean[partyMemberID.length];
		
		for(int i = 0; i < finished.length; i++)
		{
			finished[i] = false;
		}
		
		companionsPresent = 3;
		for(int i = 0; i < partyMemberID.length; i++)
		{
			if(partyMemberID[i] == 0)
			{
				companionsPresent--;
			}
		}

		//PLAYABLE
		for(int i = 0; i < partyMemberID.length; i++)
		{
			if(partyMemberID[i] != 0 || partyMemberID[i] != 1)
			{
				if(Playable[i] == null)
				{
					int idx = i;
					Thread instantiate = new Thread(new Runnable() 
					{
						@Override
						public void run()
						{
							instantiatePlayable(Playable, idx, partyMemberID[idx]);
							System.out.println("Setting " + idx + " of finished to true");
							finished[idx] = true;
						}
					});
					
					instantiate.start();
				}
				else
				{
					System.out.println(Playable[i].playableID != partyMemberID[i]);
					if(Playable[i].playableID != partyMemberID[i])
					{
						Playable[i] = null;
						
						int idx = i;
						Thread instantiate = new Thread(new Runnable() 
						{
							@Override
							public void run()
							{
								instantiatePlayable(Playable, idx, partyMemberID[idx]);
								finished[idx] = true;
							}
						});
						
						instantiate.start();
					}
					else
					{
						finished[i] = true;
					}
				}
			}
			else
			{
				finished[i] = true;
			}
			
			System.out.println("IDX " + i + ", status: " + finished[i]);
		}
		
		System.out.println("Waiting for init");
		
		for(int i = 0; i < finished.length; i++)
		{
			if(!finished[i])
			{
//				System.out.println(i);
				i = -1;
				continue;
			}
		}
		
		System.out.println("Finished init");
		
		for(int i = 0; i < Playable.length; i++)
		{
			if(Playable[i] != null)
			{
				Playable[i].spriteNum = rand.nextInt(Playable[i].idleFrames.length);
				Playable[i].spriteCounter = 0;
			}
		}
		
		entityList.add(Player);
		if(Playable[0] != null)
		{
			entityList.add(Playable[0]);
		}
		if(Playable[1] != null)
		{
			entityList.add(Playable[1]);
		}
		if(Playable[2] != null)
		{
			entityList.add(Playable[2]);
		}
		
		//Set draw order
		switch(companionsPresent)
		{
		case 0:
			//X = 940, y = 480
			Player.spriteX = 940;
			Player.spriteY = 400;
			break;
		case 1:
			//x1 = 940, y1 = 300 | playerX = 940, playerY = 500
			if(Playable[0] != null)
			{
				Playable[0].spriteX = 940;
				Playable[0].spriteY = 300;
			}

			if(Playable[1] != null)
			{
				Playable[1].spriteX = 940;
				Playable[1].spriteY = 300;			
			}

			if(Playable[2] != null)
			{
				Playable[2].spriteX = 940;
				Playable[2].spriteY = 300;		
			}
			
			Player.spriteX = 940;
			Player.spriteY = 500;
			break;
		case 2:
			//x1 = 900, y1 = 300 | x2 = 980, y2 = 500 | playerX = 940, playerY = 400
			int check = 0;
			if(Playable[0] != null)
			{
				Playable[0].spriteX = 900;
				Playable[0].spriteY = 300;
				check++;
			}
			else
			{
				check += 2;
			}
			
			if(Playable[1] != null)
			{
				if(check == 1)
				{
					Playable[1].spriteX = 980;
					Playable[1].spriteY = 500;
					check = 100;
				}
				else if(check == 2)
				{
					Playable[1].spriteX = 900;
					Playable[1].spriteY = 300;
				}
			}
			
			if(check != 100)
			{
				Playable[2].spriteX = 980;
				Playable[2].spriteY = 500;
			}
			
			Player.spriteX = 940;
			Player.spriteY = 400;
			break;
		case 3:
			Playable[0].spriteX = 880;
			Playable[0].spriteY = 250;
			Player.spriteX = 900;
			Player.spriteY = 350;
			Playable[1].spriteX = 920;
			Playable[1].spriteY = 450;
			Playable[2].spriteX = 940;
			Playable[2].spriteY = 550;
			
			break;
		}
		
		//Add then sort draw order to Y values
		drawOrder.add(Player);
		if(Playable[0] != null)
		{
			drawOrder.add(Playable[0]);
		}
		if(Playable[1] != null)
		{
			drawOrder.add(Playable[1]);
		}
		if(Playable[2] != null)
		{
			drawOrder.add(Playable[2]);
		}
		
		Collections.sort(drawOrder, new Comparator<CombatEntity>()
		{
			@Override
			public int compare(CombatEntity ce1, CombatEntity ce2) 
			{
				return Integer.compare(ce1.spriteY, ce2.spriteY);
			}
	
		});
	}
}
