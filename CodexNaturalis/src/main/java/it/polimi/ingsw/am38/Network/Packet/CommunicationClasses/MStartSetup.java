package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Class used to send the 4 Nicknames of each Player and their randomly drawn StarterCards
 */
public class MStartSetup extends MessageContent implements Serializable
{
	@Serial
	private static final long serialVersionUID = 68718754768L;

	/**
	 * The id HashMap of all Player's Nicknames and StarterCards
	 */
	private final HashMap <String, Integer> starterCards;
	/**
	 * Symbol of the top gold card on the deck
	 */
	private final Symbol goldTop;
	/**
	 * Symbol of the top resource card on the deck
	 */
	private final Symbol resourceTop;
	/**
	 * Int Array containing the id of the gold ground card
	 */
	private final int[] goldGround = new int[2];
	/**
	 * Int Array containing the id of the resource ground card
	 */
	private final int[] resourceGround = new int[2];
	/**
	 * Constructor of MStringCard
	 */
	public MStartSetup(GameController gc)
	{

		this.starterCards = gc.getGame().getNicksAndStartersIDs();
		this.goldTop = gc.getGame().getGoldDeck().getTopCardKingdom();
		this.resourceTop = gc.getGame().getResourceDeck().getTopCardKingdom();
		this.goldGround[0] = gc.getGame().getGoldDeck().getGroundCards()[0].getCardID();
		this.goldGround[1] = gc.getGame().getGoldDeck().getGroundCards()[1].getCardID();
		this.resourceGround[0] = gc.getGame().getResourceDeck().getGroundCards()[0].getCardID();
		this.resourceGround[1] = gc.getGame().getResourceDeck().getGroundCards()[1].getCardID();
	}

	/**
	 * Getter for the HashMap of all Player's Nicknames and StarterCards
	 *
	 * @return all Nicknames and the StarterCard each Player randomly drew
	 */
	public HashMap <String, Integer> getStarterCards()
	{
		return this.starterCards;
	}

	/**
	 * Getter for the symbol of the card on top of the gold deck
	 * @return the symbol
	 */
	public Symbol getGoldTop()
	{
		return goldTop;
	}
	/**
	 * Getter for the symbol of the card on top of the resource deck
	 * @return the symbol
	 */
	public Symbol getResourceTop()
	{
		return resourceTop;
	}

	/**
	 * Getter for the array of the ground gold cards
	 * @return the array
	 */
	public int[] getGoldGround()
	{
		return goldGround;
	}
	/**
	 * Getter for the array of the ground resource cards
	 * @return the array
	 */
	public int[] getResourceGround()
	{
		return resourceGround;
	}
}
