package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Hand;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.util.ArrayList;
import java.util.HashMap;

public class MFirstHandSetup extends MessageContent
{
	ArrayList<String> objectives = new ArrayList<>(4);
	private final int[] firstHand = new int[3];
	private final HashMap<String, Symbol[]> handsColors = new HashMap<>();
	private final HashMap<String, Boolean> starterFacings = new HashMap<>();
	private final HashMap<String, Color> playersColors = new HashMap<>();

	public MFirstHandSetup(String objS1, String objS2, String objP1, String objP2, Hand hand, HashMap<String, Symbol[]> colors, HashMap<String, Boolean> facings, HashMap<String, Color> pColors) {
		objectives.add(objS1);
		objectives.add(objS2);
		objectives.add(objP1);
		objectives.add(objP2);
		this.firstHand[0] = hand.getCard(0).getCardID();
		this.firstHand[1] = hand.getCard(1).getCardID();
		this.firstHand[2] = hand.getCard(2).getCardID();
		this.handsColors.putAll(colors);
		this.starterFacings.putAll(facings);
		this.playersColors.putAll(pColors);
	}

	public ArrayList<String > getObjectives()
	{
		return objectives;
	}

	public int[] getFirstHand()
	{
		return firstHand;
	}

	public HashMap<String, Symbol[]> getHandsColors() {
		return handsColors;
	}

	public HashMap<String, Boolean> getStarterFacings() {
		return starterFacings;
	}
	public 	HashMap<String, Color> getPlayersColors(){
		return playersColors;
	}
}
