package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * MPlayersData allow to send the nickName controlled by the server and the other players name to the client
 */
public class MPlayersData extends MessageContent implements Serializable
{
    @Serial
    private static final long serialVersionUID = 6548416768L;

    /**
     * Nickname of the player
     */
    private String nickName;
    /**
     * All nicknames of the players
     */
    private final LinkedList <String> playersNicknames;

    /**
     * Constructor of MPlayerData
     * @param nickName nickname of the player
     * @param playersNicknames list of all nicknames
     */
    public MPlayersData(String nickName, LinkedList<String> playersNicknames) {
        this.nickName = nickName;
        this.playersNicknames = playersNicknames;
    }

    /**
     * Getter for the player's nickname
     * @return nickname
     */
    public String getNickName() {
        return nickName;
    }
    /**
     * Getter for the all the player's nicknames
     * @return list of nicknames
     */
    public LinkedList<String> getPlayersNicknames() {
        return playersNicknames;
    }
}
