package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

/**
 * Class that allow the sending of all possible placement inside Message Class
 */
public class MConfirmedPlacement extends MessageContent
{
	private final int id;
	private final int x;
	private final int y;
	private final boolean face;

	public MConfirmedPlacement(int id, int x, int y, boolean face)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.face = face;
	}

	public int getId()
	{
		return id;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public boolean isFace()
	{
		return face;
	}
}
