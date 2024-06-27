package it.polimi.ingsw.am38.View.GuiSupporDataClasses;

import it.polimi.ingsw.am38.Model.Cards.PlayableCard;

/**
 * Class that contains the info of a card placed
 */
public class GuiPlacedConfirm
{
	/**
	 * Nickname of the player
	 */
	private final String nick;
	/**
	 * Card placed
	 */
	private final PlayableCard card;
	/**
	 * X coordinates
	 */
	private final int x;
	/**
	 * Y coordinates
	 */
	private final int y;

	/**
	 * Constructor method that set the coordinates, the nickname and the card placed
	 *
	 * @param nick of the player
	 * @param card placed
	 * @param x coordinates
	 * @param y coordinates
	 */
	public GuiPlacedConfirm(String nick, PlayableCard card, int x, int y)
	{
		this.nick = nick;
		this.card = card;
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter for the attribute nick
	 *
	 * @return nick
	 */
	public String getNick()
	{
		return nick;
	}

	/**
	 * Getter for the attribute card
	 *
	 * @return card
	 */
	public PlayableCard getCard()
	{
		return card;
	}

	/**
	 * Getter for the attribute x
	 *
	 * @return x
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Getter for the attribute y
	 *
	 * @return y
	 */
	public int getY()
	{
		return y;
	}
}
