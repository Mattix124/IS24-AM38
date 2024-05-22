package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.util.HashMap;

/**
 * Class used to send the 4 Nicknames of each Player and their randomly drawn StarterCards
 */
public class MStringCard extends MessageContent
{
	/**
	 * The id HashMap of all Player's Nicknames and StarterCards
	 */
	private HashMap <String, Integer> starterCards = new HashMap <>();
	private final Symbol goldTop;
	private final Symbol resourceTop;
	private final int[] goldGrounud;
	private final int[] resourceGround;
	/**
	 * Constructor of MStringCard
	 */
	public MStringCard(GameController gc)
	{

		this.starterCards = gc.getGame().getNicksAndStartersIDs();
		this.goldTop = gc.getGame().getGoldDeck().getTopCardKingdom();
		this.resourceTop = gc.getGame().getResourceDeck().getTopCardKingdom();
		this.goldGrounud = gc.getGame().getGoldDeck().getGroundCards();
		this.resourceGround = gc.getGame().getResourceDeck().getGroundCards();
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

	public Symbol getGoldTop()
	{
		return goldTop;
	}

	public Symbol getResourceTop()
	{
		return resourceTop;
	}

	public int[] getGoldGround()
	{
		return goldGrounud;
	}

	public int[] getResourceGround()
	{
		return resourceGround;
	}
}
