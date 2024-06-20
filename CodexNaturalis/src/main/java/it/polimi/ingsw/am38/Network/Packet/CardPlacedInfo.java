package it.polimi.ingsw.am38.Network.Packet;

public class CardPlacedInfo
{
	private int id;
	private boolean face;

	public CardPlacedInfo(int id, boolean face)
	{
		this.id = id;
		this.face = face;
	}

	public int getId()
	{
		return id;
	}

	public boolean isFace()
	{
		return face;
	}
}
