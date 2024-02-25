package object;

import entities.Entity;
import main.GameWindow;

public class OBJ_Key extends Entity
{
	GameWindow gp;
	
	public static final String OBJName = "Key";
	
	public OBJ_Key(GameWindow gp)
	{
		super(gp);
		name = "Key";
		Down1 = setup("/Objects/Key");
	}
}
