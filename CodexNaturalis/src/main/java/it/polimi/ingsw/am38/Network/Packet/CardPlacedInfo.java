package it.polimi.ingsw.am38.Network.Packet;

import java.io.Serial;
import java.io.Serializable;

public class CardPlacedInfo implements Serializable
{
	@Serial
	private static final long serialVersionUID = 175434568754768L;

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
