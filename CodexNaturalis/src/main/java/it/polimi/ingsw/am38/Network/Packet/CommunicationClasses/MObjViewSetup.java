package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class MObjViewSetup extends MessageContent implements Serializable
{
	@Serial
	private static final long serialVersionUID = 2354354768L;
	/**
	 * Array of he objective that will be visible on client side
	 */
	int[] objectives = new int[4];
	/**
	 * id of the cards in your hand
	 */
	private final int[] firstHand = new int[3];
	/**
	 * Symbol of the top gold deck
	 */
	private final Symbol topG;
	/**
	 * Symbol of the top resource deck
	 */
	private final Symbol topR;
	/**
	 * Hashmap that contains all the color code string for the cards in opponent hands
	 */
	private final HashMap <String, String[]> handsColors = new HashMap <>();
	/**
	 * Hashmap that contains all the facing of the starter cards of the players
	 */
	private final HashMap <String, Boolean> starterFacings = new HashMap <>();
	/**
	 * Hashmap that contains all the color of the players
	 */
	private final HashMap <String, Color> playersColors = new HashMap <>();
	/**
	 * Hashmap that contains all the symbol's tab associated with every player
	 */
	private final HashMap<String, VisibleElements> playersVisibleElements = new HashMap<>();
	/**
	 * String that will be displayed in the cli view
	 */
	private final String[] strings;

	/**
	 * Constructor of MObjViewSetup
	 * @param gc GameController
	 * @param p Player
	 */
	public MObjViewSetup(GameController gc, Player p){
		objectives[0] = gc.getGame().getSharedObjectiveCards().get(0).getCardID();
		objectives[1] = gc.getGame().getSharedObjectiveCards().get(1).getCardID();
		objectives[2] = p.getPair().get(0).getCardID();
		objectives[3] = p.getPair().get(1).getCardID();
		this.topG = gc.getGame().getGoldDeck().getTopCardKingdom();
		this.topR = gc.getGame().getResourceDeck().getTopCardKingdom();
		this.firstHand[0] = p.getHand().getCard(0).getCardID();
		this.firstHand[1] = p.getHand().getCard(1).getCardID();
		this.firstHand[2] = p.getHand().getCard(2).getCardID();
		this.handsColors.putAll(gc.getGame().getPlayersCardsColors());
		this.starterFacings.putAll(gc.getGame().getPlayersStarterFacing());
		this.playersColors.putAll(gc.getGame().getPlayersColors());
		this.strings = new String[]{"You have drawn 2 Resource Card, 1 Gold Card","Chose one of them: type 'obj' and a number (1 or 2)"};
		starterFacings.forEach((k, v) ->
				this.playersVisibleElements.put(k, gc.getGame().getPlayers().stream()
						.filter(x -> x.getNickname().equals(k))
						.map(x-> x.getGameField().getVisibleElements())
						.toList()
						.getFirst())
		);
	}

	/**
	 * Getter of the objective array
	 * @return objective array
	 */
	public int[] getObjectives()
	{
		return objectives;
	}
	/**
	 * Getter of the objective array
	 * @return objective array
	 */
	public int[] getFirstHand()
	{
		return firstHand;
	}

	/**
	 * Getter of the hand color hashmap
	 * @return handColor's hashmap
	 */
	public HashMap<String, String[]> getHandsColors()
	{
		return handsColors;
	}
	/**
	 * Getter of the starterCard's facing Hashmap
	 * @return facing's hashmap
	 */
	public HashMap <String, Boolean> getStarterFacings()
	{
		return starterFacings;
	}
	/**
	 * Getter of the color's facing Hashmap
	 * @return color's Hashmap
	 */
	public HashMap <String, Color> getPlayersColors()
	{
		return playersColors;
	}

	/**
	 * Getter of the Strings array
	 * @param i index of string
	 * @return the string desired
	 */
	public String getString(int i)
	{
		return strings[i];
	}

	/**
	 * Getter of the visibleElement's Hashmap
	 * @return Symbol's tab
	 */
	public HashMap<String, VisibleElements> getPlayersVisibleElements(){
		return this.playersVisibleElements;
	}
	/**
	 * Getter of the top gold deck card symbol
	 * @return top gold card symbol
	 */
	public Symbol getTopG() {
		return topG;
	}
	/**
	 * Getter of the top resource deck card symbol
	 * @return top resource card symbol
	 */
	public Symbol getTopR() {
		return topR;
	}
}