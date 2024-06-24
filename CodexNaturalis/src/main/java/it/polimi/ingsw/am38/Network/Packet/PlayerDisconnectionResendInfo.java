package it.polimi.ingsw.am38.Network.Packet;

import it.polimi.ingsw.am38.Enum.Symbol;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;

public class PlayerDisconnectionResendInfo implements Serializable
{
	@Serial
	private static final long serialVersionUID = 654687354;

	private final LinkedList <CardPlacedInfo> disconnectionDataCard;
	private final int points;
	private final String[] handColor;

	public PlayerDisconnectionResendInfo(LinkedList <CardPlacedInfo> disconnectionDataCard, int points, String[] handColor)
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

	public String[] getHandColor()
	{
		return handColor;
	}
}
