package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.util.HashMap;

/**
 * Class used to send the 4 Nicknames of each Player and their randomly drawn StarterCards
 */
public class MStringCard extends MessageContent{
	/**
	 * The id HashMap of all Player's Nicknames and StarterCards
	 */
	private HashMap<String, Integer> starterCards = new HashMap<>();
	private Symbol goldTop;
	private Symbol resourceTop;
	private int[] goldGrounud = new int[2];
	private int[] resourceGround = new int[2];

	/**
	 * Constructor of MStringCard
	 */
	public MStringCard(HashMap<String, Integer> sc, Symbol gt, Symbol rt, int[] gs, int[] rs){
		this.starterCards = new HashMap<>(sc);
		this.goldTop = gt;
		this.resourceTop = rt;
		this.goldGrounud = gs;
		this.resourceGround = rs;
	}

	/**
	 * Getter for the HashMap of all Player's Nicknames and StarterCards
	 * @return all Nicknames and the StarterCard each Player randomly drew
	 */
	public HashMap<String, Integer> getStarterCards(){
		return this.starterCards;
	}

	public Symbol getGoldTop() {
		return goldTop;
	}

	public Symbol getResourceTop() {
		return resourceTop;
	}

	public int[] getGoldGrounud() {
		return goldGrounud;
	}

	public int[] getResourceGround() {
		return resourceGround;
	}
}
