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

	int[] objectives = new int[4];
	private final int[] firstHand = new int[3];
	private final Symbol topG;
	private final Symbol topR;
	private final HashMap <String, String[]> handsColors = new HashMap <>();
	private final HashMap <String, Boolean> starterFacings = new HashMap <>();
	private final HashMap <String, Color> playersColors = new HashMap <>();
	private final HashMap<String, VisibleElements> playersVisibleElements = new HashMap<>();
	private final String[] strings;

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

	public int[] getObjectives()
	{
		return objectives;
	}

	public int[] getFirstHand()
	{
		return firstHand;
	}

	public HashMap<String, String[]> getHandsColors()
	{
		return handsColors;
	}

	public HashMap <String, Boolean> getStarterFacings()
	{
		return starterFacings;
	}

	public HashMap <String, Color> getPlayersColors()
	{
		return playersColors;
	}

	public String getString(int i)
	{
		return strings[i];
	}
	public HashMap<String, VisibleElements> getPlayersVisibleElements(){
		return this.playersVisibleElements;
	}

	public Symbol getTopG() {
		return topG;
	}

	public Symbol getTopR() {
		return topR;
	}
}