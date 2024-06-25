package it.polimi.ingsw.am38.Network.Packet;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that represents information about a card that has been placed on the field
 */
public class CardPlacedInfo implements Serializable
{
	@Serial
	private static final long serialVersionUID = 175434568754768L;
	/**
	 * ID of the card placed
	 */
	private int id;
	/**
	 * Face on which the card has been placed
	 */
	private boolean face;

	/**
	 * Constructor method that set the id and the face chosen after the placement
	 * of the card
	 *
	 * @param id of the card
	 * @param face chosen of the card
	 */
	public CardPlacedInfo(int id, boolean face)
	{
		this.id = id;
		this.face = face;
	}

	/**
	 * Getter for the ID
	 *
	 * @return
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Getter for the face
	 *
	 * @return
	 */
	public boolean isFace()
	{
		return face;
	}
}
