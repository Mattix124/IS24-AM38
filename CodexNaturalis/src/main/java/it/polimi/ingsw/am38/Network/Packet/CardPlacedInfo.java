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
	private final int id;
	/**
	 * coordinates at which the card has been played
	 */
	private final int x, y;
	/**
	 * Face chosen during the placement of the card
	 */
	private final boolean face;

	/**
	 * Constructor method that set the id and the face chosen after the placement
	 * of the card
	 *
	 * @param id of the card
	 * @param face chosen of the card
	 */
	public CardPlacedInfo(int id, boolean face, int x, int y)
	{
		this.x = x - y;
		this.y = x + y;
		this.id = id;
		this.face = face;
	}

	/**
	 * Getter for the ID
	 * @return an int containing the card ID
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Getter for the face
	 * @return a boolean representing the face (true = up, false = down)
	 */
	public boolean isFace()
	{
		return face;
	}

	/**
	 * getter for the x coordinate
	 * @return an int containing the coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * getter for the y coordinate
	 * @return an int containing the coordinate
	 */
	public int getY() {
		return y;
	}
}
