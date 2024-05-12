package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

public class MCoords extends MessageContent
{
	private final Coords coords;

	public MCoords(int x, int y)
	{
		this.coords = new Coords(x, y);
	}

	public Coords getCoords()
	{
		return coords;
	}
}
