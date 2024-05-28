package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that allow the sending of card data inside Message Class
 */
public class MDrawCard extends MessageContent implements Serializable
{
	@Serial
	private static final long serialVersionUID = 17523768L;
	/**
	 * It contains the origin of the card that will be drawn
	 */
	private final String deck;
	/**
	 * It contains the location of the card that will be drawn
	 */
	private final int index;

	/**
	 * Constructor of MDrawCard
	 * @param deck origin
	 * @param index location
	 */
	public MDrawCard(String deck, int index)
	{
		this.deck = deck;
		this.index = index;
	}

	/**
	 * Getter for origin
	 * @return origin
	 */
	public String getDeck()
	{
		return deck;
	}
	/**
	 * Getter for location
	 * @return location
	 */
	public int getIndex()
	{
		return index;
	}
}
