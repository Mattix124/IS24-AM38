package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;
import it.polimi.ingsw.am38.Network.Packet.PlayerDisconnectionResendInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class MReconnectionInfo extends MessageContent implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1754545354768L;

	private HashMap <String, PlayerDisconnectionResendInfo> playersInfo;
	private int hangingDrownId;

	public MReconnectionInfo(HashMap <String, PlayerDisconnectionResendInfo> playersInfo, int id)
	{
		this.playersInfo = playersInfo;
		this.hangingDrownId = id;
	}

	public HashMap <String, PlayerDisconnectionResendInfo> getPlayersInfo()
	{
		return playersInfo;
	}

	public int getHangingDrownId()
	{
		return hangingDrownId;
	}
}
