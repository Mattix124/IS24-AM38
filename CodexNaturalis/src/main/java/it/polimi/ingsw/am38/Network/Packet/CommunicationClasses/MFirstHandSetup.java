package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Hand;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.util.LinkedList;

public class MFirstHandSetup extends MessageContent
{
	private final String ObjS1;
	private final String ObjS2;
	private final String ObjP1;
	private final String ObjP2;
	private final int[] firstHand;

	public MFirstHandSetup(String objS1, String objS2, String objP1, String objP2, Hand hand)
	{
		ObjS1 = objS1;
		ObjS2 = objS2;
		ObjP1 = objP1;
		ObjP2 = objP2;
		firstHand = new int[3];
		this.firstHand[0] = hand.getCard(0).getCardID();
		this.firstHand[1] = hand.getCard(1).getCardID();
		this.firstHand[2] = hand.getCard(2).getCardID();
	}

	public String getObjS1()
	{
		return ObjS1;
	}

	public String getObjS2()
	{
		return ObjS2;
	}

	public String getObjP1()
	{
		return ObjP1;
	}

	public String getObjP2()
	{
		return ObjP2;
	}

	public int[] getFirstHand()
	{
		return firstHand;
	}
}
