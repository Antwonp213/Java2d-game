package ai;

import java.util.ArrayList;

import entities.Entity;
import main.GameWindow;

public class PathFinder 
{
	GameWindow gp;
	Node[][] node; //Maps are 2D spaces, with this we can set where we want our node to be
	ArrayList<Node> openList = new ArrayList<Node>();
	public ArrayList<Node> openedList = new ArrayList<Node>();
	public ArrayList<Node> pathList = new ArrayList<Node>();
	Node startNode, goalNode, currentNode;
	boolean goalReached = false;
	int step = 0;
	
	public PathFinder(GameWindow gp)
	{
		this.gp = gp;
		instantiateNodes();
	}
	
	public void instantiateNodes()
	{
		node = new Node[gp.maxWorldCol][gp.maxWorldRow];
		
		int col = 0;
		int row = 0;
		
		while(col < gp.maxWorldCol && row < gp.maxWorldRow)
		{
			node[col][row] = new Node(col, row); //Create a node map
			col++;
			if(col == gp.maxWorldCol)
			{
				col = 0;
				row++;
			}
		}
	}
	
	public void resetNodes()
	{
		int col = 0;
		int row = 0;
		
		while(col < gp.maxWorldCol && row < gp.maxWorldRow)
		{
			node[col][row].open = false;
			node[col][row].checked = false;
			node[col][row].solid = false;
			
			col++;
			if(col == gp.maxWorldCol)
			{
				col = 0;
				row++;
			}
		}
		
		openedList.clear();
		openList.clear();
		pathList.clear();
		goalReached = false;
		step = 0;
	}
	
	public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity Entity)
	{
		resetNodes();
		
		//Set Start and Goal node
		startNode = node[startCol][startRow];
		currentNode = startNode;
		goalNode = node[goalCol][goalRow];
		openList.add(currentNode);
		openedList.add(currentNode);
		
		int col = 0;
		int row = 0;
		
		while(row < gp.maxWorldRow)
		{
			int tileNum = gp.tileM.mapTileNum[gp.mapNum][col][row];
			if(gp.tileM.tile[tileNum].collision == true)
			{
				node[col][row].solid = true;
			}
						
			//Set cost
			getCost(node[col][row]);
			col++;
			if(col >= gp.maxWorldCol)
			{
				col = 0;
				row++;
			}
		}
	}
	
	public void getCost(Node node)
	{
		int xDistance = Math.abs(node.col - startNode.col);
		int yDistance = Math.abs(node.row - startNode.row);
		node.gCost = xDistance + yDistance;
		
		xDistance = Math.abs(node.col - goalNode.col);
		yDistance = Math.abs(node.row - goalNode.row);
		node.hCost = xDistance + yDistance;
		
		node.fCost = node.gCost + node.fCost;
	}
	
	public boolean search()
	{
		while(!goalReached && step < 500)
		{
			int col = currentNode.col;
			int row = currentNode.row;
			
			currentNode.checked = true;
			openList.remove(currentNode);
			
			if(row - 1 >= 0)
			{
				openNode(node[col][row - 1]);
			}
			
			if(col - 1 >= 0)
			{
				openNode(node[col - 1][row]);
			}
			
			if(row + 1 < gp.maxWorldRow)
			{
				openNode(node[col][row + 1]);
			}
			
			if(col + 1 < gp.maxWorldCol)
			{
				openNode(node[col + 1][row]);
			}
			
			//Find the best node
			int bestNodeIndex = 0;
			int bestNodeFCost = Integer.MAX_VALUE;
			
			for(int i = 0; i < openList.size(); i++)
			{
				if(openList.get(i).fCost < bestNodeFCost)
				{
					bestNodeIndex = i;
					bestNodeFCost = openList.get(i).fCost;
				}
				else if(openList.get(i).fCost == bestNodeFCost)
				{
					if(openList.get(i).gCost  < openList.get(bestNodeIndex).gCost)
					{
						bestNodeIndex = i;
					}
				}
			}
			
			//If there is no node in the openList, end the loop prematurely
			if(openList.size() == 0)
			{
				break;
			}
			
			currentNode = openList.get(bestNodeIndex);
			
			if(currentNode == goalNode)
			{
				goalReached = true;
				trackThePath();
			}
			step++;
		}
		
		return goalReached;
	}
	
	public void openNode(Node node)
	{
		if(!node.open && !node.checked && !node.solid)
		{
			node.open = true;
			node.parent = currentNode;
			openList.add(node);
			openedList.add(node);
		}
	}
	
	public void trackThePath()
	{
		Node current = goalNode;
		
		while(current != startNode)
		{
			pathList.add(0, current);
			current = current.parent;
		}
	}
}
