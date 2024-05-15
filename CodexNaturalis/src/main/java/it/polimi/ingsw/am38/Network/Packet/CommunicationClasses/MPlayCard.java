package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serializable;
/**
 * Class that allow the sending of information about the played card inside Message Class
 */
public class MPlayCard extends MessageContent implements Serializable
{
	private static final long serialVersionUID = 1344375768L;
	/**
	 * Position of the card chosen in the player's hand
	 */
	private final int handIndex;
	/**
	 * The coordinates chosen for the placement
	 */
	private final Coords coords;
	/**
	 * face chosen
	 */
	private final String facing;

	/**
	 * Constructor of MPlayCard
	 * @param handIndex index of card
	 * @param coords coordinates for placement
	 * @param facing face chosen
	 */
	public MPlayCard(int handIndex, Coords coords, String facing)
	{
		this.handIndex = handIndex;
		this.coords = coords;
		this.facing = facing;
	}

	/**
	 * Get the index of the hand
	 * @return the index
	 */
	public int getHandIndex()
	{
		return handIndex;
	}
	/**
	 * Get the coordinates
	 * @return the coordinates
	 */
	public Coords getCoords()
	{
		return coords;
	}
	/**
	 * Get the face chosen
	 * @return the face
	 */
	public String getFacing()
	{
		return facing;
	}
}
