package object;

import entities.Entity;
import main.GameWindow;

public class OBJ_Door extends Entity
{	
	GameWindow gp;
	
	public static final String OBJName = "Door";
	
	public OBJ_Door(GameWindow gp)
	{
		super(gp);
		name = "Door";
		collision = true;
		Down1 = setup("/Objects/Door");
		Down2 = setup("/Objects/Door Open");
		
		solidArea.x = 0;
		solidArea.y = 40;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 80;
		solidArea.height = 20;
	}
}
