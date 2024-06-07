package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPrivateChat;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;
import it.polimi.ingsw.am38.Network.Packet.Scope;

import java.util.LinkedList;

/**
 * ChatThread is a class that manage the chat of the game
 */
public class ChatThread extends Thread
{
	/**
	 * Instance of ServerMessageInterpreter
	 */
	private final ServerMessageSorter messageSorter;
	/**
	 * List of all player's attribute
	 */
	private final LinkedList <ServerProtocolInterface> pd;

	/**
	 * Constructor of ChatThread
	 *
	 * @param pd             PlayerData list
	 * @param msgInterpreter the ServerMessageSorter
	 */
	public ChatThread(LinkedList <ServerProtocolInterface> pd, ServerMessageSorter msgInterpreter)
	{
		this.pd = pd;
		this.messageSorter = msgInterpreter;
	}

	/**
	 * Running method to automate the chat
	 */
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
				for (ServerProtocolInterface inter : pd)
				{
					Player p = inter.getPlayer();

					if (!p.getNickname().equals(message.getSender()))
					{
						inter.chatMessage(content);
					}
				}
			}
			else
			{
				MPrivateChat effectiveMessage;
				effectiveMessage = (MPrivateChat) message.getContent();
				content = message.getSender() + " whispers to you: " + effectiveMessage.getText();

				for (ServerProtocolInterface inter : pd)
				{
					Player p = inter.getPlayer();
					if (p.getNickname().equals(effectiveMessage.getReceiver()))
					{
						inter.chatMessage(content);
					}
				}
			}
		}
	}

	/**
	 * Get the message from the ServerMessageSorter
	 *
	 * @return Message
	 */
	private Message getMessage()
	{
		return messageSorter.getChatMessage();
	}

	public void removePlayerData(String nick)
	{
		ServerProtocolInterface r = null;
		for (ServerProtocolInterface play : pd)
		{
			if (play.getPlayer().getNickname().equals(nick))
				r = play;
		}
		pd.remove(r);
	}
}
