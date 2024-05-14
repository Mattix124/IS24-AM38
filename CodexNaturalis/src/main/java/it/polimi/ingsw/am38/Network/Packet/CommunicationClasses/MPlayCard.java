package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serializable;

public class MPlayCard extends MessageContent implements Serializable
{
	private static final long serialVersionUID = 1344375768L;

	private final int handIndex;
	private final Coords coords;
	private final String facing;

	public MPlayCard(int handIndex, Coords coords, String facing)
	{
		this.handIndex = handIndex;
		this.coords = coords;
		this.facing = facing;
	}

	public int getHandIndex()
	{
		return handIndex;
	}

	public Coords getCoords()
	{
		return coords;
	}

	public String getFacing()
	{
		return facing;
	}
}
