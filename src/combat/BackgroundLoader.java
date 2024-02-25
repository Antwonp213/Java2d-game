package combat;

public class BackgroundLoader extends CombatEntity
{
	public boolean checkThread(int threadNum)
	{
		boolean flag = false;
		switch(threadNum)
		{
		case 0:
			flag = loadIdle == null ? true : false;
			break;
		case 1:
			flag = loadAttack == null ? true : false;
			break;
		case 2:
			flag = loadDashback == null ? true : false;
			break;
		case 3:
			flag = loadSkill1 == null ? true : false;
			break;
		case 4:
			flag = loadSkill2 == null ? true : false;
			break;
		case 5:
			flag = loadSkill3 == null ? true : false;
			break;
		case 6:
			flag = loadSkill3B == null ? true : false;
			break;
		case 7:
			flag = loadSkill3Lib == null ? true : false;
			break;
		case 8:
			flag = loadDash == null ? true : false;
			break;
		}
		return flag;
	}
	
	
	Thread loadIdle;
	
	Thread loadAttack;
	
	Thread loadDashback;
	
	Thread loadSkill1;
	
	Thread loadSkill2;
	
	Thread loadSkill3;
	
	Thread loadSkill3B;
	
	Thread loadSkill3Lib;
	
	Thread loadDash; 
	
	public void loadThread(int threadNum, CombatEntity entity)
	{
		switch(threadNum)
		{
		case 0:
			createLoadIdle(entity);
			break;
		case 1:
			createLoadAttack(entity);
			break;
		case 2:
			createLoadDashback(entity);
			break;
		case 3:
			createLoadSkill1(entity);
			break;
		case 4:
			createLoadSkill2(entity);
			break;
		case 5:
			createLoadSkill3(entity);
			break;
		case 6:
			createLoadSkill3B(entity);
			break;
		case 7:
			createLoadSkill3Lib(entity);
			break;
		case 8:
			createLoadDash(entity);
			break;
		}
	}
	
	private void createLoadIdle(CombatEntity entity)
	{
		loadIdle = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				entity.subFolder = "Idle";
				entity.animName = "idle";
				animNum = idleFrames.length;
				
				String subFolder = entity.subFolder;
				String animName = entity.animName;
				int animNum = entity.animNum;
				int imageID = entity.imageID;
				double imageScale = entity.imageScale;
				
				for(int i = 0; i < animNum; i++)
				{
					if(i < 10)
					{
						entity.idleFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else
					{
						entity.idleFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
				}
			}
			
		});
		
		loadIdle.start();
	}
	
	private void createLoadAttack(CombatEntity entity)
	{
		loadAttack = new Thread(new Runnable() 
		{

			@Override
			public void run() 
			{
				entity.subFolder = "Attack";
				entity.animName = "attack1";
				entity.animNum = attackFrames.length;
				
				String subFolder = entity.subFolder;
				String animName = entity.animName;
				int animNum = entity.animNum;
				int imageID = entity.imageID;
				double imageScale = entity.imageScale;
				
				for(int i = 0; i < animNum; i++)
				{
					if(animNum > 100 && i < 10)
					{
						entity.attackFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_00" + i, imageScale);
					}
					else if(animNum > 100 && i >= 10 && i < 100)
					{
						entity.attackFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else if(animNum > 100 && i >= 100)
					{
						entity.attackFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(animNum < 100 && i < 10)
					{
						entity.attackFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else
					{
						entity.attackFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
				}
			}
			
		});
		
		loadAttack.start();
	}
	
	private void createLoadDashback(CombatEntity entity)
	{
		loadDashback = new Thread(new Runnable() 
		{

			@Override
			public void run() 
			{
				entity.subFolder = "Back";
				entity.animName = "back";
				entity.animNum = dashbackFrames.length;
				
				String subFolder = entity.subFolder;
				String animName = entity.animName;
				int animNum = entity.animNum;
				int imageID = entity.imageID;
				double imageScale = entity.imageScale;
				
				for(int i = 0; i < animNum; i++)
				{
					if(i < 10)
					{
						entity.dashbackFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else
					{
						entity.dashbackFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
				}
			}
			
		});
		
		loadDashback.start();
	}
	
	private void createLoadSkill1(CombatEntity entity)
	{
		loadSkill1 = new Thread(new Runnable() 
		{

			@Override
			public void run() 
			{
				entity.subFolder = "Skill1";
				entity.animName = "skill1";
				entity.animNum = skill1Frames.length;
				
				String subFolder = entity.subFolder;
				String animName = entity.animName;
				int animNum = entity.animNum;
				int imageID = entity.imageID;
				double imageScale = entity.imageScale;
				
				for(int i = 0; i < animNum; i++)
				{
					//If animNum is above 100
					if(animNum > 100 && i < 10)
					{
						entity.skill1Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_00" + i, imageScale);
					}
					else if(animNum > 100 && i >= 10 && i < 100)
					{
						entity.skill1Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else if(animNum > 100 && i >= 100)
					{
						entity.skill1Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(animNum < 100 && i < 10)
					{
						entity.skill1Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else
					{
						entity.skill1Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
				}
			}
			
		});
		
		loadSkill1.start();
	}
	
	private void createLoadSkill2(CombatEntity entity)
	{
		loadSkill2 = new Thread(new Runnable() 
		{

			@Override
			public void run() 
			{
				entity.subFolder = "Skill2";
				entity.animName = "skill2";
				entity.animNum = skill2Frames.length;
				
				String subFolder = entity.subFolder;
				String animName = entity.animName;
				int animNum = entity.animNum;
				int imageID = entity.imageID;
				double imageScale = entity.imageScale;
				
				for(int i = 0; i < animNum; i++)
				{
					//If animNum is above 100
					if(animNum > 100 && i < 10)
					{
						entity.skill2Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_00" + i, imageScale);
					}
					else if(animNum > 100 && i >= 10 && i < 100)
					{
						entity.skill2Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else if(animNum > 100 && i >= 100)
					{
						entity.skill2Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(animNum < 100 && i < 10)
					{
						entity.skill2Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else
					{
						entity.skill2Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
				}
			}
			
		});
		
		loadSkill2.start();
	}
	
	private void createLoadSkill3(CombatEntity entity)
	{
		loadSkill3 = new Thread(new Runnable() 
		{

			@Override
			public void run() 
			{
				entity.subFolder = "Skill3";
				entity.animName = "skill3";
				entity.animNum = skill3Frames.length;
				
				String subFolder = entity.subFolder;
				String animName = entity.animName;
				int animNum = entity.animNum;
				int imageID = entity.imageID;
				double imageScale = entity.imageScale;
				
				for(int i = 0; i < animNum; i++)
				{
					//If animNum is above 100
					if(animNum > 100 && i < 10)
					{
						entity.skill3Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_00" + i, imageScale);
					}
					else if(animNum > 100 && i >= 10 && i < 100)
					{
						entity.skill3Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else if(animNum > 100 && i >= 100)
					{
						entity.skill3Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(animNum < 100 && i < 10)
					{
						entity.skill3Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else
					{
						entity.skill3Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
				}
			}
			
		});
		
		loadSkill3.start();
	}
	
	private void createLoadSkill3B(CombatEntity entity)
	{
		loadSkill3B = new Thread(new Runnable() 
		{

			@Override
			public void run() 
			{
				entity.subFolder = "Skill3b";
				entity.animName = "skill3b";
				entity.animNum = skill3bFrames.length;
				
				String subFolder = entity.subFolder;
				String animName = entity.animName;
				int animNum = entity.animNum;
				int imageID = entity.imageID;
				double imageScale = entity.imageScale;
				
				for(int i = 0; i < animNum; i++)
				{
					//If animNum is above 100
					if(animNum > 100 && i < 10)
					{
						entity.skill3bFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_00" + i, imageScale);
					}
					else if(animNum > 100 && i >= 10 && i < 100)
					{
						entity.skill3Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else if(animNum > 100 && i >= 100)
					{
						entity.skill3Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(animNum < 100 && i < 10)
					{
						entity.skill3Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else
					{
						entity.skill3Frames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
				}
			}
			
		});
		
		loadSkill3B.start();
	}
	
	private void createLoadSkill3Lib(CombatEntity entity)
	{
		loadSkill3Lib = new Thread(new Runnable() 
		{

			@Override
			public void run() 
			{
				entity.subFolder = "Skill3Library";
				entity.animName = "skill3-library";
				entity.animNum = skill3LibFrames.length;
				
				String subFolder = entity.subFolder;
				String animName = entity.animName;
				int animNum = entity.animNum;
				int imageID = entity.imageID;
				double imageScale = entity.imageScale;
				
				for(int i = 0; i < animNum; i++)
				{
					//If animNum is above 100
					if(animNum > 100 && i < 10)
					{
						entity.skill3LibFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_00" + i, imageScale);
					}
					else if(animNum > 100 && i >= 10 && i < 100)
					{
						entity.skill3LibFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else if(animNum > 100 && i >= 100)
					{
						entity.skill3LibFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
					//If animNum is below 100 but above 10
					else if(animNum < 100 && i < 10)
					{
						entity.skill3LibFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else
					{
						entity.skill3LibFrames[i] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
				}
			}
			
		});
		
		loadSkill3Lib.start();
	}
	
	private void createLoadDash(CombatEntity entity)
	{
		loadDash = new Thread(new Runnable() 
		{

			@Override
			public void run() 
			{
				entity.subFolder = "Dash";
				entity.animName = "in";
				entity.animNum = dashFrames.length;
				
				String subFolder = entity.subFolder;
				String animName = entity.animName;
				int animNum = entity.animNum;
				int imageID = entity.imageID;
				double imageScale = entity.imageScale;
				
				entity.dashFrames[0] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_front_0", imageScale);
				entity.dashFrames[1] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_front_1", imageScale);
				
				for(int i = 0; i < animNum - 2; i++)
				{
					if(i < 10)
					{
						entity.dashFrames[i + 2] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_0" + i, imageScale);
					}
					else
					{
						entity.dashFrames[i + 2] = setup(entity.Folder + "/" + subFolder + "/" + imageID + "_" + animName + "_" + i, imageScale);
					}
				}
			}
			
		});
		
		loadDash.start();
	}
}
