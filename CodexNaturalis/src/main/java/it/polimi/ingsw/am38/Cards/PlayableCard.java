package it.polimi.ingsw.am38.Cards;

public abstract class PlayableCard extends Card
{

	private Corner corners;
	private boolean orientation;

	public boolean checkPlacement()
	{
		return false;   //momentary
	}

	public Corner getCorner()
	{
		return null;    //momentary
	}
}
