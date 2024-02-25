package ai;

public class Node 
{
	Node parent;
	public int col;
	public int row;
	//From starting node
	public int gCost;
	//From goal node
	public int hCost;
	//Total cost
	public int fCost;
	
	boolean solid;
	boolean open;
	boolean checked;
	
	public Node(int col, int row)
	{
		this.col = col;
		this.row = row;
	}
}
