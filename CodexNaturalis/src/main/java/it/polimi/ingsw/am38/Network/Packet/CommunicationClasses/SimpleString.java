package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

public class SimpleString extends MessageContent
{
	private final String string;

	public SimpleString(String string)
	{
		this.string = string;
	}

	public SimpleString(StringBuilder sb)
	{
		string = sb.toString();
	}


	public String getText()
	{
		return string;
	}
}
