package it.polimi.ingsw.am38.View.GuiSupporDataClasses;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;

import java.util.LinkedList;

/**
 * Class that contains the info of the hand of the player and the decks
 */
public class DeckandHand
{
	/**
	 * Cards on top of the gold deck
	 */
	private Symbol goldT;
	/**
	 * Cards on top of the resource deck
	 */
	private Symbol resT;
	/**
	 * Gold card on the ground
	 */
	private GoldCard gold1;
	/**
	 * Gold card on the ground
	 */
	private GoldCard gold2;
	/**
	 * Resource card on the ground
	 */
	private ResourceCard res1;
	/**
	 * Resource card on the ground
	 */
	private ResourceCard res2;
	/**
	 * List that contains the cards in the hand
	 */
	private LinkedList <PlayableCard> hand;
	/**
	 * String that contains the hands of the other players
	 */
	private String[] otherHands;
	/**
	 * Nickname of the player
	 */
	private String nickname;

	/**
	 * Constructor method that set the info
	 *
	 * @param goldT gold card on the ground
	 * @param resT resource card on the ground
	 * @param gold1 gold card on the ground
	 * @param gold2 gold card on the ground
	 * @param res1 resource card on the ground
	 * @param res2 resource card on the ground
	 * @param hand hands of the other players
	 */
	public DeckandHand(Symbol goldT, Symbol resT, GoldCard gold1, GoldCard gold2, ResourceCard res1, ResourceCard res2, LinkedList <PlayableCard> hand)
	{
		this.goldT = goldT;
		this.resT = resT;
		this.gold1 = gold1;
		this.gold2 = gold2;
		this.res1 = res1;
		this.res2 = res2;
		this.hand = hand;
	}

	/**
	 * Another constructor method used in a different situation
	 *
	 * @param nickname of the player
	 * @param gfu1 gold card on the ground
	 * @param gfu2 gold card on the ground
	 * @param rfu1 resource card on the ground
	 * @param rfu2 resource card on the ground
	 * @param gtc gold card on the ground
	 * @param rtc resource card on the ground
	 * @param hcc hands of the other players
	 */
	public DeckandHand(String nickname, GoldCard gfu1, GoldCard gfu2, ResourceCard rfu1, ResourceCard rfu2, Symbol gtc, Symbol rtc, String[] hcc)
	{
		this.goldT = gtc;
		this.resT = rtc;
		this.gold1 = gfu1;
		this.gold2 = gfu2;
		this.res1 = rfu1;
		this.res2 = rfu2;
		this.otherHands = hcc;
		this.nickname = nickname;
	}

	/**
	 * Getter for the attribute otherHands
	 *
	 * @return otherHands
	 */
	public String[] getOtherHands()
	{
		return otherHands;
	}

	/**
	 * Getter for the attribute nickname
	 *
	 * @return nickname
	 */
	public String getNickname()
	{
		return nickname;
	}

	/**
	 * Getter for the attribute goldT
	 *
	 * @return goldT
	 */
	public Symbol getGoldT()
	{
		return goldT;
	}

	/**
	 * Getter for the attribute resT
	 *
	 * @return resT
	 */
	public Symbol getResT()
	{
		return resT;
	}

	/**
	 * Getter for the attribute gold1
	 *
	 * @return gold1
	 */
	public GoldCard getGold1()
	{
		return gold1;
	}

	/**
	 * Getter for the attribute gold2
	 *
	 * @return gold2
	 */
	public GoldCard getGold2()
	{
		return gold2;
	}

	/**
	 * Getter for the attribute res1
	 *
	 * @return res1
	 */
	public ResourceCard getRes1()
	{
		return res1;
	}

	/**
	 * Getter for the attribute res2
	 *
	 * @return res2
	 */
	public ResourceCard getRes2()
	{
		return res2;
	}

	/**
	 * Getter for the attribute hand
	 *
	 * @return hand
	 */
	public LinkedList <PlayableCard> getHand()
	{
		return hand;
	}

}
