package it.polimi.ingsw.am38.Model.Board;

public class Coords //Simple pair of int that describes the position of a card (highly bounded with PlayableCard in the DataStructure)
{
	private int x;
	private int y;

	public Coords(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int x()
	{
		return x;
	}

	public int y()
	{
		return y;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public void setY(int y)
	{
		this.y = y;
	}

}
