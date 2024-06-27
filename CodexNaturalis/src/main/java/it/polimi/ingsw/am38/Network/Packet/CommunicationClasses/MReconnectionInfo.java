package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;
import it.polimi.ingsw.am38.Network.Packet.PlayerDisconnectionResendInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MReconnectionInfo extends MessageContent implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1754545354768L;

	private final String nickname;
	private final HashMap <String, PlayerDisconnectionResendInfo> playersInfo;
	private final int[] ownHand = new int[3];
	private final int upGc1, upGc2, upRc1, upRc2;
	private final Symbol goldTop, resourceTop;
	private final int sharedObj1, sharedObj2, personalObj;

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

	public HashMap <String, PlayerDisconnectionResendInfo> getPlayersInfo()
	{
		return playersInfo;
	}

	public int[] getOwnHand()
	{
		return ownHand;
	}

	public int getUpGc1() {
		return upGc1;
	}

	public int getUpGc2() {
		return upGc2;
	}

	public int getUpRc1() {
		return upRc1;
	}

	public int getUpRc2() {
		return upRc2;
	}

	public Symbol getGoldTop() {
		return goldTop;
	}

	public Symbol getResourceTop() {
		return resourceTop;
	}

	public String getNickname() {
		return nickname;
	}

	public int getSharedObj1() {
		return sharedObj1;
	}

	public int getSharedObj2() {
		return sharedObj2;
	}

	public int getPersonalObj() {
		return personalObj;
	}
}
