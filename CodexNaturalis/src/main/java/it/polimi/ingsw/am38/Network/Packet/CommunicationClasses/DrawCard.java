package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

public class DrawCard extends MessageContent
{
	private final String deck;
	private final int index;

	public DrawCard(String deck, int index)
	{
		this.deck = deck;
		this.index = index;
	}

	public String getDeck()
	{
		return deck;
	}

	public int getIndex()
	{
		return index;
	}
}
