package it.polimi.ingsw.am38.DataStruture;

public class Coords //Symple pair of int that describes the position of a card (highly bounded with PlayableCard in the DataStructure)
{
	private final int x;
	private final int y;

	public Coords(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
}
