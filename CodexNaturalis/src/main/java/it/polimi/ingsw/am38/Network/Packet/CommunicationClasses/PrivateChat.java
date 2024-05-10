package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

public class PrivateChat extends MessageContent
{
	private String receiver;
	private String message;

	public PrivateChat(String receiver, String message)
	{
		this.receiver = receiver;
		this.message = message;
	}

	public String getReceiver()
	{
		return receiver;
	}

	public String getMessage()
	{
		return message;
	}
}
