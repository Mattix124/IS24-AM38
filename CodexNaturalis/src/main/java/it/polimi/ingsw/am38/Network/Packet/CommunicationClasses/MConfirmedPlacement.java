package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that allow the sending of all possible placement inside Message Class
 */
public class MConfirmedPlacement extends MessageContent implements Serializable
{
	@Serial
	private static final long serialVersionUID = 698755425L;

	private final int id;
	private final int x;
	private final int y;
	private final int points;
	private final boolean face;
	private final VisibleElements visibleElements;

	public MConfirmedPlacement(int id, int x, int y, boolean face, int points, VisibleElements visibleElements)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.face = face;
		this.points = points;
		this.visibleElements = visibleElements;
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

	public int getPoints()
	{
		return points;
	}

	public VisibleElements getVisibleElements()
	{
		return visibleElements;
	}
}
