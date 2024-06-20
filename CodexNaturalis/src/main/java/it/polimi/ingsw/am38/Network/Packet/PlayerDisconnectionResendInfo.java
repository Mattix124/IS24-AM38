package it.polimi.ingsw.am38.Network.Packet;

import it.polimi.ingsw.am38.Enum.Symbol;

import java.util.LinkedList;

public class PlayerDisconnectionResendInfo
{
	private LinkedList <CardPlacedInfo> disconnectionDataCard;
	private int points;
	private Symbol[] handColor;

	public PlayerDisconnectionResendInfo(LinkedList <CardPlacedInfo> disconnectionDataCard, int points, Symbol[] handColor)
	{
		this.disconnectionDataCard = disconnectionDataCard;
		this.points = points;
		this.handColor = handColor;
	}

	public LinkedList <CardPlacedInfo> getDisconnectionDataCard()
	{
		return disconnectionDataCard;
	}

	public int getPoints()
	{
		return points;
	}

	public Symbol[] getHandColor()
	{
		return handColor;
	}
}
