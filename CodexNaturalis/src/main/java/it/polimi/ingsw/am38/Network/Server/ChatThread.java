package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.Message;
import it.polimi.ingsw.am38.Network.Packet.Scope;

import java.util.LinkedList;

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
		Message       message;
		String        content;
		String[]      splittedMessage;
		StringBuilder effectiveMessage = new StringBuilder();
		while (true)
		{
			message = getMessage();
			content = (String) message.getContent();
			splittedMessage = content.split("/");
			effectiveMessage.append(splittedMessage[0]);
			if (message.getHeader2() == Scope.BCHAT)
			{
				for (int i = 1 ; i < splittedMessage.length ; i++)
					effectiveMessage.append(splittedMessage[i]);
				for (PlayerData playerData : pd)
				{
					Player p = playerData.getPlayer();

					if (!splittedMessage[0].equals(p.getNickname()))
					{
						playerData.getClOut().println(effectiveMessage);
					}
				}
			}
			else
			{
				effectiveMessage.append(" sends to you: ");
				for (int i = 2 ; i < splittedMessage.length ; i++)
					effectiveMessage.append(splittedMessage[i]);
				for (PlayerData playerData : pd)
				{
					Player p = playerData.getPlayer();
					if (splittedMessage[1].equals(p.getNickname()))
					{
						playerData.getClOut().println(effectiveMessage);
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
