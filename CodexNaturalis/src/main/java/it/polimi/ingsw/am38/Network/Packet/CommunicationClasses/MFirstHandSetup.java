package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Hand;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.util.ArrayList;
import java.util.LinkedList;

public class MFirstHandSetup extends MessageContent
{
	ArrayList<String> objectives = new ArrayList<>(4);
	private final int[] firstHand = new int[3];

	public MFirstHandSetup(String objS1, String objS2, String objP1, String objP2, Hand hand)
	{
		objectives.add(objS1);
		objectives.add(objS2);
		objectives.add(objP1);
		objectives.add(objP2);
		this.firstHand[0] = hand.getCard(0).getCardID();
		this.firstHand[1] = hand.getCard(1).getCardID();
		this.firstHand[2] = hand.getCard(2).getCardID();
	}

	public ArrayList<String > getObjectives()
	{
		return objectives;
	}

	public int[] getFirstHand()
	{
		return firstHand;
	}
}
