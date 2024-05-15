package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Network.Packet.Message;

import java.util.LinkedList;

/**
 * Class created to simplify syntax in ServerMessageSorter
 */
public class GameList extends LinkedList<Message>
{
	/**
	 * Contains is a method that accept a string (player's nickname) to find his message
	 * @param nickname player's nickname
	 * @return true if found, false if not
	 */
	public boolean contains(String nickname)
	{
		for(Message m : this)
			 if(m.getSender().equals(nickname))
				 return true;
		return false;
	}
}

