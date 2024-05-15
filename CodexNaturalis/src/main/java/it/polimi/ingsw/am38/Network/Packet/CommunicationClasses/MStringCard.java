package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

/**
 * Class used to send a message with a card inside
 */
public class MStringCard extends MessageContent
{
	/**
	 * The message
	 */
	private String text;
	/**
	 * The id of the card
	 */
	private int id;

	/**
	 * Constructor of MStringCard
	 * @param text the text that will be displayed
	 * @param id the id of the card
	 */
	public MStringCard(String text, int id)
	{
		this.text = text;
		this.id = id;
	}

	/**
	 * Getter for the text
	 * @return text
	 */
	public String getText()
	{
		return text;
	}
	/**
	 * Getter for the id
	 * @return id
	 */
	public int getId()
	{
		return id;
	}
}
