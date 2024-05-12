package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.util.LinkedList;

public class Placement extends MessageContent
{
	private final LinkedList <Coords> placements;

	public Placement(LinkedList <Coords> placements)
	{
		this.placements = placements;
	}

	public LinkedList <Coords> getPlacements()
	{
		return placements;
	}
}
