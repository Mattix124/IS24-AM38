package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

public class PlayCard extends MessageContent
{
	private final int handIndex;
	private final Coords coords;
	private final String facing;

	public PlayCard(int handIndex, Coords coords, String facing)
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
