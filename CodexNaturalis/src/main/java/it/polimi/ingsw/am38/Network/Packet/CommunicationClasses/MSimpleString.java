package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serial;
import java.io.Serializable;

public class MSimpleString extends MessageContent implements Serializable
{
	@Serial
	private final static long serialVersionUID = 19898L;
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
