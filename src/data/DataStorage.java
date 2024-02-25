package data;

import java.io.Serializable;

public class DataStorage implements Serializable
{
//	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -654672569390582185L;
	//SYSTEM stuff
	
	//Player position
	protected int PlayerWorldX;
	protected int PlayerWorldY;
	protected String playerDir;
	//NPC position
	protected EntityStorage[] NPC;
	protected EntityStorage[] obj;
	protected EntityStorage[] Monster;
	//Questline
	
	//Events
	
	//Player stats
	protected int playerStam;
	//Companion stats
	
	//Player inventory
	
	
	public class EntityStorage implements Serializable
	{
		private static final long serialVersionUID = -6249157121688050735L;
		int worldX; int worldY; int waypoint; int wait;
		boolean lockWaypoint; boolean onPath;
		String direction;
		int spriteNum;
		boolean collision;
		
		String name;
	}
}