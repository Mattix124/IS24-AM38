package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

public class MSimpleString extends MessageContent
{
	private final String string;

	public MSimpleString(String string)
	{
		this.string = string;
	}

	public MSimpleString(StringBuilder sb)
	{
		string = sb.toString();
	}


	public String getText()
	{
		return string;
	}
}
