package it.polimi.ingsw.am38.Model.Board;

import java.util.LinkedList;

/**
 * This class helps the Field class to count the 3 needed card for the placement procedure.
 */
public class EnteredCardControl
{
	/**
	 * This attributes contains how many of the 3 cards are presents.
	 */
	private int enteredCard;

	/**
	 * This attributes contains how many of the angle of the 3 cards are presents.
	 */
	private int checkedAngle;

	/**
	 * The constructor of a EnteredCardControl Class that initialize every parameter to 0.
	 */
	public EnteredCardControl()
	{
		this.enteredCard = 0;
		this.checkedAngle = 0;
	}

	/**
	 * Get the number of entered card.
	 *
	 * @return the value.
	 */
	public int getEnteredCard()
	{
		return enteredCard;
	}

	/**
	 * Increase the number of cards presents.
	 */
	public void increaseEnteredCard()
	{
		enteredCard++;
	}

	/**
	 * Get the number of angle presents in the cards entered.
	 *
	 * @return the value.
	 */
	public int getCheckedAngle()
	{
		return checkedAngle;
	}

	/**
	 * Increase the number of angle presents in the cards entered.
	 */
	public void increaseCheckedAngle()
	{
		checkedAngle++;
	}
}
