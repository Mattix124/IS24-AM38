package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Network.Packet.Message;

import java.util.LinkedList;

public class GameList extends LinkedList<Message>
{
	public boolean contains(String o)
	{
		for(Message m : this)
			 if(m.getSender().equals(o))
				 return true;
		return false;
	}
}

