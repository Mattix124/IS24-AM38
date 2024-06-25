package it.polimi.ingsw.am38.View.GuiSupporDataClasses;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;

import java.util.LinkedList;

public class DeckandHand
{
	private Symbol goldT;
	private Symbol resT;
	private GoldCard gold1;
	private GoldCard gold2;
	private ResourceCard res1;
	private ResourceCard res2;
	private LinkedList <PlayableCard> hand;
	private String[] otherHands;
	private String nickname;

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

	public String[] getOtherHands()
	{
		return otherHands;
	}

	public String getNickname()
	{
		return nickname;
	}

	public Symbol getGoldT()
	{
		return goldT;
	}

	public Symbol getResT()
	{
		return resT;
	}

	public GoldCard getGold1()
	{
		return gold1;
	}

	public GoldCard getGold2()
	{
		return gold2;
	}

	public ResourceCard getRes1()
	{
		return res1;
	}

	public ResourceCard getRes2()
	{
		return res2;
	}

	public LinkedList <PlayableCard> getHand()
	{
		return hand;
	}

}
