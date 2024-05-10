package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

public class SimpleString extends MessageContent
{
	private String string;

	public SimpleString(String string)
	{
		this.string = string;
	}

	public String getText()
	{
		return string;
	}
}
