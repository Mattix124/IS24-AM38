package it.polimi.ingsw.am38.Network.Packet;

import java.util.LinkedList;

public class MPlayersData extends MessageContent
{
    private String nickName;
    private final LinkedList <String> playersNicknames;

    public MPlayersData(String nickName, LinkedList<String> playersNicknames) {
        this.nickName = nickName;
        this.playersNicknames = playersNicknames;
    }

    public String getNickName() {
        return nickName;
    }

    public LinkedList<String> getPlayersNicknames() {
        return playersNicknames;
    }
}
