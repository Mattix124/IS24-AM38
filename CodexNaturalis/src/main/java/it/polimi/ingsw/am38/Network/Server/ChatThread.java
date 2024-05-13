package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPrivateChat;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;
import it.polimi.ingsw.am38.Network.Packet.Scope;

import java.io.IOException;
import java.util.LinkedList;

import static it.polimi.ingsw.am38.Network.Packet.Scope.BCHAT;
import static it.polimi.ingsw.am38.Network.Packet.Scope.CHAT;

public class ChatThread extends Thread
{
	private final ServerMessageSorter messageInterpreter;
	private final LinkedList <PlayerData> pd;

	public ChatThread(LinkedList <PlayerData> pd, ServerMessageSorter msgInterpreter)
	{
		this.pd = pd;
		this.messageInterpreter = msgInterpreter;
	}

	public void run()
	{
		Message message;
		String  content;

		while (true)
		{
			message = getMessage();
			if (message.getHeader2() == Scope.BCHAT)
			{
				MSimpleString effectiveMessage;
				effectiveMessage = (MSimpleString) message.getContent();
				content = message.getSender() + ": " + effectiveMessage.getText();
				for (PlayerData playerData : pd)
				{
					Player p = playerData.getPlayer();

					if (!p.getNickname().equals(message.getSender()))
					{
						try
						{
							playerData.getClOOut().writeObject(new Message(CHAT, BCHAT, new MSimpleString(content)));
						}
						catch (IOException e)
						{
							System.out.println("Message lost");
						}
					}
				}
			}
			else
			{
				MPrivateChat effectiveMessage;
				effectiveMessage = (MPrivateChat) message.getContent();
				content = message.getSender() + "whispers to you: " + effectiveMessage.getText();

				for (PlayerData playerData : pd)
				{
					Player p = playerData.getPlayer();
					if (p.getNickname().equals(effectiveMessage.getReceiver()))
					{
						try
						{
							playerData.getClOOut().writeObject(new Message(CHAT, CHAT, new MSimpleString(content)));
						}
						catch (IOException e)
						{
							System.out.println("Message lost");

						}
					}
				}
			}
		}

	}

	private Message getMessage()
	{
		return messageInterpreter.getChatMessage();
	}
}
