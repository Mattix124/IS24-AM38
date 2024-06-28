package it.polimi.ingsw.am38.Network.Packet;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
/**
 * Class that stores information needed to resend data to a player
 * who has reconnected after a disconnection
 */
public class PlayerDisconnectionResendInfo implements Serializable
{
	@Serial
	private static final long serialVersionUID = 654687354;
	/**
	 * List representing the state of cards placed by the player at the time of disconnection
	 */
	private final LinkedList <CardPlacedInfo> disconnectionDataCard;
	/**
	 * Points scored before the disconnection
	 */
	private final int points;
	/**
	 * List of kingdoms and card types of the hand of the player disconnected
	 */
	private final String[] handColor;
	/**
	 * attribute containing all the visible Symbols on a Player's field
	 */
	private final VisibleElements symTab;
	/**
	 * Attribute that assign the color to the reconnected player
	 */
	private final Color color;

	/**
	 * Constructor method the set the attribute needed
	 * @param disconnectionDataCard list of card placed before the disconnection
	 * @param points points scored by the player before the disconnection
	 * @param handColor list of kingdoms and card types of the hand of the player disconnected
	 */
	public PlayerDisconnectionResendInfo(LinkedList <CardPlacedInfo> disconnectionDataCard, int points, String[] handColor, VisibleElements symTab, Color color)
	{
		this.disconnectionDataCard = disconnectionDataCard;
		this.points = points;
		this.handColor = handColor;
        this.symTab = symTab;
		this.color = color;
    }

	/**
	 * Getter for the card placed before the disconnection
	 * @return a list with info about the cards placed
	 */
	public LinkedList <CardPlacedInfo> getDisconnectionDataCard()
	{
		return disconnectionDataCard;
	}

	/**
	 * Getter for the points of the player at the time of the disconnection
	 * @return points scored by the player
	 */
	public int getPoints()
	{
		return points;
	}

	/**
	 * Getter for the kingdom of the hand at the time of disconnection
	 * @return an array containing strings that represents the kingdom and the type of the card(gold or resource)
	 */
	public String[] getHandColor() {
		return handColor;
	}

	/**
	 * Getter for the symbol's tab
	 * @return symbol's tab
	 */
	public VisibleElements getSymTab() {
		return symTab;
	}

	/**
	 * Getter for color
	 * @return color
	 */
	public Color getColor() {
		return color;
	}
}
