package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;
import it.polimi.ingsw.am38.Network.Packet.PlayerDisconnectionResendInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that allow the information of a game to travel to a reconnecting client
 */
public class MReconnectionInfo extends MessageContent implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1754545354768L;
	/**
	 * Nickname of the player
	 */
	private final String nickname;
	/**
	 * Hashmap containing the gameField of every player
	 */
	private final HashMap <String, PlayerDisconnectionResendInfo> playersInfo;
	/**
	 * Array of int that contains all the cards in your hand
	 */
	private final int[] ownHand = new int[3];
	/**
	 * Ids of the top card of the deck
	 */
	private final int upGc1, upGc2, upRc1, upRc2;
	/**
	 * Symbols of the top 2 cards on the deck
	 */
	private final Symbol goldTop, resourceTop;
	/**
	 * Attributes that contains all the objective in the game
	 */
	private final int sharedObj1, sharedObj2, personalObj;

	/**
	 * Constructor
	 * @param playersInfo field info
	 * @param ownHand hand info
	 * @param nickname nickname
	 * @param upGc1 id gold 1
	 * @param upGc2 id gold 2
	 * @param upRc1 id resource 1
	 * @param upRc2 id resource 2
	 * @param goldTop symbol gold top
	 * @param resourceTop symbol resource top
	 * @param sharedObj1 public objective 1
	 * @param sharedObj2 public objective 2
	 * @param personalObj private objective
	 */
	public MReconnectionInfo(HashMap <String, PlayerDisconnectionResendInfo> playersInfo, ArrayList<PlayableCard> ownHand, String nickname, int upGc1, int upGc2, int upRc1, int upRc2, Symbol goldTop, Symbol resourceTop, int sharedObj1, int sharedObj2, int personalObj)
	{
		this.playersInfo = playersInfo;
        this.nickname = nickname;
        this.sharedObj1 = sharedObj1;
        this.sharedObj2 = sharedObj2;
        this.personalObj = personalObj;
        for(int i = 0 ; i < 3 ; i++)
			this.ownHand[i] = ownHand.get(i).getCardID();
        this.upGc1 = upGc1;
        this.upGc2 = upGc2;
        this.upRc1 = upRc1;
        this.upRc2 = upRc2;
        this.goldTop = goldTop;
        this.resourceTop = resourceTop;

    }

	/**
	 * Getter of players fields
	 * @return hashmap of players fields
	 */
	public HashMap <String, PlayerDisconnectionResendInfo> getPlayersInfo()
	{
		return playersInfo;
	}

	/**
	 * Getter of player's hand
	 * @return the array of the player's hand
	 */
	public int[] getOwnHand()
	{
		return ownHand;
	}
	/**
	 * Getter of the id of the card
	 * @return gold 1 id
	 */
	public int getUpGc1() {
		return upGc1;
	}
	/**
	 * Getter of the id of the card
	 * @return gold 2 id
	 */
	public int getUpGc2() {
		return upGc2;
	}
	/**
	 * Getter of the id of the card
	 * @return resource 1 id
	 */
	public int getUpRc1() {
		return upRc1;
	}
	/**
	 * Getter of the id of the card
	 * @return resource 2 id
	 */
	public int getUpRc2() {
		return upRc2;
	}
	/**
	 * Getter of the symbol of the card
	 * @return gold top symbol
	 */
	public Symbol getGoldTop() {
		return goldTop;
	}
	/**
	 * Getter of the symbol of the card
	 * @return resource top symbol
	 */
	public Symbol getResourceTop() {
		return resourceTop;
	}
	/**
	 * Getter of the nickname
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * Getter of the first public objective id
	 * @return the id
	 */
	public int getSharedObj1() {
		return sharedObj1;
	}
	/**
	 * Getter of the second public objective id
	 * @return the id
	 */
	public int getSharedObj2() {
		return sharedObj2;
	}
	/**
	 * Getter of the private objective id
	 * @return the id
	 */
	public int getPersonalObj() {
		return personalObj;
	}
}
