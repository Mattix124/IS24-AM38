package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serializable;

public class MPrivateChat extends MessageContent implements Serializable
{
	private static final long serialVersionUID = 1756574768L;

	private String receiver;
	private String message;

	public MPrivateChat(String receiver, String message)
	{
		this.receiver = receiver;
		this.message = message;
	}

	public MPrivateChat(String receiver, StringBuilder message)
	{
		this.receiver = receiver;
		this.message = message.toString();
	}

	public String getReceiver()
	{
		return receiver;
	}

	public String getText()
	{
		return message;
	}
}
