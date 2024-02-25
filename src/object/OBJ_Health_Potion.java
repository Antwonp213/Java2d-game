package object;

import entities.Entity;
import main.GameWindow;

public class OBJ_Health_Potion extends Entity 
{
	GameWindow gp;
	
	public static final String OBJName = "Health Potion";

	public OBJ_Health_Potion(GameWindow gp)
	{
		super(gp);
		name = "Health Potion";
		Down1 = setup("/Objects/Health Potion");
	}
}
