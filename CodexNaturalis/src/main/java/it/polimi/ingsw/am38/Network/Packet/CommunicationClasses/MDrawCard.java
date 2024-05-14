package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serializable;

public class MDrawCard extends MessageContent implements Serializable
{
	private static final long serialVersionUID = 17523768L;

	private final String deck;
	private final int index;

	public MDrawCard(String deck, int index)
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
