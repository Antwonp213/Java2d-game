package data;

import java.io.Serializable;

public class UserPreferences implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -712150499416533807L;
	//DEBUG
	public boolean debug;
	public boolean combatEnabled;
	public boolean collisionEnabled;
	public boolean displayDebugMsg;
	public boolean displayFPS;
	
	//Bools
	public boolean local;
	public boolean fullScreen;
	public boolean mute;
	
	//Ints
	public int volumeKnob;
	
}
